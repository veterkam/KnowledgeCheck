package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import edu.javatraining.knowledgecheck.controller.dto.DtoValidator;
import edu.javatraining.knowledgecheck.controller.dto.UserDto;
import edu.javatraining.knowledgecheck.controller.dto.UserRecoveryDto;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.exception.RequestException;
import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import com.google.inject.Inject;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import edu.javatraining.knowledgecheck.service.StudentService;
import edu.javatraining.knowledgecheck.service.TutorService;
import edu.javatraining.knowledgecheck.service.UserService;
import edu.javatraining.knowledgecheck.service.tools.*;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
        "/account/profile"
})

@Singleton
public class AccountControllerServlet extends AbstractBaseControllerServlet {
    private final int COUNT_USER_ON_PAGE = 20;
    private final int PAGINATION_LIMIT = 5;

    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<TutorService> tutorServiceProvider;
    @Inject
    private Provider<StudentService> studentServiceProvider;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
                    request.getSession().setAttribute("anonym", null);
                    showMyProfileForm(request, response);
                    break;

                case "/account/users":
                    showUserList(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
//                case "/account/myprofile":
//                    showMyProfileForm(request, response);
//                    break;
                case "/account/myprofile":
                    // User click button Next, verify the fields
                    myProfileProcessing(request, response);
                    break;
                case "/account/myprofile/confirm":
                    myProfileConfirmEmail(request, response);
                    break;
//                case "/account/users":
//                    showUserList(request, response);
//                    break;
                case "/account/users/remove":
                    removeUser(request, response);
                    break;
                case "/account/users":
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
            // Store user data in session
            session.setAttribute("userDto", null);
            alertManager.success("app.account.registration_success");

            if(user.isVerified()) {
                session.setAttribute("user", user);
                alertManager.success("app.common.welcome");
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
        response.sendRedirect(request.getContextPath() + "/");
    }

    private void showMyProfileForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_MY_PROFILE_FORM);
        dispatcher.forward(request, response);
    }



    private void myProfileProcessing(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        User modifyUser = extractUserDto(request, user.getRole());

        if(modifyUser.getFirstName() == null) {
            pageNotFound(request, response);
            return;
        }

        String repeatPassword = request.getParameter("repeatPassword");
        if(repeatPassword == null){
            // user didn't change password
            modifyUser.setPassword(user.getPassword());
        }

        // Add disabled fields
        modifyUser.setId(user.getId());
        modifyUser.setUsername(user.getUsername());
        modifyUser.setRole(user.getRole());
        modifyUser.setVerified(true);

        Validator validator = new Validator();
        validator.validate(modifyUser);

        AlertManager alertManager = getAlertManager(request);
        boolean isFailed = false;
        if(validator.isFailed()) {
            alertManager.danger(validator.getErrors());
            isFailed = true;
        }

        if(repeatPassword != null) {
            // user changed password
            // validate password
            if (!repeatPassword.equals(modifyUser.getPassword())) {
                alertManager.danger("Passwords are different.");
                isFailed = true;
            }
        }

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("anonym", modifyUser);

        if (isFailed) {
            showMyProfileForm(request, response);
            return;
        }

        session.setAttribute("passwordChanged", repeatPassword != null );

        if(repeatPassword != null || !user.getEmail().equals(modifyUser.getEmail()) ) {
            // User modified password or email, ask confirmation
            String msg = "Hello. To confirm profile modification, please use verification code.";
            askVerificationCode(modifyUser.getEmail(), msg, request);
            showMyProfileForm(request, response);
        } else {
            // User didn't modify password and email, save changes without asking confirmation
            saveMyProfile(modifyUser, request, response);
        }
    }

    private void myProfileConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        boolean confirm;

        try {
            confirm = checkConfirmation(request);
        } catch (RequestException e) {
            pageNotFound(request, response);
            return;
        }

        if ( confirm ) {
            // save profile modification
            HttpSession session = request.getSession();
            user = (User) session.getAttribute("anonym");

            boolean pswChanged = (Boolean) session.getAttribute("passwordChanged");
            if(pswChanged) {
                String password = Cipher.encode(user.getPassword());
                user.setPassword(password);
            }
            saveMyProfile(user, request, response);
        } else {
            // Verification code is wrong. Come back to edit verification code
            getAlertManager(request).danger("Verification code is wrong. Please, try again!");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
            showMyProfileForm(request, response);
        }
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DAOException {

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
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_USER_LIST);
        dispatcher.forward(request, response);
    }

    private void removeUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMINISTRATOR) {
            pageNotFound(request, response);
            return;
        }

        String strId = request.getParameter("id");
        Long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        try {
            UserService userService = userServiceProvider.get();
            userService.deleteById(id);
            getAlertManager(request).success("The user is removed successfully");
        } catch(DAOException e) {
            logger.trace(e.getMessage(), e);
            getAlertManager(request).danger("Can not remove user. Try again!");
        }
        response.sendRedirect(request.getContextPath() + "/account/users");
    }

    private void changeUserVerificationStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMINISTRATOR) {
            pageNotFound(request, response);
            return;
        }

        String strId = request.getParameter("id");
        Long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        String strVerified = request.getParameter("verified");
        boolean verified = ( "true".equals(strVerified) );

        try {
            UserService userService = userServiceProvider.get();
            User u = userService.findOneById(id);
            u.setVerified(verified);
            userService.update(u);
            getAlertManager(request).success("The verification status of user is updated successfully");
        } catch (DAOException e) {
            logger.trace(e.getMessage(), e);
            getAlertManager(request).danger("Can not updateUpdate verification status of user. Try again!");
        }

        response.sendRedirect(request.getContextPath() + "/account/users");
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DAOException {

        String strId = request.getParameter("id");
        Long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        User data = studentServiceProvider.get().findOneById(id);
        if(data == null) {
            pageNotFound(request, response);
            return;
        }

        switch(data.getRole()) {
            case STUDENT:
                data = studentServiceProvider.get().findOneById(id);
                break;
            case TUTOR:
                data = tutorServiceProvider.get().findOneById(id);
                break;
            default:
                pageNotFound(request, response);
                return;
        }

        request.setAttribute("userData", data);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_PROFILE);
        dispatcher.forward(request, response);
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
            getAlertManager(request).success("Profile data saved successfully.");
        } else {
            getAlertManager(request).danger("Profile data saving failed. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/account/myprofile");
    }

    private boolean saveUser(User user) throws DAOException {
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

    private User extractUserDto(HttpServletRequest request, User.Role role) {
        switch(role) {
            case STUDENT:
                return extractStudent(request);
            case TUTOR:
                return extractTutor(request);
            default:
                return extractUser(request);
        }
    }

    private Tutor extractTutor(HttpServletRequest request) {
        Tutor tutor = new Tutor();
        extractUser(tutor, request);
        String position = request.getParameter("position");
        String scientificDegree = request.getParameter("scientificDegree");
        String academicTitle = request.getParameter("academicTitle");

        tutor.setPosition(position);
        tutor.setScientificDegree(scientificDegree);
        tutor.setAcademicTitle(academicTitle);

        return tutor;
    }

    private Student extractStudent(HttpServletRequest request) {
        Student student = new Student();
        extractUser(student, request);
        String specialty = request.getParameter("specialty");
        String group = request.getParameter("group");
        String strYear = request.getParameter("year");

        int year;
        try {
            year = Integer.parseInt(strYear);
        } catch (NumberFormatException e) {
            year = 0;
        }

        student.setSpecialty(specialty);
        student.setGroup(group);
        student.setYear(year);

        return student;
    }

    private User extractUser(HttpServletRequest request) {
        User user = new User();
        extractUser(user, request);
        return user;
    }

    private void extractUser(User out, HttpServletRequest request) {
        // Process parameters
        out.setFirstName(request.getParameter("firstName"));
        out.setLastName(request.getParameter("lastName"));
        out.setEmail(request.getParameter("email"));
        out.setUsername(request.getParameter("username"));
        out.setPassword(request.getParameter("password"));
    }

    private UserDto extractUserDto(HttpServletRequest request) {
        UserDto userDto = new UserDto();
        extractUserDto(userDto, request);
        return userDto;
    }

    private void extractUserDto(UserDto out, HttpServletRequest request) {
        // Process parameters
        out.setFirstName(request.getParameter("firstName"));
        out.setLastName(request.getParameter("lastName"));
        out.setEmail(request.getParameter("email"));
        out.setUsername(request.getParameter("username"));
        out.setPassword(request.getParameter("password"));
        out.setConfirmPassword(request.getParameter("confirmPassword"));
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
        if(true) return "123";
        String username = getServletContext().getInitParameter("emailUsername");
        String password = getServletContext().getInitParameter("emailPassword");

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

    private User attachProfile(User user) throws DAOException {
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
