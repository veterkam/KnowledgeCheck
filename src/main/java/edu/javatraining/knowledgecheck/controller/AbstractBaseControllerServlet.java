package edu.javatraining.knowledgecheck.controller;

import edu.javatraining.knowledgecheck.tools.AlertManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;


public class AbstractBaseControllerServlet extends HttpServlet {

    protected final String VIEW_LOGIN_FORM = "/WEB-INF/view/account/LoginForm.jsp";
    protected final String VIEW_REGISTRATION_FORM = "/WEB-INF/view/account/RegistrationForm.jsp";
    protected final String VIEW_PASSWORD_RECOVERY_FORM = "/WEB-INF/view/account/PasswordRecoveryForm.jsp";
    protected final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/common/PageNotFound.jsp";
    protected final String VIEW_TEST_BOARD = "/WEB-INF/view/testing/TestList.jsp";
    protected final String VIEW_MY_TESTS = "/WEB-INF/view/testing/MyTests.jsp";
    protected final String VIEW_EDIT_TEST = "/WEB-INF/view/testing/EditTest.jsp";
    protected final String VIEW_TEST = "/WEB-INF/view/testing/Test.jsp";
    protected final String VIEW_STUDENTS_RESULTS = "/WEB-INF/view/testing/StudentsResults.jsp";
    protected final String VIEW_TEST_STATISTICS = "/WEB-INF/view/testing/TestStatistics.jsp";
    protected final String VIEW_MY_PROFILE_FORM = "/WEB-INF/view/account/MyProfileForm.jsp";
    protected final String VIEW_USER_LIST = "/WEB-INF/view/account/UserList.jsp";
    protected final String VIEW_SUBJECTS_FORM = "/WEB-INF/view/testing/SubjectsForm.jsp";
    protected final String VIEW_PROFILE = "/WEB-INF/view/account/Profile.jsp";

    protected static final Logger logger = LogManager.getLogger("controller");

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
            throws IOException {

        response.sendRedirect(request.getContextPath() + url);
    }

    protected void pageNotFound(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        forward(request, response, VIEW_PAGE_NOT_FOUND);
    }

    protected void genFormId(HttpSession session) {
        session.setAttribute("FID", "" + Math.random());
    }

    protected boolean checkFormId(HttpServletRequest request) {
        String originalFormId = (String) request.getSession().getAttribute("FID");
        String formId = request.getParameter("_FID");
        request.getSession().removeAttribute("FID");

        return originalFormId.equals(formId);
    }
}
