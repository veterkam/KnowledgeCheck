package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.datalayer.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epam.javatraining.knowledgecheck.datalayer.model.User;
import com.epam.javatraining.knowledgecheck.datalayer.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.service.mail.MailSender;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This servlet acts as a page controller for the application,
 * handling all requests from the user.
 */
public class ControllerServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ControllerServlet.class.getName());

    private ConnectionPool connectionPool;


    @Override
    public void init() throws ServletException {
        String url = getServletContext().getInitParameter("url");
        String username = getServletContext().getInitParameter("username");
        String password = getServletContext().getInitParameter("password");

        try {
            this.connectionPool = new ConnectionPool(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        logger.trace("Begin destroy");
        logger.trace("Connection pool size: " + this.connectionPool.getSize());
        try {
            this.connectionPool.release();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        logger.trace("Connection pool size: " + this.connectionPool.getSize());
        logger.trace("End destroy");
    }

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
                case "/register":
                    register(request, response);
                    break;
                case "/logout":
                    logout(request, response);
                    break;
                case "/sendmail":
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
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/LoginForm.jsp");
            dispatcher.forward(request, response);
            return;
        }

        UserDao UserDao = new UserDao(connectionPool);
        User user = UserDao.get(username);

        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/Welcome.jsp");
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("loginFailed", "true");
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/LoginForm.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (firstname == null || lastname == null || username == null || password == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/RegisterForm.jsp");
            dispatcher.forward(request, response);
            return;
        }

        UserDao UserDao = new UserDao(connectionPool);
        // Is the username unique?
        User user = UserDao.get(username);
        String errorMsg;

        if (user == null) {
            // The username is unique.
            user = new User(firstname, lastname, "test@google.com", User.Role.ADMINISTRATOR, username, password);
            // Insert user to data base.
            boolean isInserted = UserDao.insert(user);
            if (isInserted) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/Welcome.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                errorMsg = "Registration failed. Please, try again!";
            }
        } else {
            errorMsg = "Registration failed. A user with this username already exists. Please, try again!";
        }

        request.setAttribute("registerFailedMsg", errorMsg);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/RegisterForm.jsp");
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
