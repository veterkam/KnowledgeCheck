package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class AbstractBaseControllerServlet extends HttpServlet {

    protected final String VIEW_LOGIN_FORM = "/WEB-INF/view/LoginForm.jsp";
    protected final String VIEW_WELCOME = "/WEB-INF/view/Welcome.jsp";
    protected final String VIEW_REGISTER_FORM = "/WEB-INF/view/RegisterForm.jsp";
    protected final String VIEW_PASSWORD_RECOVERY_FORM = "/WEB-INF/view/PasswordRecoveryForm.jsp";
    protected final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/PageNotFound.jsp";
    protected final String VIEW_HOME = "/WEB-INF/view/Home.jsp";
    protected final String VIEW_TEST_BOARD = "/WEB-INF/view/TestBoard.jsp";
    protected final String VIEW_TEST_BOARD_MY_TESTS = "/WEB-INF/view/MyTests.jsp";

    protected static final Logger logger = LogManager.getLogger("controller");
    private static ConnectionPool connectionPool = null;

    private void initConnectionPool() throws ServletException {
        // Init connectionPoll
        String url = getServletContext().getInitParameter("dbUrl");
        String username = getServletContext().getInitParameter("dbUsername");
        String password = getServletContext().getInitParameter("dbPassword");

        if (url == null || username == null || password == null) {
            throw new ServletException("Unable to find init parameters");
        }

        try {
            connectionPool = new ConnectionPool(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
        }
    }

    protected ConnectionPool getConnectionPool() throws ServletException {
        if(connectionPool == null) {
            initConnectionPool();
        }
        return connectionPool;
    }

    @Override
    public void init() throws ServletException {
        if(connectionPool == null) {
            initConnectionPool();
        }
    }

    protected void pageNotFound(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_PAGE_NOT_FOUND);
        dispatcher.forward(request, response);
    }
}
