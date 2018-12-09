package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.service.*;
import edu.javatraining.knowledgecheck.service.tools.AlertManager;
import edu.javatraining.knowledgecheck.service.tools.Pagination;
import edu.javatraining.knowledgecheck.service.tools.Presentation;
import edu.javatraining.knowledgecheck.service.tools.Validator;
import edu.javatraining.knowledgecheck.domain.*;
import org.apache.logging.log4j.util.Strings;

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
        "/",
        "/testing",
        "/testing/mytests",
        "/testing/edit",
        "/testing/remove",
        "/testing/testing",
        "/testing/testing/result",
        "/testing/studentsresults",
        "/testing/teststatistics",
        "/testing/subjects",
        "/testing/subjects/save"
})
@Singleton
public class TestingControllerServlet extends AbstractBaseControllerServlet {

    final int COUNT_TEST_ON_PAGE = 10;
    final int PAGINATION_LIMIT = 5;



    @Inject
    private Provider<TestService> testServiceProvider;
    @Inject
    private Provider<QuestionService> questionServiceProvider;
    @Inject
    private Provider<AnswerService> answerServiceProvider;
    @Inject
    private Provider<TestingResultsService> testingResultServiceProvider;
    @Inject
    private Provider<SubjectService> subjectServiceProvider;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {


        String action = request.getServletPath();

        try {
            switch (action) {
                case "/":
                case "/testing":
                    showSimpleListOfTests(request, response);
                    break;
                case "/testing/mytests":
                    showTutorMyTests(request, response);
                    break;
                case "/testing/edit":
                    editTest(request, response);
                    break;
                case "/testing/remove":
                    removeTest(request, response);
                    break;
                case "/testing/testing":
                    runTest(request, response);
                    break;
                case "/testing/testing/result":
                    processTestResult(request, response);
                    break;
                case "/testing/studentsresults":
                    studentsResults(request, response);
                    break;
                case "/testing/teststatistics":
                    testStatistics(request, response);
                    break;
                case "/testing/subjects":
                    showSubjectsForm(request, response);
                    break;
                case "/testing/subjects/save":
                    saveSubjects(request, response);
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

    private ViewOptions initViewOptions(HttpServletRequest request, TestService testService, boolean filterByUser) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // Init presentation
        Presentation presentation = new Presentation();

        String dateOrder;

        // Read subject list for subject filter
        SubjectService subjectService = subjectServiceProvider.get();
        presentation.setSubjects( subjectService.findAll() );

        // Scan parameters
        // Current page of test list
        String pageNoStr = request.getParameter("pageNo");
        int pageNo;
        try {
            pageNo = Integer.parseInt(pageNoStr);
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        // Filter on subject id
        Long subjectId;
        String strSubjectId = request.getParameter("presentationSubjectId");
        try {
            subjectId = Long.parseLong(strSubjectId);
        } catch (NumberFormatException e) {
            strSubjectId = (String) session.getAttribute("presentationSubjectId");
            subjectId = (Strings.isBlank(strSubjectId)) ? 0 : Long.parseLong(strSubjectId);
        }
        presentation.setSubjectId(subjectId);

        // Order by date
        dateOrder = request.getParameter("presentationDateOrder");
        if(Strings.isBlank(dateOrder)) {
            dateOrder = (String) session.getAttribute("presentationDateOrder");
        }
        presentation.setDateOrder(dateOrder);

        // If need enable filter
        if(subjectId > 0) {
            testService.setFilterSubjectId(presentation.getSubjectId());
            testService.enableFilter();
        }

        if (filterByUser) {
            testService.setFilterTutorId(user.getId());
            testService.enableFilter();
        }

        // Enable order by date
        if( presentation.getDateOrder().equals(Presentation.DATE_DESCENDING) ) {
            testService.enableDescDateOrder();
        } else {
            testService.enableAscDateOrder();
        }

        // Calc test count
        Long count = testService.count();

        // Init pagination
        int pageCount = (int) Math.ceil(((double)count) / ((double)COUNT_TEST_ON_PAGE));
        Pagination pagination = new Pagination(pageNo, pageCount, PAGINATION_LIMIT);

        return new ViewOptions(presentation, pagination);
    }

    private void storeViewOptions(ViewOptions vo, HttpServletRequest request) {
        // Data for short period store in request
        request.setAttribute("presentationSubjects", vo.presentation.getSubjects());
        request.setAttribute("presentationOrders", vo.presentation.getOrders());
        // Data for long period store in session
        request.getSession().setAttribute(
                "presentationSubjectId", String.valueOf( vo.presentation.getSubjectId() ) );
        request.getSession().setAttribute(
                "presentationDateOrder", vo.presentation.getDateOrder());

        request.setAttribute("pagination", vo.pagination);
    }

    private void showTests(HttpServletRequest request, HttpServletResponse response,
                           String view, boolean onlyTest, boolean filterByTutor)
            throws IOException, ServletException, DAOException {
        User user = (User) request.getSession().getAttribute("user");

        if(filterByTutor && (user == null || user.getRole() != User.Role.TUTOR)) {
            pageNotFound(request, response);
            return;
        }

        TestService testService = testServiceProvider.get();
        ViewOptions vo = initViewOptions(request, testService, filterByTutor);

        // Read test list
        Long offset = (long)(vo.pagination.getCurrent() - 1) * COUNT_TEST_ON_PAGE;
        List<Test> tests;
        if(onlyTest) {
            // without question and answers
            tests = testService.findPlainAll(offset, (long) COUNT_TEST_ON_PAGE);
        } else {
            // with question and answers
            tests = testService.findComplexAll(offset, (long) COUNT_TEST_ON_PAGE);
        }

        if(user != null && user.getRole() == User.Role.STUDENT) {
            List<Integer> scores = new ArrayList<>();
            TestingResultsService testingResultsService = testingResultServiceProvider.get();
            for (Test test : tests) {
                TestingResults tr = testingResultsService.find(user.getId(), test.getId());
                scores.add(tr.getScore());
            }
            request.setAttribute("scores", scores);
        }

        // Store data in request and session
        storeViewOptions(vo, request);
        request.setAttribute("tests", tests);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

    private void showSimpleListOfTests(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // Show all tests without question data and answer data
        showTests(request, response, VIEW_TEST_BOARD, true, false);
    }

    private void showTutorMyTests(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // Show all current user's tests with question data and answer data
        showTests(request, response, VIEW_MY_TESTS, false, true);
    }

    private void removeTest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        String testId = request.getParameter("testId");
        long id;
        try {
            id = Long.parseLong(testId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);
        Test test = new Test();
        test.setId(id);
        test.setTutor(new Tutor(user));
        TestService testService = testServiceProvider.get();
        if(testService.delete(test)) {
            alertManager.success("The test was successfully removed.");
        } else {
            alertManager.danger("The test was not removed. Maybe you don't have permission to removed it.");
        }

        response.sendRedirect(request.getContextPath() + "/testing/mytests");

    }

    private void editTest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);
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

        TestService testService = testServiceProvider.get();
        SubjectService subjectService = subjectServiceProvider.get();

        if (Validator.containNull(paramSubject, paramTitle, paramDescription)) {
            // Parameters do not have test data
            if(test.getId() > -1) {
                test = testService.findComplexOneById(test.getId());
                if(test.getTutorId().equals( user.getId() )) {
                    request.setAttribute("test", test );
                } else {
                    alertManager.danger("You don't have permission to change this test.");
                    response.sendRedirect(request.getContextPath() + "/testing/mytests");
                    return;
                }
            }
            // Only show empty edit form
            // Read subject list for subject filter in presentation
            request.setAttribute("subjects", subjectService.findAll() );
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }

        Subject subject = null;

        try {
            Long subjectId = Long.parseLong(paramSubject);
            subject = subjectService.findOneById(subjectId);
        } catch (NumberFormatException e) {
            alertManager.danger("Subject is wrong.");
        }

        test.setSubject(subject);
        test.setTitle(paramTitle);
        test.setDescription(paramDescription);

        Validator validator = new Validator();
        validator.validate(test);

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
            question.setDescription(paramQuestionDescs[i]);
            validator.validate(question);
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
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // it's not removed, do nothing
                    }
                } catch (NumberFormatException e) {
                    // it's new answer, do nothing
                }

                answer.setDescription(paramAnswers[j]);

                String paramCorrect = request.getParameter("corrects["+i+"]["+j+"]");
                boolean correct = (paramCorrect != null);
                answer.setCorrect(correct);
                answerList.add(answer);

                validator.validate(answer);
            }

