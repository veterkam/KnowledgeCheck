package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.dao.UserDao;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.mail.EmailSender;
import com.epam.javatraining.knowledgecheck.service.mail.Validator;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = {
        "/authorization/login",
        "/authorization/logout",
        "/authorization/register"
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
                    // TODO верификация
                    register(request, response);
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
            Integer rnd = (int)(Math.random() * 1000000 + 999999);
            String[] code = new String[] {rnd.toString()};

            try {
                sendEmailVerificationCode(email, code[0]);
            } catch(MessagingException e) {
                errorMsg = "Can't send message. Verify your e-mail and try again!";
                forwardToRegisterForm(request, response, errorMsg);
            }

            // Store code in parameters
            Map<String, String[]> parameters = request.getParameterMap();
            parameters.put("code", code);
            // Store parameters in session
            request.getSession().setAttribute("userInfo", parameters);
            // Put flag of e-mail verification into attributes
            request.setAttribute("verifyEmail", "true");
        } else {
            errorMsg = "Registration failed. A user with this username already exists. Please, try again!";
        }

        forwardToRegisterForm(request, response, errorMsg);
    }

    private void verifyCodeAndRegister(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("verificationCode");
        if(code == null) {

        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        // Are there parameters?
        String action = request.getParameter("action");
        action = (action == null) ? "" : action;

        switch("action") {
            case "Next":
                verifyUserInfo(request, response);
                break;
            case "Back":
                break;
            case "Cancel":
                forwardToRegisterForm(request, response);
                break;
            case "Register":
                verifyCodeAndRegister(request, response);
                break;
            default:
                forwardToRegisterForm(request, response);
                break;
        }





//            user = new User(firstname, lastname, email, User.Role.valueOf(role), username, password);
//            // Insert user to data base.
//            boolean isInserted = UserDao.insert(user);
//            if (isInserted) {
//                HttpSession session = request.getSession();
//                session.setAttribute("user", user);
//                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_WELCOME);
//                dispatcher.forward(request, response);
//                return;
//            } else {
//                errorMsg = "Registration failed. Please, try again!";
//            }

    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //session destroy
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }


}
