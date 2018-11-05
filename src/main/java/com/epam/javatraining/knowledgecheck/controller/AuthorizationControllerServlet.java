package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.dao.UserDao;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.AlertManager;
import com.epam.javatraining.knowledgecheck.service.EmailSender;
import com.epam.javatraining.knowledgecheck.service.Validator;
import com.mysql.cj.exceptions.MysqlErrorNumbers;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {
        "/authorization/login",
        "/authorization/logout",
        "/authorization/register",
        "/authorization/register/back",
        "/authorization/register/confirm",
        "/authorization/register/verify",
        "/authorization/recovery",
        "/authorization/recovery/back",
        "/authorization/recovery/confirm",
        "/authorization/recovery/verify"
})
public class AuthorizationControllerServlet extends AbstractBaseControllerServlet {

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
                    forwardToRegisterForm(request, response);
                    break;
                case "/authorization/register/back":
                    forwardToRegisterForm(request, response);
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
                    forwardToRecoveryForm(request, response);
                    break;
                case "/authorization/recovery/back":
                    forwardToRecoveryForm(request, response);
                    break;
                case "/authorization/recovery/verify":
                    // User click button Next, verify the fields
                    recoveryVerifyUserInfo(request, response);
                    break;
                case "/authorization/recovery/confirm":
                    recoveryConfirmEmail(request, response);
                    break;
                case "/authorization/login":
                    login(request, response);
                    break;
                case "/authorization/logout":
                    logout(request, response);
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

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        AlertManager alertManager = getAlertManagerFromSession(request.getSession());
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_LOGIN_FORM);
            dispatcher.forward(request, response);
            return;
        }

        UserDao UserDao = new UserDao(getConnectionPool());
        User user = UserDao.get(username);

        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            alertManager.success("Registration success! Welcome " + user.getFullname());
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            alertManager.danger("Username or password is wrong. Please, try again!");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_LOGIN_FORM);
            dispatcher.forward(request, response);
        }
    }

    private void forwardToRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_REGISTER_FORM);
        dispatcher.forward(request, response);
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

    private void registerVerifyUserInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException{

        // Process parameters
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");
        String role = request.getParameter("role");

        if (Validator.containNull(firstname, lastname,
                email, username, password, repeatPassword, role)) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManagerFromSession(request.getSession());

        boolean isFailed = false;
        Validator validator = new Validator();
        validator.validateFirstName(firstname);
        validator.validateLastName(lastname);
        validator.validateEmail(email);
        validator.validateUsername(username);
        validator.validatePassword(password);
        validator.validatePassword(repeatPassword, "repeat password");
        validator.isNotBlank(role, "role");

        if(validator.isFailed()) {
            alertManager.danger(validator.getErrors());
            isFailed = true;
        }


        if (!password.equals(repeatPassword)) {
            alertManager.danger("Passwords are different.");
            isFailed = true;
        }

        User anonym = new User();
        anonym.setFirstname(firstname);
        anonym.setLastname(lastname);
        anonym.setEmail(email);
        anonym.setUsername(username.toLowerCase());
        anonym.setPassword(password);
        anonym.setRole((role != "") ? User.Role.valueOf(role) : User.Role.ADMINISTRATOR);

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("anonym", anonym);

        if (isFailed) {
            forwardToRegisterForm(request, response);
            return;
        }

        UserDao userDao = new UserDao(getConnectionPool());
        // Is the username unique?
        User user = userDao.get(username);

        if (user == null) {
            // The username is unique.
            try {
                String msg = "Hello. Thanks for registration in KnowledgeCheck!";
                String code = sendVerificationCodeByEmail(email, msg);
                session.setAttribute("verificationCode", code);
                // Put flag of e-mail verification into attributes
                request.setAttribute("verifyEmail", "true");
                alertManager.info("We sent you a verification code. Please check your mailbox.");
            } catch (MessagingException e) {
                logger.error(e.getMessage(), e);
                alertManager.danger("Can't send message. Verify your e-mail address and try again!");
            }

        } else {
            // The username is not unique.
            alertManager.danger("Registration failed. A user with this username already exists. Please, try again!");
        }

        forwardToRegisterForm(request, response);
    }

    private void registerConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("verificationCode");
        HttpSession session = request.getSession();
        String expectedCode = (String) session.getAttribute("verificationCode");
        User user = (User) session.getAttribute("anonym");

        if (Validator.containNull(code, expectedCode, user)) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManagerFromSession(request.getSession());

        if (expectedCode.equals(code)) {

            // Insert user to data base.
            String errorMsg = "";
            try {
                new UserDao(getConnectionPool()).insert(user);
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
                forwardToRegisterForm(request, response);
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
            forwardToRegisterForm(request, response);
        }
    }


    private void forwardToRecoveryForm(HttpServletRequest request, HttpServletResponse response)
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

        AlertManager alertManager = getAlertManagerFromSession(request.getSession());

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
            forwardToRecoveryForm(request, response);
            return;
        }

        UserDao userDao = new UserDao(getConnectionPool());
        User user = userDao.get(anonym.getUsername());

        if (user == null || !user.getEmail().equals(email)) {
            String errorMsg = "Can't find username " + anonym.getUsername() +
                    " with e-mail " + email + ". Please, try again!";
            alertManager.danger(errorMsg);
        } else {
            try {
                String msg = "Hello. You started password recovering. ";
                String code = sendVerificationCodeByEmail(email, msg);
                session.setAttribute("verificationCode", code);
                // Put flag of e-mail verification into attributes
                request.setAttribute("verifyEmail", "true");
                alertManager.success("We sent you a verification code. Please check your mailbox.");
            } catch (MessagingException e) {
                logger.error(e.getMessage(), e);
                alertManager.danger("Can't send message. Verify your e-mail address and try again!");
            }
        }

        forwardToRecoveryForm(request, response);
    }

    private void recoveryConfirmEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, DAOException, IOException {
        String code = request.getParameter("verificationCode");
        HttpSession session = request.getSession();
        String expectedCode = (String) session.getAttribute("verificationCode");
        User anonym = (User) session.getAttribute("anonym");

        if (Validator.containNull(code, expectedCode, anonym)) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManagerFromSession(request.getSession());

        if (expectedCode.equals(code)) {
            UserDao userDao = new UserDao(getConnectionPool());
            User user = userDao.get(anonym.getUsername());

            if (user != null) {
                user.setPassword(anonym.getPassword());
                userDao.update(user);
                session.setAttribute("user", user);
                session.setAttribute("anonym", null);
                alertManager.success("Recovery password success! Welcome " + user.getFullname());
                response.sendRedirect(request.getContextPath() + "/");
                return;
            } else {
                alertManager.danger("Sorry, can't find " + anonym.getUsername() + ". Please, try again!");
            }

        } else {
            // Verification code is wrong. Come back to edit verification code
            alertManager.danger("Verification code is wrong. Please, try again!");
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
        }
        forwardToRecoveryForm(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //session destroy
        request.getSession().invalidate();
        AlertManager alertManager = getAlertManagerFromSession(request.getSession());
        alertManager.success("Logout success!");
        response.sendRedirect(request.getContextPath() + "/");
    }
}
