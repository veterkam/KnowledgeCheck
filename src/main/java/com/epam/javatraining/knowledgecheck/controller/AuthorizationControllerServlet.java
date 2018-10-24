package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.dao.UserDao;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.mail.MailSender;

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
        "/authorization/sendmail"
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
                case "/authorization/sendmail":
                    sendMail(request, response);
                    break;
                default:
                    login(request, response);
                    break;
            }
        } catch (IOException | ServletException | SQLException e ) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
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
            request.setAttribute("loginFailed", "true");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_LOGIN_FORM);
            dispatcher.forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");



        // Add list of User.Roles to request
        request.setAttribute("roles", User.Role.values());

        if (firstname == null || lastname == null || email == null || username == null || password == null || role == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_REGISTER_FORM);
            dispatcher.forward(request, response);
            return;
        }

        UserDao UserDao = new UserDao(getConnectionPool());
        // Is the username unique?
        User user = UserDao.get(username);
        String errorMsg;

        if (user == null) {
            // The username is unique.
            user = new User(firstname, lastname, email, User.Role.valueOf(role), username, password);
            // Insert user to data base.
            boolean isInserted = UserDao.insert(user);
            if (isInserted) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_WELCOME);
                dispatcher.forward(request, response);
                return;
            } else {
                errorMsg = "Registration failed. Please, try again!";
            }
        } else {
            errorMsg = "Registration failed. A user with this username already exists. Please, try again!";
        }

        request.setAttribute("registerFailedMsg", errorMsg);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_REGISTER_FORM);
        dispatcher.forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //session destroy
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }

    private void sendMail(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        MailSender sender = new MailSender("grodnojavatraining@gmail.com", "J@v@15648977");
        try {
            sender.send("Test letter", "Hello. It is test message!",
                    "test@gmail.com", "veterkam@mail.ru, ");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}
