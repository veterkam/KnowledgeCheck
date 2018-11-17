package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.exception.RequestException;
import com.epam.javatraining.knowledgecheck.model.dao.StudentDao;
import com.epam.javatraining.knowledgecheck.model.dao.TutorDao;
import com.epam.javatraining.knowledgecheck.model.dao.UserDao;
import com.epam.javatraining.knowledgecheck.model.entity.Student;
import com.epam.javatraining.knowledgecheck.model.entity.Tutor;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.*;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.logging.log4j.util.Strings;

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

@WebServlet(urlPatterns = {
        "/authorization/login",
        "/authorization/login/do",
        "/authorization/logout",
        "/authorization/register",
        "/authorization/register/back",
        "/authorization/register/confirm",
        "/authorization/register/verify",
        "/authorization/recovery",
        "/authorization/recovery/back",
        "/authorization/recovery/confirm",
        "/authorization/recovery/verify",
        "/authorization/myprofile",
        "/authorization/myprofile/back",
        "/authorization/myprofile/confirm",
        "/authorization/myprofile/verify",
        "/authorization/users",
        "/authorization/users/remove",
        "/authorization/users/verify",
        "/authorization/profile"
})
public class AuthorizationControllerServlet extends AbstractBaseControllerServlet {
    private final int COUNT_USER_ON_PAGE = 20;
    private final int PAGINATION_LIMIT = 5;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();
        try {
            switch (action) {
                case "/authorization/register":
                    request.getSession().setAttribute("anonym", null);
                    showRegisterForm(request, response);
                    break;
                case "/authorization/register/back":
                    showRegisterForm(request, response);
                    break;
                case "/authorization/register/verify":
                    // User click button Next, verify the fields
                    registerVerifyUserInfo(request, response);
                    break;
                case "/authorization/register/confirm":
                    registerConfirmEmail(request, response);
                    break;
                case "/authorization/recovery":
                    request.getSession().setAttribute("anonym", null);
                    recoveryForm(request, response);
                    break;
                case "/authorization/recovery/back":
                    recoveryForm(request, response);
                    break;
                case "/authorization/recovery/verify":
                    // User click button Next, verify the fields
                    recoveryVerifyUserInfo(request, response);
                    break;
                case "/authorization/recovery/confirm":
                    recoveryConfirmEmail(request, response);
                    break;
                case "/authorization/login":
                    loginForm(request, response);
                    break;
                case "/authorization/login/do":
                    loginDo(request, response);
                    break;
                case "/authorization/logout":
                    logout(request, response);
                    break;
                case "/authorization/myprofile":
                    request.getSession().setAttribute("anonym", null);
                    showMyProfileForm(request, response);
                    break;
                case "/authorization/myprofile/back":
                    showMyProfileForm(request, response);
                    break;
                case "/authorization/myprofile/verify":
                    // User click button Next, verify the fields
                    verifyProfileInfo(request, response);
                    break;
                case "/authorization/myprofile/confirm":
                    profileConfirmEmail(request, response);
                    break;
                case "/authorization/users":
                    showUserList(request, response);
                    break;
                case "/authorization/users/remove":
                    removeUser(request, response);
                    break;
                case "/authorization/users/verify":
                    changeUserVerificationStatus(request, response);
                    break;
                case "/authorization/profile":
                    showProfile(request, response);
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (DAOException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void loginForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_LOGIN_FORM);
        dispatcher.forward(request, response);
    }

    private void loginDo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, DAOException {

        AlertManager alertManager = getAlertManager(request);
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDao userDao = new UserDao();
        User user = userDao.get(username);

        if (user != null && user.isVerified() && user.getPassword().equals(password)) {

            if( user.getRole() == User.Role.TUTOR) {
                TutorDao tutorDao = new TutorDao();
                user = tutorDao.get(user.getId());
            } else if( user.getRole() == User.Role.STUDENT) {
                StudentDao studentDao = new StudentDao();
                user = studentDao.get(user.getId());
            }

            alertManager.success("Registration success! Welcome " + user.getFullname());
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            alertManager.danger("Username or password is wrong. Please, try again!");
            response.sendRedirect(request.getContextPath() + "/authorization/login");
        }
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_REGISTER_FORM);
        dispatcher.forward(request, response);
    }


