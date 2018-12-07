package edu.javatraining.knowledgecheck.controller;

import edu.javatraining.knowledgecheck.data.SqlScriptRunner;
import edu.javatraining.knowledgecheck.service.tools.AlertManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;


public class AbstractBaseControllerServlet extends HttpServlet {

    protected final String VIEW_LOGIN_FORM = "/WEB-INF/view/account/LoginForm.jsp";
    protected final String VIEW_REGISTER_FORM = "/WEB-INF/view/account/RegisterForm.jsp";
    protected final String VIEW_PASSWORD_RECOVERY_FORM = "/WEB-INF/view/account/PasswordRecoveryForm.jsp";
    protected final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/common/PageNotFound.jsp";
    protected final String VIEW_TEST_BOARD = "/WEB-INF/view/testing/TestList.jsp";
    protected final String VIEW_MY_TESTS = "/WEB-INF/view/testing/MyTests.jsp";
    protected final String VIEW_EDIT_TEST = "/WEB-INF/view/testing/EditTest.jsp";
    protected final String VIEW_TESTING = "/WEB-INF/view/testing/Testing.jsp";
    protected final String VIEW_STUDENTS_RESULTS = "/WEB-INF/view/testing/StudentsResults.jsp";
    protected final String VIEW_TEST_STATISTICS = "/WEB-INF/view/testing/TestStatistics.jsp";
    protected final String VIEW_MY_PROFILE_FORM = "/WEB-INF/view/account/MyProfileForm.jsp";
    protected final String VIEW_USER_LIST = "/WEB-INF/view/account/UserList.jsp";
    protected final String VIEW_SUBJECTS_FORM = "/WEB-INF/view/account/SubjectsForm.jsp";
    protected final String VIEW_PROFILE = "/WEB-INF/view/account/Profile.jsp";

    protected static final Logger logger = LogManager.getLogger("controller");


    @Override
    public void init() throws ServletException {
        try {
            // TODO prepare db
           // prepareDataBase();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void prepareDataBase() throws ServletException {
        try {
            URL resource = getClass().getResource("/init.sql");
            File file = new File(resource.getFile());
            Reader reader = new FileReader( file );

            SqlScriptRunner runner = new SqlScriptRunner(null);
            runner.runScript(reader);
        } catch(SQLException | IOException e) {
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

    protected void forward(HttpServletRequest request, HttpServletResponse response, String view)
            throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException, ServletException {

        response.sendRedirect(request.getContextPath() + url);
    }

    protected void pageNotFound(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        forward(request, response, VIEW_PAGE_NOT_FOUND);
    }
}
