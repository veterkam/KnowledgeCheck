package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPoolManager;
import com.epam.javatraining.knowledgecheck.service.AlertManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


public class AbstractBaseControllerServlet extends HttpServlet {

    protected final String VIEW_LOGIN_FORM = "/WEB-INF/view/authorization/LoginForm.jsp";
    protected final String VIEW_REGISTER_FORM = "/WEB-INF/view/authorization/RegisterForm.jsp";
    protected final String VIEW_PASSWORD_RECOVERY_FORM = "/WEB-INF/view/authorization/PasswordRecoveryForm.jsp";
    protected final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/common/PageNotFound.jsp";
    protected final String VIEW_TEST_BOARD = "/WEB-INF/view/testboard/TestBoard.jsp";
    protected final String VIEW_TEST_BOARD_MY_TESTS = "/WEB-INF/view/testboard/MyTests.jsp";
    protected final String VIEW_TEST_BOARD_EDIT_TEST = "/WEB-INF/view/testboard/EditTest.jsp";
    protected final String VIEW_TEST_BOARD_TESTING = "/WEB-INF/view/testboard/Testing.jsp";
    protected final String VIEW_TEST_BOARD_STUDENTS_RESULTS = "/WEB-INF/view/testboard/StudentsResults.jsp";
    protected final String VIEW_TEST_BOARD_TEST_STATISTICS = "/WEB-INF/view/testboard/TestStatistics.jsp";
    protected final String VIEW_MY_PROFILE_FORM = "/WEB-INF/view/authorization/MyProfileForm.jsp";
    protected final String VIEW_USER_LIST = "/WEB-INF/view/authorization/UserList.jsp";

    protected static final Logger logger = LogManager.getLogger("controller");

    private void initConnectionPoolManager() throws ServletException {
        // Init connectionPoll
        String url = getServletContext().getInitParameter("dbUrl");
        String username = getServletContext().getInitParameter("dbUsername");
        String password = getServletContext().getInitParameter("dbPassword");

        if (url == null || username == null || password == null) {
            throw new ServletException("Unable to find init parameters");
        }

        try {
            ConnectionPoolManager.init(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
        }
    }

    protected AlertManager getAlertManager(HttpServletRequest request) {
        HttpSession session = request.getSession();
        AlertManager manager = (AlertManager)session.getAttribute("alertManager");
        if( manager == null ) {
            manager = new AlertManager();
            session.setAttribute("alertManager", manager);
        }

        return manager;
    }

    @Override
    public void init() throws ServletException {
        initConnectionPoolManager();
    }

    protected void pageNotFound(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_PAGE_NOT_FOUND);
        dispatcher.forward(request, response);
    }
}