            question.setAnswers(answerList);
            questionList.add(question);
        }
        test.setQuestions(questionList);

        if (validator.isFailed()) {
            // We have errors
            alertManager.danger(validator.getErrors());
            // Read subject list for subject filter in presentation
            request.setAttribute("subjects",  subjectService.findAll() );
            // Send test data for auto filing
            request.setAttribute("test", test);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_EDIT_TEST);
            dispatcher.forward(request, response);
            return;
        }

        if(!remAnswerList.isEmpty()) {
            AnswerService service = answerServiceProvider.get();
            for(Answer answer : remAnswerList) {
                service.delete(answer);
            }
        }

        if(!remQuestionList.isEmpty()) {
            QuestionService service = questionServiceProvider.get();
            for(Question question : remQuestionList) {
                service.delete(question);
            }
        }

        if( !testService.updateComplex(test) ) {
            testService.insertComplex(test);
        }

        alertManager.success("Test was saved successfully.");
        response.sendRedirect(request.getContextPath() + "/testing/mytests");
    }

    public void runTest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, DAOException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.STUDENT) {
            pageNotFound(request, response);
            return;
        }

        String strTestId = request.getParameter("testId");
        long testId;
        try {
            testId = Long.parseLong(strTestId);
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        TestService testService = testServiceProvider.get();
        Test test = testService.findComplexOneById(testId);

        if(test == null) {
            getAlertManager(request).danger("Can't find the test. Please, try again!");
            response.sendRedirect(request.getContextPath() + "/" );
        } else {
            request.setAttribute("test", test);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TESTING);
            dispatcher.forward(request, response);
        }
    }

    public void processTestResult(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, DAOException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.STUDENT) {
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

        // Read from DB Lists correct answers for each question
        TestService testService = testServiceProvider.get();
        Map<Long, List<Long>> correctAnswerMap = testService.findCorrectAnswerIdsByTestId(testId);

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
        TestingResultsService testingResultsDao = testingResultServiceProvider.get();
        testingResultsDao.update(testingResults);

        // Show results
        int score = Math.round(100.0f * ((float)correctlySelAnswerCount) / ((float) correctAnswerMap.size()));
        String message = "Your result is " + score + "% correct answers!";
        if (score > 50) {
            getAlertManager(request).success(message);
        } else {
            getAlertManager(request).danger(message);
        }

        response.sendRedirect(request.getContextPath() + "/");
    }

    public void studentsResults(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        TestService testService = testServiceProvider.get();
        ViewOptions vo = initViewOptions(request, testService, true);

        // Read test list
        Long offset = (long) (vo.pagination.getCurrent() - 1) * COUNT_TEST_ON_PAGE;
        List<Test> tests;

        // single test list, without question and answers
        tests = testService.findPlainAll(offset, (long) COUNT_TEST_ON_PAGE);
        QuestionService questionDao = questionServiceProvider.get();
        // Results of testing for each test and student
        List< List<TestingResults> > testingResultsList = new ArrayList<>();
        TestingResultsService testingResultsDao = testingResultServiceProvider.get();
        for(Test test : tests) {
            // attach simple question list without answers
            List<Question> questions =  questionDao.findPlainAll(test.getId());
            test.setQuestions(questions);
            // read students's results of testing
            List<TestingResults> testingResults = testingResultsDao.find(test.getId());
            testingResultsList.add(testingResults);
        }

        // Store data in request and session
        storeViewOptions(vo, request);
        request.setAttribute("tests", tests);
        request.setAttribute("testingResultsList", testingResultsList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_STUDENTS_RESULTS);
        dispatcher.forward(request, response);
    }

    public void testStatistics(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        TestService testService = testServiceProvider.get();
        ViewOptions vo = initViewOptions(request, testService, true);

        // Read test list
        Long offset = (long) (vo.pagination.getCurrent() - 1) * COUNT_TEST_ON_PAGE;
        List<Test> tests;

        // single test list, without question and answers
        tests = testService.findPlainAll(offset, (long) COUNT_TEST_ON_PAGE);
        QuestionService questionDao = questionServiceProvider.get();
        TestingResultsService testingResultsDao = testingResultServiceProvider.get();
        List<TestStatistics> statisticsList = new ArrayList<>();
        for(Test test : tests) {
            // attach simple question list without answers
            List<Question> questions =  questionDao.findPlainAll(test.getId());
            test.setQuestions(questions);
            // read students's results of testing
            List<TestingResults> testingResultsList = testingResultsDao.find(test.getId());

            // Calculate statistics
            TestStatistics statistics = new TestStatistics();
            int studentCount = testingResultsList.size();
            statistics.setStudentCount(studentCount);
            // Calculate scores. First calc how many students
            // answered correct for each question
            Map<Long, Integer> scores = new HashMap<>();
            // results for each student for current test
            for(TestingResults results : testingResultsList) {

                for (long questionId : results.getAnswerResults().keySet()) {

                    if (!scores.containsKey(questionId)) {
                        scores.put(questionId, 0);
                    }

                    // answer is(not) correct for each question?
                    boolean correct = results.getAnswerResults().get(questionId);
                    if (correct) {
                        int correctAnswerCount = scores.get(questionId) + 1;
                        scores.put(questionId, correctAnswerCount);
                    }
                }
            }

            // Second calc scores: divided count of correct student's answers
            // by count of students
            for (long questionId : scores.keySet()) {
                int correctAnswerCount = scores.get(questionId);
                int score = Math.round(100.0f * ((float)correctAnswerCount) / ((float) statistics.getStudentCount()));
                scores.put(questionId, score);
            }

            statistics.setScores(scores);
            statisticsList.add(statistics);
        }

        // Store data in request and session
        storeViewOptions(vo, request);
        request.setAttribute("tests", tests);
        request.setAttribute("statisticsList", statisticsList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_STATISTICS);
        dispatcher.forward(request, response);
    }


    public void showSubjectsForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        SubjectService subjectService = subjectServiceProvider.get();
        List<Subject> subjects = subjectService.findAll();
        request.setAttribute("subjects", subjects);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_SUBJECTS_FORM);
        dispatcher.forward(request, response);
    }

    public void saveSubjects(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        String[] subjectNames = request.getParameterValues("subjects");
        String[] subjectIds = request.getParameterValues("subjectIds");
        String[] modify = request.getParameterValues("modify");
        String[] remove = request.getParameterValues("remove");

        AlertManager alertManager = getAlertManager(request);
        SubjectService subjectService = subjectServiceProvider.get();
        Subject subject = new Subject();
        Long id;

        for(int i = 0; i < subjectNames.length; i++) {
            try {
                id = Long.parseLong(subjectIds[i]);
            } catch(NumberFormatException e) {
                pageNotFound(request, response);
                return;
            }

            subject.setId(id);
            subject.setName(subjectNames[i]);

            try {
                if("1".equals(remove[i])) {
                    subjectService.delete(subject);
                } else if("1".equals(modify[i]) && Strings.isNotBlank(subjectNames[i])) {
                   if(id > -1) {
                       subjectService.update(subject);
                   } else {
                       subjectService.insert(subject);
                   }
                }
            } catch(DAOException e) {
                logger.error(e.getMessage(), e);
                alertManager.danger(e.getMessage() + " \"" + subject.getName() + "\"");
            }
        }

        if(alertManager.isEmpty()) {
            alertManager.success("Changes are saved successfully");
        }

        response.sendRedirect(request.getContextPath() + "/testing/subjects");
    }

    private class ViewOptions {
        private Presentation presentation;
        private Pagination pagination;

        public ViewOptions(Presentation presentation, Pagination pagination) {
            this.presentation = presentation;
            this.pagination = pagination;
        }
    }

}