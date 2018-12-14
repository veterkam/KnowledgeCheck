package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import edu.javatraining.knowledgecheck.controller.dto.*;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import com.google.inject.Inject;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import edu.javatraining.knowledgecheck.service.StudentService;
import edu.javatraining.knowledgecheck.service.TutorService;
import edu.javatraining.knowledgecheck.service.UserService;
import edu.javatraining.knowledgecheck.tools.*;

import javax.mail.MessagingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet(urlPatterns = {
        "/account/login",
        "/account/logout",
        "/account/registration",
        "/account/registration/back",
        "/account/registration/confirm",
        "/account/recovery",
        "/account/recovery/back",
        "/account/recovery/confirm",
        "/account/myprofile",
        "/account/myprofile/back",
        "/account/myprofile/confirm",
        "/account/users",
        "/account/users/remove",
        "/account/profile/*"
})

@Singleton
public class AccountControllerServlet extends AbstractBaseControllerServlet {
    private final int COUNT_USER_ON_PAGE = 10;
    private final int PAGINATION_LIMIT = 5;

    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<TutorService> tutorServiceProvider;
    @Inject
    private Provider<StudentService> studentServiceProvider;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        String action = request.getServletPath();

        try {
            switch (action) {
                case "/account/login":
                    loginForm(request, response);
                    break;

                case "/account/logout":
                    logout(request, response);
                    break;

                case "/account/registration":
                    showRegistrationForm(request, response);
                    break;

                case "/account/recovery":
                    showRecoveryForm(request, response);
                    break;

                case "/account/myprofile":
                    showMyProfileForm(request, response);
                    break;

                case "/account/users":
                    showUserList(request, response);
                    break;

                case "/account/users/remove":
                    removeUser(request, response);
                    break;

                case "/account/users/verify":
                    changeUserVerificationStatus(request, response);
                    break;

                case "/account/profile":
                    showProfile(request, response);
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        String action = request.getServletPath();
        try {
            switch (action) {
                case "/account/login":
                    loginProcessing(request, response);
                    break;
                case "/account/registration":
                    // User click button Next, verify the fields
                    registrationProcessing(request, response);
                    break;
                case "/account/registration/confirm":
                    registrationConfirmEmail(request, response);
                    break;
                case "/account/recovery":
                    // User click button Next, verify the fields
                    recoveryProcessing(request, response);
                    break;
                case "/account/recovery/confirm":
                    recoveryConfirmEmail(request, response);
                    break;
                case "/account/myprofile":
                    // User click button Next, verify the fields
                    myProfileProcessing(request, response);
                    break;
                case "/account/myprofile/confirm":
                    myProfileConfirmEmail(request, response);
                    break;
                case "/account/profile":
                    showProfile(request, response);
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private void loginForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        genFormId(request.getSession());
        forward(request, response, VIEW_LOGIN_FORM);
    }

    private void loginProcessing(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        if(!checkFormId(request)) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserService userService = userServiceProvider.get();
        User user = userService.findOneByUsername(username);

        if (user != null && user.isVerified() &&
                Cipher.validate(password, user.getPassword())) {

            password = Cipher.encode(password);
            user.setPassword(password);
            userService.updatePassword(user);
            user = attachProfile(user);

            alertManager.success("app.account.login_success");
            request.getSession().setAttribute("user", user);
            redirect(request, response, "/");
        } else {
            alertManager.danger("app.account.login_error");
            loginForm(request, response);
        }
    }

    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if( request.getParameter("back") == null ) {
            request.getSession().setAttribute("userDto", null);
        }

        forwardRegistrationForm(request, response);
    }

    private void forwardRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        genFormId(request.getSession());
        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());
        forward(request, response, VIEW_REGISTRATION_FORM);
    }


    private void registrationProcessing(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if(!checkFormId(request)) {
            pageNotFound(request, response);
            return;
        }

        // Process parameters
        UserDto userDto = extractUserDto(request);
        Map<String, List<String>> errors = DtoValidator.validate(userDto);

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("userDto", userDto);

        AlertManager alertManager = getAlertManager(request);
        if (!errors.isEmpty()) {
            alertManager.danger("app.account.validation.incorrect_data_entered");
            request.setAttribute("errors", errors);
            forwardRegistrationForm(request, response);
            return;
        }

        // Is the username unique?
        UserService userService = userServiceProvider.get();
        User user = userService.findOneByUsername(userDto.getUsername());

        if (user == null) {
            // The username is unique. Verify e-mail.
            String msg = "Hello. Thanks for registration in KnowledgeCheck!";
            askVerificationCode(userDto.getEmail(), msg, request);

        } else {
            // The username is not unique.
            alertManager.danger("app.account.username_already_exists");
        }

        forwardRegistrationForm(request, response);
    }