    private void registerVerifyUserInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException{

        // Process parameters
        User anonym = extractUser(request);

        if ( anonym.getFirstname()== null) {
            pageNotFound(request, response);
            return;
        }

        String repeatPassword = request.getParameter("repeatPassword");
        String role = request.getParameter("role");

        Validator validator = new Validator();
        validator.validate(anonym);
        validator.validatePassword(repeatPassword, "repeat password");
        validator.isNotBlank(role, "role");

        AlertManager alertManager = getAlertManager(request);

        boolean isFailed = false;
        if(validator.isFailed()) {
            alertManager.danger(validator.getErrors());
            isFailed = true;
        }

        if (anonym.getPassword() != null && !anonym.getPassword().equals(repeatPassword)) {
            alertManager.danger("Passwords are different.");
            isFailed = true;
        }

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("anonym", anonym);

        if (isFailed) {
            showRegisterForm(request, response);
            return;
        }

        UserDao userDao = new UserDao();
        // Is the username unique?
        User user = userDao.get(anonym.getUsername());

        if (user == null) {
            // The username is unique. Verify e-mail.
            String msg = "Hello. Thanks for registration in KnowledgeCheck!";
            askVerificationCode(anonym.getEmail(), msg, request);

        } else {
            // The username is not unique.
            alertManager.danger("Registration failed. A user with this username already exists. Please, try again!");
        }

        showRegisterForm(request, response);
    }

    private void registerConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean confirm;

