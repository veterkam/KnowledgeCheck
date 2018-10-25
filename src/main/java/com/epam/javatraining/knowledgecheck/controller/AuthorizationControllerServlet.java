package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.dao.UserDao;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.mail.EmailSender;
import com.epam.javatraining.knowledgecheck.service.mail.Validator;
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
        "/authorization/register/verify"
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

        request.setCharacterEncoding("UTF-8");

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
                    verifyUserInfo(request, response);
                    break;
                case "/authorization/register/confirm":
                    confirmEmailAndRegister(request, response);
                    break;
                case "/authorization/restore":
                    login(request, response);
                    break;
                case "/authorization/login":
                    login(request, response);
                    break;
                case "/authorization/logout":
                    logout(request, response);
                    break;
                default:
                    login(request, response);
                    break;
            }
        } catch (IOException | ServletException | SQLException e ) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
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
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_WELCOME);
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("ErrorMessage",
                    "Username or password is wrong. Please, try again!");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_LOGIN_FORM);
            dispatcher.forward(request, response);
        }
    }

    private void forwardToRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_REGISTER_FORM);
        dispatcher.forward(request, response);
    }

    private void forwardToRegisterForm(HttpServletRequest request, HttpServletResponse response, String error)
        throws ServletException, IOException
    {
        if(error.length() > 0) {
            request.setAttribute("ErrorMessage", error);
        }

        forwardToRegisterForm(request, response);
    }

    private void sendEmailVerificationCode(String email, String code)
            throws MessagingException {
        String username = getServletContext().getInitParameter("emailUsername");
        String password = getServletContext().getInitParameter("emailPassword");

        EmailSender sender = new EmailSender(username, password);
        String subject = "KnowledgeCheck Verification Code";
        String msg = "Hello. Thanks for registration in KnowledgeCheck!\n" +
                "Your verification code is " + code;
        sender.send(subject, msg,"KnowledgeCheck@knowledgecheck.com", email);
    }

    private void verifyUserInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        // Process parameters
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");
        String role = request.getParameter("role");

        // Are there empty parameters?
        String errorMsg = "";
        errorMsg += (firstname.isEmpty()) ? "firstname, " : "";
        errorMsg += (lastname.isEmpty()) ? "lastname, " : "";
        errorMsg += (email.isEmpty()) ? "email, " : "";
        errorMsg += (username.isEmpty()) ? "username, " : "";
        errorMsg += (password.isEmpty()) ? "password, " : "";
        errorMsg += (repeatPassword.isEmpty()) ? "repeatPassword, " : "";
        errorMsg += (role.isEmpty()) ? "role, " : "";

        if(!errorMsg.isEmpty()) {
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
            errorMsg = "Some of the fields are empty: " + errorMsg + ".<br>";
        }

        if(email.length() > 0 && !Validator.validateEmail(email)) {
            errorMsg += "E-mail is invalid.<br>";
        }

        if(!password.equals(repeatPassword)) {
            errorMsg += "Passwords are different.<br>";
        }

        User anonym = new User();
        anonym.setFirstname(firstname);
        anonym.setLastname(lastname);
        anonym.setEmail(email);
        anonym.setUsername(username.toLowerCase());
        anonym.setPassword(password);
        anonym.setRole( (role != "") ? User.Role.valueOf(role) : User.Role.ADMINISTRATOR );

        // Store user info in session for form autofilling
        HttpSession session = request.getSession();
        session.setAttribute("anonym", anonym);

        if(!errorMsg.isEmpty()) {
            forwardToRegisterForm(request, response, errorMsg);
            return;
        }

        UserDao UserDao = new UserDao(getConnectionPool());
        // Is the username unique?
        User user = UserDao.get(username);
        errorMsg = "";

        if (user == null) {
            // The username is unique.

            // Generate e-mail verification code
            Integer rnd = (int)(Math.random() * 1000000 + 1000000);
            String code = rnd.toString();

            try {
                sendEmailVerificationCode(email, code);
            } catch(MessagingException e) {
                logger.error(e.getMessage(), e);
                errorMsg = "Can't send message. Verify your e-mail and try again!";
                forwardToRegisterForm(request, response, errorMsg);
            }

            session.setAttribute("verificationCode", code);
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
        } else {
            // The username is not unique.
            errorMsg = "Registration failed. A user with this username already exists. Please, try again!";
        }

        forwardToRegisterForm(request, response, errorMsg);
    }

    private void confirmEmailAndRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, SQLException, IOException {
        String code = request.getParameter("verificationCode");
        HttpSession session = request.getSession();
        String expectedCode = (String) session.getAttribute("verificationCode");
        User user = (User) session.getAttribute("anonym");
        if(expectedCode.equals(code)) {

            // Insert user to data base.
            boolean isInserted = false;
            String errorMsg = "";
            try {
                isInserted = new UserDao(getConnectionPool()).insert(user);
            } catch (SQLException e) {

                if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                    errorMsg += "Sorry, it's too late, username is already busy.\n";
                } else {
                    logger.error(e.getMessage(), e);
                }
            }

            if (isInserted) {
                // Success!
                // Store user data in session
                session.setAttribute("anonym", null);
                session.setAttribute("user", user);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_WELCOME);
                dispatcher.forward(request, response);
                return;
            } else {
                // Failed! Come back to edit user info
                errorMsg += "Registration failed. Please, try again!";
                forwardToRegisterForm(request, response, errorMsg);
                return;
            }
        } else {
            // Verification code is wrong. Come back to edit verification code
            String errorMsg = "Verification code is wrong. Please, try again!";
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
            forwardToRegisterForm(request, response, errorMsg);
            return;
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //session destroy
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }
}