    private void registrationConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        if(!checkFormId(request)) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);

        if ( checkConfirmation(request) ) {
            // Insert user to data base.
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) session.getAttribute("userDto");
            User user = userDto.toUser();

            boolean verified = ( user.getRole() != User.Role.ADMINISTRATOR );
            user.setVerified(verified);

            String password = Cipher.encode(user.getPassword());
            user.setPassword(password);

            try {
                UserService userService = userServiceProvider.get();
                userService.insert(user);
            } catch (DAOException e) {

                Throwable childException = e.getCause();
                if(childException instanceof SQLException) {
                    SQLException sqlException = (SQLException) childException;
                    if (sqlException.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                        alertManager.danger("app.account.username_is_already_busy");
                    } else {
                        logger.error(e.getMessage(), e);
                    }
                }

                // Failed! Come back to edit user info
                alertManager.danger("app.account.registration_failed");
                forwardRegistrationForm(request, response);
                return;
            }

            // Success!
            session.setAttribute("userDto", null);
            alertManager.success("app.account.registration_success");

            if(user.isVerified()) {
                // Store user data in session
                user = attachProfile(user);
                session.setAttribute("user", user);
                alertManager.success("app.account.welcome");
            } else {
                alertManager.success("app.account.login_after_verification");
            }
            redirect(request, response, "/");

        } else {
            // Verification code is wrong. Come back to edit verification code
            alertManager.danger("app.account.code_is_wrong");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
            forwardRegistrationForm(request, response);
        }
    }


    private void showRecoveryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if(request.getParameter("back") == null) {
            request.getSession().setAttribute("userDto", null);
        }

        forwardRecoveryForm(request, response);
    }

    private void forwardRecoveryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        genFormId(request.getSession());
        forward(request, response, VIEW_PASSWORD_RECOVERY_FORM);
    }

    private void recoveryProcessing(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

         if(!checkFormId(request)) {
            pageNotFound(request, response);
            return;
        }

        // Process parameters
        UserRecoveryDto userDto = extractUserRecoveryDto(request);
        Map<String, List<String>> errors = DtoValidator.validate(userDto);

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("userDto", userDto);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            forwardRecoveryForm(request, response);
            return;
        }


        AlertManager alertManager = getAlertManager(request);

        UserService userService = userServiceProvider.get();
        User user = userService.findOneByUsername(userDto.getUsername());

        if (user != null && user.isVerified() && user.getEmail().equals(userDto.getEmail())) {
            String msg = "Hello. You started password recovering. ";
            askVerificationCode(user.getEmail(), msg, request);
        } else {
            String errorMsg = "app.account.can_not_find_username_and_email";
            alertManager.danger(errorMsg);
        }

        forwardRecoveryForm(request, response);
    }

    private void recoveryConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        if(!checkFormId(request)) {
            pageNotFound(request, response);
            return;
        }

        HttpSession session = request.getSession();
        AlertManager alertManager = getAlertManager(request);

        if ( checkConfirmation(request) ) {
            UserRecoveryDto userDto = (UserRecoveryDto) session.getAttribute("userDto");
            UserService userService = userServiceProvider.get();
            User user = userService.findOneByUsername(userDto.getUsername());

            if (user != null) {
                user = attachProfile(user);
                String password = Cipher.encode(userDto.getPassword());
                user.setPassword(password);
                if ( userService.updatePassword(user) ) {
                    session.setAttribute("user", user);
                    session.setAttribute("userDto", null);
                    alertManager.success("app.account.recovery_password_success");
                    redirect(request, response, "/");
                    return;
                } else {
                    alertManager.danger("app.account.can_not_change_password");
                }
            } else {
                alertManager.danger("app.account.can_not_find_specified_user");
            }

        } else {
            // Verification code is wrong. Come back to edit verification code
            alertManager.danger("app.account.code_is_wrong");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
        }

        forwardRecoveryForm(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // remove user data from session
        request.getSession().setAttribute("user", null);
        AlertManager alertManager = getAlertManager(request);
        alertManager.success("app.account.logout_success");
        redirect(request, response, "/");
    }

    private void showMyProfileForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        if( request.getParameter("back") == null ) {

            request.getSession().setAttribute("userDto",
                    convertUserToUserDto(user));
        }

        forwardMyProfileForm(request, response);
    }

    private UserDto convertUserToUserDto(User u) {

        UserDto userDto;

        switch(u.getRole()) {
            case TUTOR:
                userDto = new TutorDto();
                ((TutorDto) userDto).fromTutor((Tutor) u);
                break;
            case STUDENT:
                userDto = new StudentDto();
                ((StudentDto) userDto).fromStudent((Student) u);
                break;
            default:
                userDto = new UserDto();
                userDto.fromUser(u);
        }

        return userDto;
    }

    private void forwardMyProfileForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        genFormId(request.getSession());
        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());
        forward(request, response, VIEW_MY_PROFILE_FORM);
    }

    private void myProfileProcessing(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        if (!checkFormId(request) || user == null) {
            pageNotFound(request, response);
            return;
        }

        // Process parameters
        UserDto userDto = extractUserDto(request, user.getRole());
        Map<String, List<String>> errors = DtoValidator.validate(userDto);

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("userDto", userDto);

        AlertManager alertManager = getAlertManager(request);
        if (!errors.isEmpty()) {
            alertManager.danger("app.account.validation.incorrect_data_entered");
            request.setAttribute("errors", errors);
            forwardMyProfileForm(request, response);
            return;
        }

        boolean isUnique = true;
        if (!user.getUsername().equals(userDto.getUsername())) {
            // Is the username unique?
            UserService userService = userServiceProvider.get();
            isUnique = (userService.findOneByUsername(userDto.getUsername()) == null);
        }

        if (isUnique) {
            // The username is unique. Verify e-mail.
            String msg = "Hello. To confirm profile modification, please use verification code.";
            askVerificationCode(userDto.getEmail(), msg, request);
        } else {
            // The username is not unique.
            alertManager.danger("app.account.username_already_exists");
        }
        forwardMyProfileForm(request, response);
    }

    private void myProfileConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        if(!checkFormId(request) || user == null) {
            pageNotFound(request, response);
            return;
        }

        if ( checkConfirmation(request) ) {
            // save profile modification
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) session.getAttribute("userDto");
            User modifedUser = userDto.toUser();
            modifedUser.setId(user.getId());
            modifedUser.setVerified(user.isVerified());
            String password = Cipher.encode(modifedUser.getPassword());
            modifedUser.setPassword(password);
            saveMyProfile(modifedUser, request, response);
        } else {
            // Verification code is wrong. Come back to edit verification code
            getAlertManager(request).danger("Verification code is wrong. Please, try again!");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
            showMyProfileForm(request, response);
        }
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMINISTRATOR) {
            pageNotFound(request, response);
            return;
        }

        UserService userService = userServiceProvider.get();

        // Scan parameters
        // Current page of test list
        int pageNo = getPageNo(request);

        // Calc test count
        Long count = userService.count();

        // Init pagination
        int pageCount = (int) Math.ceil(((double)count) / ((double)COUNT_USER_ON_PAGE));
        Pagination pagination = new Pagination(pageNo, pageCount, PAGINATION_LIMIT);
        pagination.setElementLimit(COUNT_USER_ON_PAGE);

        // Read user list
        Long offset = (long)(pagination.getCurrent() - 1) * COUNT_USER_ON_PAGE;

        List<User> users = userService.findAll(offset, (long) COUNT_USER_ON_PAGE);

        for(int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            switch( u.getRole() ) {
                case TUTOR:
                    TutorService tutorService = tutorServiceProvider.get();
                    Tutor tutor = tutorService.findOneById(u.getId());
                    users.set(i, tutor);
                    break;
                case STUDENT:
                    StudentService studentService = studentServiceProvider.get();
                    Student student = studentService.findOneById(u.getId());
                    users.set(i, student);
                    break;
            }
        }

        request.setAttribute("users", users);
        request.setAttribute("pagination", pagination);
        forward(request, response, VIEW_USER_LIST);
    }

    private void removeUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMINISTRATOR) {
            pageNotFound(request, response);
            return;
        }

        String username = request.getParameter("username");

        try {
            UserService userService = userServiceProvider.get();
            userService.delete( userService.findOneByUsername(username) );
            getAlertManager(request).success("app.account.user_removed_successfully");
        } catch(DAOException e) {
            logger.trace(e.getMessage(), e);
            getAlertManager(request).danger("app.account.failed_remove_user");
        }

        redirect(request, response, "/account/users");
    }

    private void changeUserVerificationStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMINISTRATOR) {
            pageNotFound(request, response);
            return;
        }

        String strVerified = request.getParameter("verified");
        String username = request.getParameter("username");

        boolean verified = ( "true".equals(strVerified) );

        try {
            UserService userService = userServiceProvider.get();
            User u = userService.findOneByUsername(username);
            u.setVerified(verified);
            userService.update(u);
            getAlertManager(request).success("app.account.user_status_update_successfully");
        } catch (DAOException e) {
            logger.trace(e.getMessage(), e);
            getAlertManager(request).danger("app.account.failed_update_user_status");
        }

        redirect(request, response, "/account/users");
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DAOException {

        // Extract username from URL
        String username = request.getPathInfo().substring(1);

        if(username == null) {
            pageNotFound(request, response);
            return;
        }

        User user = userServiceProvider.get().findOneByUsername(username);
        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        user = attachProfile(user);

        request.setAttribute("userData", user);
        forward(request, response, VIEW_PROFILE);
    }

    private int getPageNo(HttpServletRequest request) {
        String pageNoStr = request.getParameter("pageNo");
        int pageNo;
        try {
            pageNo = Integer.parseInt(pageNoStr);
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        return pageNo;
    }

    private boolean checkConfirmation(HttpServletRequest request) {
        String code = request.getParameter("verificationCode");
        String expectedCode = (String) request.getSession().getAttribute("verificationCode");

        return expectedCode.equals(code);
    }

    private void saveMyProfile(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        boolean success;
        try {
            success = saveUser(user);
        } catch(DAOException e) {
            logger.error(e.getMessage(), e);
            success = false;
        }

        if( success ) {
            request.getSession().setAttribute("user", user);
            getAlertManager(request).success("app.account.profile_save_success");
        } else {
            getAlertManager(request).danger("app.account.profile_save_failed");
        }

        redirect(request, response, "/account/myprofile");
    }

    private boolean saveUser(User user) {
        switch(user.getRole()) {
            case  STUDENT:
                StudentService studentService = studentServiceProvider.get();
                return studentService.update( (Student) user );
            case  TUTOR:
                TutorService tutorService = tutorServiceProvider.get();
                return tutorService.update( (Tutor) user );
            default:
                UserService userService = userServiceProvider.get();
                return userService.update( user );
        }
    }

    private UserDto extractUserDto(HttpServletRequest request, User.Role role) {
        switch(role) {
            case STUDENT:
                return extractStudentDto(request);
            case TUTOR:
                return extractTutorDto(request);
            default:
                return extractUserDto(request);
        }
    }

    private TutorDto extractTutorDto(HttpServletRequest request) {

        TutorDto tutorDto = new TutorDto();
        extractUserDto(tutorDto, request);
        tutorDto.setPosition( request.getParameter("position") );
        tutorDto.setScientificDegree( request.getParameter("scientificDegree") );
        tutorDto.setAcademicTitle( request.getParameter("academicTitle") );

        return tutorDto;
    }

    private StudentDto extractStudentDto(HttpServletRequest request) {

        StudentDto studentDto = new StudentDto();
        extractUserDto(studentDto, request);
        studentDto.setSpecialty( request.getParameter("specialty") );
        studentDto.setGroup( request.getParameter("group") );
        studentDto.setYear( request.getParameter("year") );

        return studentDto;
    }

    private UserDto extractUserDto(HttpServletRequest request) {
        UserDto userDto = new UserDto();
        extractUserDto(userDto, request);
        return userDto;
    }

    private void extractUserDto(UserDto out, HttpServletRequest request) {
        // Process parameters
        extractUserRecoveryDto(out, request);
        out.setFirstName(request.getParameter("firstName"));
        out.setLastName(request.getParameter("lastName"));
        out.setRole(request.getParameter("role"));
    }

    private UserRecoveryDto extractUserRecoveryDto(HttpServletRequest request) {
        UserRecoveryDto userRecoveryDto = new UserRecoveryDto();
        extractUserRecoveryDto(userRecoveryDto, request);
        return userRecoveryDto;
    }

    private void extractUserRecoveryDto(UserRecoveryDto out, HttpServletRequest request) {
        // Process parameters
        out.setEmail(request.getParameter("email"));
        out.setUsername(request.getParameter("username"));
        out.setPassword(request.getParameter("password"));
        out.setConfirmPassword(request.getParameter("confirmPassword"));
    }

    private String sendVerificationCodeByEmail(String email, String msg)
            throws MessagingException {

        Properties props = PropertyFileReader.read("/configure.properties");

        if("false".equals(props.getProperty("email.active"))) {
            return "123";
        }

        String username = props.getProperty("email.user");
        String password = props.getProperty("email.password");

        EmailSender sender = new EmailSender(username, password);

        // Generate e-mail verification code
        Integer rnd = (int) (Math.random() * 1000000 + 1000000);
        String code = rnd.toString();
        String subject = "KnowledgeCheck Verification Code";
        msg += "\nYour verification code is " + code;
        sender.send(subject, msg, "KnowledgeCheck@knowledgecheck.com", email);

        return code;
    }

    private void askVerificationCode(String email, String msg, HttpServletRequest request) {
        AlertManager alertManager = getAlertManager(request);
        try {
            String code = sendVerificationCodeByEmail(email, msg);
            request.getSession().setAttribute("verificationCode", code);
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
            alertManager.info("app.account.we_sent_you_code");
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            alertManager.danger("app.account.can_not_send_code");
        }
    }

    private User attachProfile(User user) {
        switch(user.getRole()) {
            case TUTOR:
                TutorService tutorService = tutorServiceProvider.get();
                return tutorService.attachProfile(user);
            case STUDENT:
                StudentService studentService = studentServiceProvider.get();
                return studentService.attachProfile(user);
            default:
                return user;
        }
    }
}
