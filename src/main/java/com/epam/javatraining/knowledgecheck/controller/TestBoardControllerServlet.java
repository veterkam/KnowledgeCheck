package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.dao.*;
import com.epam.javatraining.knowledgecheck.model.entity.*;
import com.epam.javatraining.knowledgecheck.service.AlertManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {
        "/testboard",
        "/testboard/mytests",
        "/testboard/edit",
        "/testboard/remove",
        "/testboard/testing",
        "/testboard/testing/result"
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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String action = request.getServletPath();

        if ( !action.equals("/testboard") && user == null) {
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
                    editTest(user, request, response);
                    break;
                case "/testboard/remove":
                    removeTest(user, request, response);
                    break;
                case "/testboard/testing":
                    if ( user.getRole() != User.Role.STUDENT) {
                        pageNotFound(request, response);
                    } else {
                        runTest(request, response);
                    }
                    break;
                case "/testboard/testing/result":
                    processTestResult(user, request, response);
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
        if ( user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
        } else {
            // Show all current user's tests with question data and answer data
            showTests(user, VIEW_TEST_BOARD_MY_TESTS, false, request, response);
        }
    }

    private void removeTest(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

        if ( user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
        }

        String testId = request.getParameter("testId");
        long id;
        try {
            id = Long.parseLong(testId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManagerFromSession(request.getSession());
        Test test = new Test();
        test.setId(id);
        test.setTutor(new Tutor(user));
        TestDao testDao = new TestDao(getConnectionPool());
        if(testDao.delete(test)) {
            alertManager.success("The test was successfully removed.");
        } else {
            alertManager.danger("The test was not removed. Maybe you don't have permission to removed it.");
        }

        response.sendRedirect(request.getContextPath() + "/testboard/mytests");

    }

    private void editTest(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

        if ( user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManagerFromSession(request.getSession());
        Test test = new Test();
        test.setTutor(new Tutor(user));

        String paramSubject = request.getParameter("subject");
        String paramTitle = request.getParameter("title");
        String paramDescription = request.getParameter("description");
        String paramTestId = request.getParameter("testId");
        try {
            long testId = Long.parseLong(paramTestId);
            test.setId(testId);
        } catch (NumberFormatException e) {
            // it is new test, do nothing
        }

        TestDao testDao = new TestDao(getConnectionPool());
        SubjectDao subjectDao = new SubjectDao(getConnectionPool());

        if (Validator.containNull(paramSubject, paramTitle, paramDescription)) {
            // Parameters do not have test data
            if(test.getId() > -1) {
                test = testDao.get(test.getId());
                if(test.getTutorId() == user.getId()) {
                    request.setAttribute("test", test );
                } else {
                    alertManager.danger("You don't have permission to change this test.");
                    response.sendRedirect(request.getContextPath() + "/testboard/mytests");
                    return;
                }
            }
            // only show empty form
            // Read subject list for subject filter in presentation
            request.setAttribute("subjects", subjectDao.listAll() );
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }

        Subject subject = null;

        try {
            int subjectId = Integer.parseInt(paramSubject);
            subject = subjectDao.get(subjectId);
        } catch (NumberFormatException e) {
            alertManager.danger("Subject is wrong.");
        }

        Validator validator = new Validator();
        validator.validateTestTitle(paramTitle);
        validator.validateTestDescritption(paramDescription);

        test.setSubject(subject);
        test.setTitle(paramTitle);
        test.setDescription(paramDescription);

        // Fill test object with the data from request parameters
        String[] paramQuestionDescs = request.getParameterValues("questions");
        String[] paramQuestionIds = request.getParameterValues("questionIds");
        String[] paramQuestionRemFlags = request.getParameterValues("questionRemovingFlags");


        List<Question> questionList = new ArrayList<>();
        List<Question> remQuestionList = new ArrayList<>();
        List<Answer> remAnswerList = new ArrayList<>();

        for(int i = 0; i < paramQuestionDescs.length; i++) {
            Question question = new Question();
            question.setTestId(test.getId());

            try {
                long id = Long.parseLong(paramQuestionIds[i]);
                question.setId(id);

                try {
                    int isRemoved = Integer.parseInt(paramQuestionRemFlags[i]);
                    if( isRemoved == 1 ) {
                        // Add question to list for removing
                        remQuestionList.add(question);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    // it's not removed, do nothing
                }
            } catch (NumberFormatException e) {
                // it's new question, do nothing
            }

            // question object with the data from request parameters
            validator.validateQuestionDescritption(paramQuestionDescs[i]);
            question.setDescription(paramQuestionDescs[i]);
            List<Answer> answerList = new ArrayList<>();

            String[] paramAnswers = request.getParameterValues("answers["+i+"]");
            String[] paramAnswerIds = request.getParameterValues("answerIds["+i+"]");
            String[] paramAnswerRemFlags = request.getParameterValues("answerRemovingFlags["+i+"]");
            for(int j = 0; j < paramAnswers.length; j++) {
                Answer answer = new Answer();
                answer.setQuestionId(question.getId());
                try {
                    long id = Long.parseLong(paramAnswerIds[j]);
                    answer.setId(id);
                    try {
                        int isRemoved = Integer.parseInt(paramAnswerRemFlags[j]);
                        if(isRemoved == 1) {
                            // Add answer to list for removing
                            remAnswerList.add(answer);
                        }
                    } catch (NumberFormatException e) {
                        // it's not removed, do nothing
                    }
                } catch (NumberFormatException e) {
                    // it's new answer, do nothing
                }

                validator.validateAnswerDescritption(paramAnswers[j]);

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

        if(!remAnswerList.isEmpty()) {
            AnswerDao dao = new AnswerDao(getConnectionPool());
            for(Answer answer : remAnswerList) {
                dao.delete(answer);
            }
        }

        if(!remQuestionList.isEmpty()) {
            QuestionDao dao = new QuestionDao(getConnectionPool());
            for(Question question : remQuestionList) {
                dao.delete(question);
            }
        }

        if (validator.isFailed()) {
            // We have errors
            alertManager.danger(validator.getErrors());
            // Read subject list for subject filter in presentation
            request.setAttribute("subjects",  subjectDao.listAll() );
            // Send test data for auto filing
            request.setAttribute("test", test);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }

        if( !testDao.update(test) ) {
            testDao.insert(test);
        }

        alertManager.success("Test was saved successfully.");
        response.sendRedirect(request.getContextPath() + "/testboard/mytests");
    }

    public void runTest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, DAOException {
        String testId = request.getParameter("testId");
        long id;
        try {
            id = Long.parseLong(testId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        TestDao testDao = new TestDao(getConnectionPool());
        Test test = testDao.get(id);

        if(test == null) {
            getAlertManagerFromSession(request.getSession()).danger("Can't find the test. Please, try again!");
            response.sendRedirect(request.getContextPath() + "/" );
        } else {
            request.setAttribute("test", test);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD_TESTING);
            dispatcher.forward(request, response);
        }
    }

    public void processTestResult(User user, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, DAOException {

        if (user.getRole() != User.Role.STUDENT) {
            pageNotFound(request, response);
            return;
        }

        String strTestId = request.getParameter("testId");
        Long testId;
        try {
            testId = Long.parseLong(strTestId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        // Read from BD Lists correct answers for each question
        TestDao testDao = new TestDao(getConnectionPool());
        Map<Long, List<Long>> correctAnswerMap = testDao.getCorrectAnswerIds(testId);

        Map<Long, List<Long>> selectedAnswerMap = new HashMap<>();

        // Build from request lists of checked answers for each question
        String[] paramQuestions = request.getParameterValues("questionIds");
        for (int i = 0; i < paramQuestions.length; i++) {
            try {
                Long questionId = Long.parseLong(paramQuestions[i]);
                String[] paramAnswers = request.getParameterValues("answerIds[" + i + "]");
                for (int j = 0; j < paramAnswers.length; j++) {
                    Long answerId = Long.parseLong(paramAnswers[j]);

                    // is selected?
                    String paramCheckedAnswers = request.getParameter("checkedAnswers[" + i + "][" + j + "]");
                    if (paramCheckedAnswers != null) {
                        // add checked answer to map
                        if (!selectedAnswerMap.containsKey(questionId)) {
                            // create list of answers for each new question
                            selectedAnswerMap.put(questionId, new ArrayList<>());
                        }

                        selectedAnswerMap.get(questionId).add(answerId);
                    }

                }
            } catch (NumberFormatException e) {
                pageNotFound(request, response);
                return;
            }
        }


        Map<Long, Boolean> answerResults = new HashMap<>();
        int correctlySelAnswerCount = 0;

        for (Long questionId : correctAnswerMap.keySet()) {
            answerResults.put(questionId, false);

            if (selectedAnswerMap.containsKey(questionId)) {
                List<Long> selectedAnswers = selectedAnswerMap.get(questionId);
                List<Long> correctAnswers = correctAnswerMap.get(questionId);

                if (selectedAnswers.size() == correctAnswers.size()) {

                    boolean isListEq = true;
                    for (Long val : correctAnswers) {
                        if (!selectedAnswers.contains(val)) {
                            isListEq = false;
                            break;
                        }
                    }

                    if (isListEq) {
                        answerResults.put(questionId, true);
                        correctlySelAnswerCount++;
                    }
                }
            }
        }

        // Save results in DB
        TestingResults testingResults = new TestingResults(user.getId(), testId, answerResults);
        TestingResultsDao testingResultsDao = new TestingResultsDao(getConnectionPool());
        testingResultsDao.update(testingResults);

        // Show results
        int percentMark = 100 * correctlySelAnswerCount / correctAnswerMap.size();
        String message = "Your result is " + percentMark + "% right answers!";
        if (percentMark > 50) {
            getAlertManagerFromSession(request.getSession()).success(message);
        } else {
            getAlertManagerFromSession(request.getSession()).danger(message);
        }

        response.sendRedirect(request.getContextPath() + "/");
    }
}