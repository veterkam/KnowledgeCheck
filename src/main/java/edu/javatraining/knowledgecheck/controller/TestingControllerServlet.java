package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import edu.javatraining.knowledgecheck.controller.dto.*;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.exception.RequestException;
import edu.javatraining.knowledgecheck.service.*;
import edu.javatraining.knowledgecheck.service.tools.AlertManager;
import edu.javatraining.knowledgecheck.service.tools.LocaleMsgReader;
import edu.javatraining.knowledgecheck.service.tools.Pagination;
import edu.javatraining.knowledgecheck.service.tools.Presentation;
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
        "/testing/test",
        "/testing/test/result",
        "/testing/studentsresults",
        "/testing/teststatistics",
        "/testing/subjects"
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

        String action = request.getServletPath();

        try {
            switch (action) {
                case "/testing/subjects":
                    saveSubjects(request, response);
                    break;
                case "/testing/edit":
                    editTestProcessing(request, response);
                    break;
                case "/testing/test":
                    testProcessing(request, response);
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
                    showEditTestForm(request, response);
                    break;
                case "/testing/remove":
                    removeTest(request, response);
                    break;
                case "/testing/test":
                    runTest(request, response);
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
        int pageNo;
        try {
            pageNo = Integer.parseInt( request.getParameter("pageNo") );
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
            throws IOException, ServletException {
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
            tests = testService.findPlainTests(offset, (long) COUNT_TEST_ON_PAGE);
        } else {
            // with question and answers
            tests = testService.findAllTestsWithQuestionsAndAnswers(offset, (long) COUNT_TEST_ON_PAGE);
        }

        if(user != null && user.getRole() == User.Role.STUDENT) {
            // if student, than add scores(results) to the page
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
        forward(request, response, view);
    }

    private void showSimpleListOfTests(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Show all tests without question data and answer data
        showTests(request, response, VIEW_TEST_BOARD, true, false);
    }

    private void showTutorMyTests(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Show all current user's tests with question data and answer data
        showTests(request, response, VIEW_MY_TESTS, false, true);
    }

    private void removeTest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != User.Role.TUTOR) {
            pageNotFound(request, response);
            return;
        }

        long id;
        try {
            id = Long.parseLong( request.getParameter("testId") );
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);
        Test test = new Test();
        test.setId(id);
        // Set tutor for safe removing
        test.setTutor(new Tutor(user));
        TestService testService = testServiceProvider.get();
        if(testService.delete(test)) {
            alertManager.success("app.testing.test_removing_success");
        } else {
            alertManager.danger("app.testing.test_removing_failed");
        }

        redirect(request, response, "/testing/mytests");

    }

    private void forwardEditTestForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        genFormId(request.getSession());
        // Read subject list for subject filter in presentation
        request.setAttribute("subjects", subjectServiceProvider.get().findAll() );
        forward(request, response, VIEW_EDIT_TEST);
    }

    private void showEditTestForm(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        User user = checkPermit(request, User.Role.TUTOR);

        if(user == null) {
            pageNotFound(request, response);
            return;
        }

        try {
            Long testId = Long.parseLong( request.getParameter("testId") );
            // Read test info from Data Base
            TestService testService = testServiceProvider.get();
            Test test = testService.findComplexOneById(testId);
            if(test != null && test.getTutorId().equals( user.getId() )) {
                request.setAttribute("test", new TestDto(test) );
            } else {
                getAlertManager(request).danger("app.testing.you_do_not_have_permission_to_change_this_test");
                redirect(request, response, "/testing/mytests");
                return;
            }
        } catch(NumberFormatException e) {
            // It is new test
            // there is nothing to read from DB
        }

        forwardEditTestForm(request, response);
    }

    private void editTestProcessing(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        User user = checkPermit(request, User.Role.TUTOR);

        if(!checkFormId(request) || user == null) {
            pageNotFound(request, response);
            return;
        }

        AlertManager alertManager = getAlertManager(request);
        TestDto testDto = extractTestDto(request);

        if( !validateTestDto(testDto) ) {
            // Return test to edit
            alertManager.danger("app.testing.test_saving_failed");
            request.setAttribute("test", testDto );
            forwardEditTestForm(request, response);
            return;
        }

        // Drop removed questions or answers from DB
        dropRemovedElementsFromDataSource(testDto);
        // Save test's changes
        Test test = testDto.toTest();
        test.setTutor(new Tutor(user));
        testServiceProvider.get().save(test);

        alertManager.success("app.testing.test_saving_success");
        redirect(request, response, "/testing/mytests");
    }

    private TestDto extractTestDto(HttpServletRequest request) {
        TestDto testDto = new TestDto();

        testDto.setTestId( request.getParameter("testId") );
        testDto.setSubjectId( request.getParameter("subjectId") );
        testDto.setTitle( request.getParameter("title") );
        testDto.setDescription( request.getParameter("description") );

        testDto.setQuestions( extractQuestionsDto(request) );

        return testDto;
    }

    private List<QuestionDto> extractQuestionsDto(HttpServletRequest request) {
        // Extract question DTO from request parameters

        List<QuestionDto> questions = new ArrayList<>();

        String[] paramQuestionDescs = request.getParameterValues("questionDescriptions");
        String[] paramQuestionIds = request.getParameterValues("questionIds");
        String[] paramQuestionRemFlags = request.getParameterValues("questionRemovingFlags");

        for(int i = 0; i < paramQuestionDescs.length; i++) {

            QuestionDto questionDto = new QuestionDto();
            questionDto.setQuestionId( paramQuestionIds[i] );
            questionDto.setRemoved( paramQuestionRemFlags[i] );
            questionDto.setDescription( paramQuestionDescs[i] );
            questionDto.setAnswers( extractAnswersDto(request, i) );

            questions.add(questionDto);
        }

        return questions;
    }

    private List<AnswerDto> extractAnswersDto(HttpServletRequest request, int offset) {
        // Extract answer DTO from request parameters

        List<AnswerDto> answers = new ArrayList<>();

        String[] paramAnswerDescs = request.getParameterValues("answerDescriptions["+ offset +"]");
        String[] paramAnswerIds = request.getParameterValues("answerIds["+ offset +"]");
        String[] paramAnswerRemFlags = request.getParameterValues("answerRemovingFlags["+ offset +"]");

        for(int j = 0; j < paramAnswerDescs.length; j++) {
            AnswerDto answerDto = new AnswerDto();
            answerDto.setAnswerId( paramAnswerIds[j] );
            answerDto.setRemoved( paramAnswerRemFlags[j] );
            answerDto.setDescription( paramAnswerDescs[j] );
            answerDto.setCorrect( request.getParameter("corrects["+offset+"]["+j+"]") != null );

            answers.add(answerDto);
        }

        return answers;
    }

    private <T extends DtoWithErrors> boolean validateTestingDto(T dto) {

        boolean isValid = true;

        Map<String, List<String>> errors = DtoValidator.validate(dto);

        if(!errors.isEmpty()) {
            isValid = false;
            dto.setErrors(errors);
        }

        return isValid;
    }

    private boolean validateTestDto(TestDto testDto) {

        boolean isValid = validateTestingDto(testDto);

        for(QuestionDto q : testDto.getQuestions()) {

            if(q.isRemoved()) {
                continue;
            }

            isValid &= validateTestingDto(q);

            for(AnswerDto a : q.getAnswers()) {

                if(a.isRemoved()) {
                    continue;
                }

                isValid &= validateTestingDto(a);
            }
        }

        return isValid;
    }

    private void dropRemovedElementsFromDataSource(TestDto testDto) {

        for(QuestionDto q : testDto.getQuestions()) {

            if(q.isRemoved()) {
                try {
                    questionServiceProvider.get().deleteById(
                            Long.parseLong(q.getQuestionId()));
                } catch (NumberFormatException e) {
                    // do nothing
                }
            } else {
                for(AnswerDto a : q.getAnswers()) {
                    if(a.isRemoved()) {
                        try {
                            answerServiceProvider.get().deleteById(
                                    Long.parseLong(a.getAnswerId()));
                        } catch (NumberFormatException e) {
                            // do nothing
                        }
                    }
                }
            }
        }
    }

    public void runTest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        if(checkPermit(request, User.Role.STUDENT) == null) {
            pageNotFound(request, response);
            return;
        }

        long testId;
        try {
            testId = Long.parseLong( request.getParameter("id") );
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        Test test = testServiceProvider.get().findComplexOneById(testId);

        if(test == null) {
            getAlertManager(request).danger("app.testing.can_not_find_test");
            redirect(request, response, "/" );
        } else {
            request.setAttribute("test", test);
            forward(request, response, VIEW_TEST);
        }
    }

    public void testProcessing(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Process results of test
        User user = checkPermit(request, User.Role.STUDENT);

        if(user == null ) {
            pageNotFound(request, response);
            return;
        }

        Long testId;
        try {
            testId = Long.parseLong( request.getParameter("testId") );
        } catch (NumberFormatException e) {
            pageNotFound(request, response);
            return;
        }

        // Read from DB Lists correct answers for each question
        // Map<Long questionId, List<Long answerId>>
        Map<Long, List<Long>> correctAnswersMap = testServiceProvider.get().findCorrectAnswerIdsByTestId(testId);
        // Extract checked answers from request
        Map<Long, List<Long>> checkedAnswersMap = extractCheckedAnswersMap(request);
        // Calculate correctly checked answers
        // Map<Long questionId, boolean>
        Map<Long, Boolean> answerResults = calcCorrectlyCheckedAnswers(correctAnswersMap, checkedAnswersMap);
        // Save results in DB
        TestingResults testingResults = new TestingResults(user.getId(), testId, answerResults);
        TestingResultsService testingResultsDao = testingResultServiceProvider.get();
        testingResultsDao.update(testingResults);

        // Prepare message with results
        int score = testingResults.getScore();
        String message = LocaleMsgReader.message(request,
                "app.testing.your_result_is", score);
        if (score > 50) {
            getAlertManager(request).success(message, false);
        } else {
            getAlertManager(request).warning(message, false);
        }

        redirect(request, response, "/");
    }

    private Map<Long, Boolean> calcCorrectlyCheckedAnswers(
                                            Map<Long, List<Long>> correctAnswersMap,
                                            Map<Long, List<Long>> checkedAnswersMap) {
        // Calculate correctly checked answers
        // Map<Long questionId, boolean>
        Map<Long, Boolean> answerResults = new HashMap<>();

        for (Long questionId : correctAnswersMap.keySet()) {
            answerResults.put(questionId, false);

            if (checkedAnswersMap.containsKey(questionId)) {
                List<Long> checkedAnswers = checkedAnswersMap.get(questionId);
                List<Long> correctAnswers = correctAnswersMap.get(questionId);

                if (checkedAnswers.size() == correctAnswers.size()) {

                    boolean isListEq = true;
                    for (Long val : correctAnswers) {
                        if (!checkedAnswers.contains(val)) {
                            isListEq = false;
                            break;
                        }
                    }

                    answerResults.put(questionId, isListEq);
                }
            }
        }

        return answerResults;
    }

    private Map<Long, List<Long>> extractCheckedAnswersMap(HttpServletRequest request)
        throws RequestException {

        // Extract from request lists of checked answers for each question
        // Map<Long questionId, List<Long correct answerId>>
        Map<Long, List<Long>> checkedAnswerMap = new HashMap<>();
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
                        if (!checkedAnswerMap.containsKey(questionId)) {
                            // create list of answers for each new question
                            checkedAnswerMap.put(questionId, new ArrayList<>());
                        }

                        checkedAnswerMap.get(questionId).add(answerId);
                    }

                }
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
                throw new RequestException(e.getMessage(), e);
            }
        }

        return checkedAnswerMap;
    }

    public void studentsResults(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if(checkPermit(request, User.Role.TUTOR) == null ) {
            pageNotFound(request, response);
            return;
        }

        TestService testService = testServiceProvider.get();
        ViewOptions vo = initViewOptions(request, testService, true);
        Long offset = (long) (vo.pagination.getCurrent() - 1) * COUNT_TEST_ON_PAGE;

        List<Test> tests = testService.findAllTestsWithQuestions(offset, (long) COUNT_TEST_ON_PAGE);

        // Prepare results of testing for each test and student
        List< List<TestingResults> > testingResultsList = new ArrayList<>();
        TestingResultsService testingResultsService = testingResultServiceProvider.get();
        for(Test test : tests) {
            // Read students's results of testing for current test
            List<TestingResults> testingResults = testingResultsService.find(test.getId());
            testingResultsList.add(testingResults);
        }

        // Store data in request and session
        storeViewOptions(vo, request);
        request.setAttribute("tests", tests);
        request.setAttribute("testingResultsList", testingResultsList);
        forward(request, response, VIEW_STUDENTS_RESULTS);
    }

    public void testStatistics(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        User user = checkPermit(request, User.Role.TUTOR);

        if(user == null ) {
            pageNotFound(request, response);
            return;
        }

        ViewOptions vo = initViewOptions(request, testServiceProvider.get(), true);
        Long offset = (long) (vo.pagination.getCurrent() - 1) * COUNT_TEST_ON_PAGE;

        List<Test> tests = testServiceProvider.get()
                .findAllTestsWithQuestions(offset, (long) COUNT_TEST_ON_PAGE);
        TestingResultsService testingResultsDao = testingResultServiceProvider.get();
        List<TestStatistics> statisticsList = new ArrayList<>();
        for(Test test : tests) {
            // read students's results of testing
            // and calc statistics
            List<TestingResults> testingResultsList = testingResultsDao.find(test.getId());
            TestStatistics statistics = calcStatistics(testingResultsList);
            statisticsList.add(statistics);
        }

        // Store data in request and session
        storeViewOptions(vo, request);
        request.setAttribute("tests", tests);
        request.setAttribute("statisticsList", statisticsList);
        forward(request, response, VIEW_TEST_STATISTICS);
    }

    private TestStatistics calcStatistics(List<TestingResults> testingResultsList) {
        // Get student testing results list for one test
        // Calculate statistics correct and incorrect
        // answers for each question of current test
        TestStatistics statistics = new TestStatistics();
        int studentCount = testingResultsList.size();
        statistics.setStudentCount(studentCount);

        // First calc how many students
        // answered correct for each question
        Map<Long, Integer> scores = new HashMap<>();
        // results for each student for current test
        for(TestingResults results : testingResultsList) {

            for (long questionId : results.getAnswerResults().keySet()) {

                if (!scores.containsKey(questionId)) {
                    scores.put(questionId, 0);
                }

                // answer is correct?
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

        return statistics;
    }


    public void showSubjectsForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if (checkPermit(request, User.Role.TUTOR) == null) {
            pageNotFound(request, response);
            return;
        }

        SubjectService subjectService = subjectServiceProvider.get();
        List<Subject> subjects = subjectService.findAll();
        request.setAttribute("subjects", subjects);
        genFormId(request.getSession());
        forward(request, response, VIEW_SUBJECTS_FORM);
    }

    public void saveSubjects(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if(!checkFormId(request)) {
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
                // Check action
                if("1".equals(remove[i])) {
                    subjectService.delete(subject);
                } else if("1".equals(modify[i]) && Strings.isNotBlank(subjectNames[i])) {
                    subjectService.save(subject);
                }
            } catch(DAOException e) {
                logger.error(e.getMessage(), e);
                alertManager.danger("app.common.unknown_error");
                alertManager.danger("app.common.try_again");
            }
        }

        if(alertManager.isEmpty()) {
            alertManager.success("app.common.changes_are_saved");
        }

        redirect(request, response, "/testing/subjects");
    }

    private class ViewOptions {
        private Presentation presentation;
        private Pagination pagination;

        public ViewOptions(Presentation presentation, Pagination pagination) {
            this.presentation = presentation;
            this.pagination = pagination;
        }
    }

    private User checkPermit(HttpServletRequest request, User.Role role) {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null || user.getRole() != role) {
            return null;
        }

        return user;
    }

}