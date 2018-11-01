package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.dao.SubjectDao;
import com.epam.javatraining.knowledgecheck.model.dao.TestDao;
import com.epam.javatraining.knowledgecheck.model.entity.Subject;
import com.epam.javatraining.knowledgecheck.model.entity.Test;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.Pagination;
import com.epam.javatraining.knowledgecheck.service.Presentation;
import com.epam.javatraining.knowledgecheck.service.Validator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/testboard",
        "/testboard/mytests",
        "/testboard/edit",
        "/testboard/add",
        "/testboard/remove"
})
public class TestBoardControllerServlet extends AbstractBaseControllerServlet {

    final int COUNT_TEST_ON_PAGE = 10;
    final int PAGINATION_LIMIT = 5;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String action = request.getServletPath();

        if ( !action.equals("/testboard") &&
             (user == null || user.getRole() != User.Role.TUTOR)) {
            pageNotFound(request, response);
            return;
        }


        try {
            switch (action) {
                case "/testboard":
                    showSimpleListOfTests(request, response);
                    break;
                case "/testboard/mytests":
                    showTutorMyTests(user, request, response);
                    break;
                case "/testboard/edit":
//                    editTest(user, request, response);
                    break;
                case "/testboard/add":
                    addTest(user, request, response);
                    break;
                case "/testboard/delete":
                    deleteTest(user, request, response);
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (IOException | DAOException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void showTests(User user, String view, boolean single,
                           HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // Scan parameters
        // Current page of test list
        String pageNoStr = request.getParameter("pageNo");
        int pageNo;
        try {
            pageNo = Integer.parseInt(pageNoStr);
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        // Read subject list for subject filter in presentation
        SubjectDao subjectDao = new SubjectDao(getConnectionPool());
        List<Subject> subjects = subjectDao.listAll();
        // Init presentation
        Presentation present = new Presentation(request, subjects);

        TestDao testDao = new TestDao(getConnectionPool());
        // If need enable filter
        if(present.getFilterBySubjectId() > 0) {
            testDao.setFilterSubjectId(present.getFilterBySubjectId());
            testDao.enableFilter();
        }

        if(user != null) {
            testDao.setFilterTutorId(user.getId());
            testDao.enableFilter();
        }

        // Enable order by date
        String order = present.getOrderByDate().equals(Presentation.DATE_DESCENDING) ?
                TestDao.ORDER_DESC : TestDao.ORDER_ASC;
        testDao.setDateOrder(order);
        testDao.enableOrder();

        // Calc test count
        int count = testDao.getTestCount();

        // Init pagination
        int pageCount = count / COUNT_TEST_ON_PAGE + 1;
        Pagination pagination = new Pagination(pageNo, pageCount, PAGINATION_LIMIT);

        // Read test list
        int offset = (pageNo - 1) * COUNT_TEST_ON_PAGE;
        List<Test> tests;
        if(single) {
            // without question and answers
            tests = testDao.getListSingle(offset, COUNT_TEST_ON_PAGE);
        } else {
            // with question and answers
            tests = testDao.getList(offset, COUNT_TEST_ON_PAGE);
        }

        // Store data in request and session
        present.store();
        request.setAttribute("tests", tests);
        request.setAttribute("pagination", pagination);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

    private void showSimpleListOfTests(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // Show all tests without question data and answer data
        showTests(null, VIEW_TEST_BOARD, true, request, response);
    }

    private void showTutorMyTests(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // Show all current user's tests with question data and answer data
        showTests(user, VIEW_TEST_BOARD_MY_TESTS, false, request, response);
    }

    private void deleteTest(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

    }

    private void addTest(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

        String subject = request.getParameter("subject");
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        if (Validator.containNull(subject, title, description)) {
            // Read subject list for subject filter in presentation
            SubjectDao subjectDao = new SubjectDao(getConnectionPool());
            List<Subject> subjects = subjectDao.listAll();
            request.setAttribute("subjects", subjects);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }



        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_EDIT_TEST);
        dispatcher.forward(request, response);
    }
}