        try {
            confirm = checkConfirmation(request);
        } catch (RequestException e) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);

        if ( confirm ) {
            // Insert user to data base.
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("anonym");

            boolean verified = ( user.getRole() != User.Role.ADMINISTRATOR );
            user.setVerified(verified);

            try {
                new UserDao().insert(user);
            } catch (DAOException e) {

                Throwable childException = e.getCause();
                if(childException instanceof SQLException) {
                    SQLException sqlException = (SQLException) childException;
                    if (sqlException.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                        alertManager.danger("Sorry, it's too late, username is already busy.");
                    } else {
                        logger.error(e.getMessage(), e);
                    }
                }

                // Failed! Come back to edit user info
                alertManager.danger("Registration failed. Please, try again!");
                showRegisterForm(request, response);
                return;
            }

            // Success!
            // Store user data in session
            session.setAttribute("anonym", null);
            session.setAttribute("user", user);
            alertManager.success("Registration success! Welcome " + user.getFullname());
            response.sendRedirect(request.getContextPath() + "/");

        } else {
            // Verification code is wrong. Come back to edit verification code
            alertManager.danger("Verification code is wrong. Please, try again!");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
            showRegisterForm(request, response);
        }
    }


    private void recoveryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_PASSWORD_RECOVERY_FORM);
        dispatcher.forward(request, response);
    }

    private void recoveryVerifyUserInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // Process parameters
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");

        if (Validator.containNull(email, username, password, repeatPassword)) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);

        boolean isFailed = false;
        Validator validator = new Validator();
        validator.validateEmail(email);
        validator.validateUsername(username);
        validator.validatePassword(password);
        validator.validatePassword(repeatPassword);

        if(validator.isFailed()) {
            alertManager.danger(validator.getErrors());
            isFailed = true;
        }

        if (!password.equals(repeatPassword)) {
            alertManager.danger("Passwords are different.");
            isFailed = true;
        }

        User anonym = new User();
        anonym.setEmail(email);
        anonym.setUsername(username.toLowerCase());
        anonym.setPassword(password);

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("anonym", anonym);

        if (isFailed) {
            recoveryForm(request, response);
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.get(anonym.getUsername());

        if (user != null && user.isVerified() && user.getEmail().equals(email)) {
            String msg = "Hello. You started password recovering. ";
            askVerificationCode(email, msg, request);
        } else {
            String errorMsg = "Can't find username " + anonym.getUsername() +
                    " with e-mail " + email + ". Please, try again!";
            alertManager.danger(errorMsg);
        }

        recoveryForm(request, response);
    }

    private void recoveryConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, DAOException, IOException {
        HttpSession session = request.getSession();
        User anonym = (User) session.getAttribute("anonym");


        boolean confirm;

        try {
            confirm = checkConfirmation(request);
        } catch (RequestException e) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);

        if ( confirm ) {
            UserDao userDao = new UserDao();
            User user = userDao.get(anonym.getUsername());

            if (user != null) {
                user.setPassword(anonym.getPassword());
                if ( userDao.update(user) ) {
                    session.setAttribute("user", user);
                    session.setAttribute("anonym", null);
                    alertManager.success("Recovery password success! Welcome " + user.getFullname());
                    response.sendRedirect(request.getContextPath() + "/");
                    return;
                } else {
                    alertManager.danger("Sorry, can't update " + anonym.getUsername() + ". Please, try again!");
                }
            } else {
                alertManager.danger("Sorry, can't find " + anonym.getUsername() + ". Please, try again!");
            }

        } else {
            // Verification code is wrong. Come back to edit verification code
            alertManager.danger("Verification code is wrong. Please, try again!");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
        }
        recoveryForm(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //session destroy
        request.getSession().invalidate();
        AlertManager alertManager = getAlertManager(request);
        alertManager.success("Logout success!");
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



    private void verifyProfileInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        User modifyUser = extractUser(request, user.getRole());

        if(modifyUser.getFirstname() == null) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);
        // Process parameters
        String repeatPassword = request.getParameter("repeatPassword");

        // Add disabled fields
        modifyUser.setId(user.getId());
        modifyUser.setUsername(user.getUsername());
        modifyUser.setRole(user.getRole());
        if(repeatPassword == null){
            // user didn't change password
            modifyUser.setPassword(user.getPassword());
        }

        boolean isFailed = false;
        Validator validator = new Validator();
        validator.validate(modifyUser);

        if(repeatPassword != null) {
            // user changed password
            // validate password
            validator.validatePassword(repeatPassword, "repeat password");

            if(validator.isFailed()) {
                alertManager.danger(validator.getErrors());
                validator.reset();
                isFailed = true;
            }

            if (!repeatPassword.equals(modifyUser.getPassword())) {
                alertManager.danger("Passwords are different.");
                isFailed = true;
            }

        } else if(validator.isFailed()) {
            alertManager.danger(validator.getErrors());
            validator.reset();
            isFailed = true;
        }

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("anonym", modifyUser);

        if (isFailed) {
            showMyProfileForm(request, response);
            return;
        }

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

    private void profileConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        // Scan parameters
        // Current page of test list
        int pageNo = getPageNo(request);

        UserDao userDao = new UserDao();
        // Calc test count
        int count = userDao.getUserCount();

        // Init pagination
        int pageCount = (int) Math.ceil(((double)count) / ((double)COUNT_USER_ON_PAGE));
        Pagination pagination = new Pagination(pageNo, pageCount, PAGINATION_LIMIT);
        pagination.setElementLimit(COUNT_USER_ON_PAGE);

        // Read user list
        int offset = (pagination.getCurrent() - 1) * COUNT_USER_ON_PAGE;
        TutorDao tutorDao = new TutorDao();
        StudentDao studentDao = new StudentDao();

        List<User> users = userDao.getList(offset, COUNT_USER_ON_PAGE);

        for(int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            switch( u.getRole() ) {
                case TUTOR:
                    Tutor tutor = tutorDao.get(u.getId());
                    users.set(i, tutor);
                    break;
                case STUDENT:
                    Student student = studentDao.get(u.getId());
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
        int id;
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        UserDao userDao = new UserDao();
        try {
            userDao.delete(id);
            getAlertManager(request).success("The user is removed successfully");
        } catch(DAOException e) {
            logger.trace(e.getMessage(), e);
            getAlertManager(request).danger("Can not remove user. Try again!");
        }
        response.sendRedirect(request.getContextPath() + "/authorization/users");
    }

    private void changeUserVerificationStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMINISTRATOR) {
            pageNotFound(request, response);
            return;
        }

        String strId = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        String strVerified = request.getParameter("verified");
        boolean verified = ( "true".equals(strVerified) );

        UserDao dao = new UserDao();
        try {
            User u = dao.get(id);
            u.setVerified(verified);
            dao.update(u);
            getAlertManager(request).success("The verification status of user is updated successfully");
        } catch (DAOException e) {
            logger.trace(e.getMessage(), e);
            getAlertManager(request).danger("Can not update verification status of user. Try again!");
        }

        response.sendRedirect(request.getContextPath() + "/authorization/users");
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DAOException {

        String strId = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        UserDao dao = new UserDao();
        User data = dao.get(id);
        if(data == null) {
            pageNotFound(request, response);
            return;
        }

        switch(data.getRole()) {
            case STUDENT:
                data = (new StudentDao()).get(id);
                break;
            case TUTOR:
                data = (new TutorDao()).get(id);
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

    private boolean checkConfirmation(HttpServletRequest request) throws RequestException {
        String code = request.getParameter("verificationCode");
        String expectedCode = (String) request.getSession().getAttribute("verificationCode");

        if (Validator.containNull(code, expectedCode)) {
            throw new RequestException("Request is invalid, con not find verification code");
        }

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

        response.sendRedirect(request.getContextPath() + "/authorization/myprofile");
    }

    private boolean saveUser(User user) throws DAOException {
        switch(user.getRole()) {
            case  STUDENT:
                StudentDao studentDao = new StudentDao();
                return studentDao.update( (Student) user );
            case  TUTOR:
                TutorDao tutorDao = new TutorDao();
                return tutorDao.update( (Tutor) user );
            default:
                UserDao userDao = new UserDao();
                return userDao.update( user );
        }
    }

    private User extractUser(HttpServletRequest request, User.Role role) {
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

    private void extractUser( User out, HttpServletRequest request) {
        // Process parameters
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if( Strings.isNotBlank(username)) {
            username = username.toLowerCase();
        }

        out.setFirstname(firstname);
        out.setLastname(lastname);
        out.setEmail(email);
        out.setUsername(username);
        out.setPassword(password);
        out.setRole(Strings.isBlank(role) ? User.Role.ADMINISTRATOR : User.Role.valueOf(role));
    }

    private String sendVerificationCodeByEmail(String email, String msg)
            throws MessagingException {
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
            alertManager.info("We sent you a verification code. Please check your mailbox.");
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            alertManager.danger("Can't send message. Verify your e-mail address and try again!");
        }
    }
}
