package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.dao.SubjectDao;
import com.epam.javatraining.knowledgecheck.model.dao.TestDao;
import com.epam.javatraining.knowledgecheck.model.entity.*;
import com.epam.javatraining.knowledgecheck.service.AlertManager;
import com.epam.javatraining.knowledgecheck.service.Pagination;
import com.epam.javatraining.knowledgecheck.service.Presentation;
import com.epam.javatraining.knowledgecheck.service.Validator;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
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

        String paramSubject = request.getParameter("subject");
        String paramTitle = request.getParameter("title");
        String paramDescription = request.getParameter("description");

        SubjectDao subjectDao = new SubjectDao(getConnectionPool());

        if (Validator.containNull(paramSubject, paramTitle, paramDescription)) {
            // Read subject list for subject filter in presentation
            request.setAttribute("subjects", subjectDao.listAll() );
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }

        AlertManager alerter = new AlertManager();
        Subject subject = null;


        if(Strings.isBlank(paramSubject)) {
            alerter.danger("Subject is wrong.");
        } else {
            try {
                int subjectId = Integer.parseInt(paramSubject);
                subject = subjectDao.get(subjectId);
            } catch (NumberFormatException e) {
                alerter.danger("Subject is wrong.");
            }
        }

        Validator validator = new Validator(alerter);
        validator.validateTestTitle(paramTitle);
        validator.validateTestDescritption(paramDescription);

        Test test = new Test();
        test.setSubject(subject);
        test.setTitle(paramTitle);
        test.setDescription(paramDescription);

        String[] paramQuestions = request.getParameterValues("questions");
        List<Question> questionList = new ArrayList<>();

        for(int i = 0; i < paramQuestions.length; i++) {
            validator.validateQuestionDescritption(paramQuestions[i]);
            Question question = new Question();
            question.setDescription(paramQuestions[i]);
            List<Answer> answerList = new ArrayList<>();

            String[] paramAnswers = request.getParameterValues("answers["+i+"]");
            for(int j = 0; j < paramAnswers.length; j++) {
                validator.validateAnswerDescritption(paramAnswers[j]);
                Answer answer = new Answer();
                answer.setDescription(paramAnswers[j]);

                String paramCorrect = request.getParameter("corrects["+i+"]["+j+"]");
                boolean correct = (paramCorrect != null);
                answer.setCorrect(correct);
                answerList.add(answer);
            }

            question.setAnswers(answerList);
            questionList.add(question);
        }
        test.setQuestions(questionList);


        if (true || !alerter.isEmpty()) {
            // We have errors
            // Read subject list for subject filter in presentation
            request.setAttribute("subjects",  subjectDao.listAll() );
            request.setAttribute("test", test);
            request.setAttribute("alerts", alerter.getAlerts());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }

        test.setTutor(new Tutor(user));

        alerter.success("Test was saved successfully.");
        response.sendRedirect("/testboard/mytests");
    }
}