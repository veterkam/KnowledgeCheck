package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.SQLException;


public class AbstractBaseControllerServlet extends HttpServlet {

    protected final String VIEW_LOGIN_FORM = "/WEB-INF/view/LoginForm.jsp";
    protected final String VIEW_WELCOME = "/WEB-INF/view/Welcome.jsp";
    protected final String VIEW_REGISTER_FORM = "/WEB-INF/view/RegisterForm.jsp";

    protected static final Logger logger = LogManager.getLogger("controller");
    private static ConnectionPool connectionPool = null;

    private void initConnectionPool() throws ServletException {
        // Init connectionPoll
        String url = getServletContext().getInitParameter("url");
        String username = getServletContext().getInitParameter("username");
        String password = getServletContext().getInitParameter("password");

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
}
