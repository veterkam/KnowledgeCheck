package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.UserFactory;
import edu.javatraining.knowledgecheck.configure.WebAppServletModule;
import edu.javatraining.knowledgecheck.controller.dto.TestDto;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.service.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Guice(modules = WebAppServletModule.class)
public class TestingControllerSuite {

    private final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/common/PageNotFound.jsp";
    private final String VIEW_TEST_LIST = "/WEB-INF/view/testing/TestList.jsp";
    private final String VIEW_MY_TESTS = "/WEB-INF/view/testing/MyTests.jsp";
    private final String VIEW_EDIT_TEST = "/WEB-INF/view/testing/EditTest.jsp";
    private final String VIEW_TEST = "/WEB-INF/view/testing/Test.jsp";
    private final String VIEW_STUDENTS_RESULTS = "/WEB-INF/view/testing/StudentsResults.jsp";
    private final String VIEW_TEST_STATISTICS = "/WEB-INF/view/testing/TestStatistics.jsp";
    private final String VIEW_SUBJECTS_FORM = "/WEB-INF/view/testing/SubjectsForm.jsp";

    @Inject
    private TestingControllerServlet servlet;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext context;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private Provider<TestService> testServiceProvider;
    @Mock
    private Provider<QuestionService> questionServiceProvider;
    @Mock
    private Provider<AnswerService> answerServiceProvider;
    @Mock
    private Provider<TestingResultsService> testingResultsServiceProvider;
    @Mock
    private Provider<SubjectService> subjectServiceProvider;
    @Mock
    private TestService testService;
    @Mock
    private QuestionService questionService;
    @Mock
    private AnswerService answerService;
    @Mock
    private TestingResultsService testingResultsService;
    @Mock
    private SubjectService subjectService;


    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet.init(servletConfig);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(servlet.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        when(testServiceProvider.get()).thenReturn(testService);
        when(questionServiceProvider.get()).thenReturn(questionService);
        when(answerServiceProvider.get()).thenReturn(answerService);
        when(testingResultsServiceProvider.get()).thenReturn(testingResultsService);
        when(subjectServiceProvider.get()).thenReturn(subjectService);

        servlet.setTestServiceProvider(testServiceProvider);
        servlet.setQuestionServiceProvider(questionServiceProvider);
        servlet.setAnswerServiceProvider(answerServiceProvider);
        servlet.setTestingResultServiceProvider(testingResultsServiceProvider);
        servlet.setSubjectServiceProvider(subjectServiceProvider);
    }

    @Test
    public void getWrongUrlShouldShowPageNotFound() throws Exception {
        shouldShowView("/anypath/WRONG_URL", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getTestingShouldShowTestList() throws Exception {
        shouldShowView("/testing", VIEW_TEST_LIST);
    }

    @Test
    public void getTestingShouldSetAttrs() throws Exception {
        shouldShowView("/testing", VIEW_TEST_LIST);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("tests"));
    }

    @Test
    public void getTestingStudentShouldSetAttrsStudentScores() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getStudent());
        shouldShowView("/testing", VIEW_TEST_LIST);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("tests"));
        Assert.assertTrue(attrName.getAllValues().contains("scores"));
    }

    @Test
    public void getSubjectsByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/subjects", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getSubjectsByNotTutorShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/subjects", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getSubjectsByTutorShouldShowSubjectsForm() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/subjects", VIEW_SUBJECTS_FORM);
    }

    @Test
    public void getSubjectsByTutorShouldSetAttr() throws Exception {

        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/subjects", VIEW_SUBJECTS_FORM);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(session).setAttribute( attrName.capture(), anyString());
        Assert.assertTrue(attrName.getAllValues().contains("FID"));

        verify(request).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("subjects"));
    }


    @Test
    public void getResultsByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/results", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getResultsByNotTutorShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/results", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getResultsByTutorShouldShowStudentsResults() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/results", VIEW_STUDENTS_RESULTS);
    }

    @Test
    public void getResultsByTutorShouldSetAttr() throws Exception {

        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/results", VIEW_STUDENTS_RESULTS);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("tests"));
        Assert.assertTrue(attrName.getAllValues().contains("testingResultsList"));
    }

    @Test
    public void getMyTestsByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/mytests", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getMyTestsByNotTutorShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/mytests", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getMyTestsByTutorShouldShowStudentsResults() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/mytests", VIEW_MY_TESTS);
    }

    @Test
    public void getMyTestsByTutorShouldSetAttr() throws Exception {

        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/mytests", VIEW_MY_TESTS);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("tests"));
    }

    @Test
    public void getTestStatisticsByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/statistics", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getTestStatisticsByNotTutorShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/statistics", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getTestStatisticsByTutorShouldShowStatistics() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/statistics", VIEW_TEST_STATISTICS);
    }

    @Test
    public void getTestStatisticsByTutorShouldSetAttr() throws Exception {

        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/statistics", VIEW_TEST_STATISTICS);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("tests"));
        Assert.assertTrue(attrName.getAllValues().contains("statisticsList"));
    }

    @Test
    public void getEditTestByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/edit", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getEditTestByNotTutorShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/edit", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getEditTestByTutorShouldShowEditTestForm() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/edit", VIEW_EDIT_TEST);
    }

    @Test
    public void getEditTestByTutorShouldSetAttrSubjects() throws Exception {

        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/edit", VIEW_EDIT_TEST);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("subjects"));
    }

    @Test
    public void getEditCreatedTestByTutorShouldSetAttrTest() throws Exception {

        User user = UserFactory.getTutor();
        edu.javatraining.knowledgecheck.domain.Test test = new edu.javatraining.knowledgecheck.domain.Test();
        test.setId(1L);
        test.setTutor( (Tutor) user);

        when(session.getAttribute("user")).thenReturn(user);
        when(testService.findComplexOneById(any())).thenReturn(test);
        when(request.getPathInfo()).thenReturn("/" + test.getId());
        shouldShowView("/testing/edit", VIEW_EDIT_TEST);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TestDto> attrTest = ArgumentCaptor.forClass(TestDto.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), attrTest.capture());
        Assert.assertTrue(attrName.getAllValues().contains("test"));
        Assert.assertEquals(attrTest.getAllValues().get(0).getTestId(), "" + test.getId());
    }

    @Test
    public void getEditCreatedTestByTutorNotOwnerShouldRedirectMyTests() throws Exception {

        User owner = UserFactory.getTutor();
        User notOwner = UserFactory.getTutor();
        notOwner.setId(10L);
        notOwner.setUsername("NOT_OWNER");
        edu.javatraining.knowledgecheck.domain.Test test = new edu.javatraining.knowledgecheck.domain.Test();
        test.setId(1L);
        test.setTutor( (Tutor) owner);

        when(session.getAttribute("user")).thenReturn(notOwner);
        when(testService.findComplexOneById(any())).thenReturn(test);
        when(request.getPathInfo()).thenReturn("/" + test.getId());
        when(request.getServletPath()).thenReturn("/testing/edit");
        servlet.doGet(request, response);

        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(response, only()).sendRedirect(arg.capture());
        Assert.assertEquals(arg.getValue(), "/testing/mytests");
    }

    @Test
    public void getRemoveTestByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/remove", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getRemoveTestByNotTutorShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/remove", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getRemoveTestByTutorWithOutTestIdShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        shouldShowView("/testing/remove", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getRemoveTestByTutorWithTestIdShouldCallDel() throws Exception {

        final Long testId = 1L;
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        when(request.getServletPath()).thenReturn("/testing/remove");
        when(request.getParameter("testId")).thenReturn("" + testId);
        servlet.doGet(request, response);

        ArgumentCaptor<edu.javatraining.knowledgecheck.domain.Test> arg =
                ArgumentCaptor.forClass(edu.javatraining.knowledgecheck.domain.Test.class);
        verify(testService, only()).delete(arg.capture());
        Assert.assertEquals(arg.getValue().getId(), testId);
    }

    @Test
    public void getRemoveTestByTutorAfterDelShouldRedirectMyTests() throws Exception {

        final Long testId = 1L;
        when(session.getAttribute("user")).thenReturn(UserFactory.getTutor());
        when(request.getServletPath()).thenReturn("/testing/remove");
        when(request.getParameter("testId")).thenReturn("" + testId);
        servlet.doGet(request, response);

        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(arg.capture());
        Assert.assertEquals(arg.getValue(), "/testing/mytests");
    }

    @Test
    public void getTestByAnonymShouldShowPageNotFound() throws Exception {
        shouldShowView("/testing/test", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getTestByNotStudentShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/testing/test", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getTestByStudentWithOutTestIdShouldShowPageNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getStudent());
        shouldShowView("/testing/test", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getTestByStudentWithTestIdShouldReadTestFromDB() throws Exception {
        Long testId = 1L;
        when(session.getAttribute("user")).thenReturn(UserFactory.getStudent());
        when(request.getServletPath()).thenReturn("/testing/test");
        when(request.getPathInfo()).thenReturn("/" + testId);
        servlet.doGet(request, response);

        ArgumentCaptor<Long> attr = ArgumentCaptor.forClass(Long.class);
        verify(testService).findComplexOneById(attr.capture());
        Assert.assertEquals(attr.getValue(), testId);
    }

    @Test
    public void getTestByStudentWithCorrectTestIdShouldRunTest() throws Exception {

        Long testId = 1L;
        when(session.getAttribute("user")).thenReturn(UserFactory.getStudent());
        when(request.getPathInfo()).thenReturn("/" + testId);
        when(testService.findComplexOneById(any())).thenReturn(new edu.javatraining.knowledgecheck.domain.Test());

        shouldShowView("/testing/test", VIEW_TEST);
    }

    @Test
    public void getTestByStudentWithWrongTestIdShouldRedirectHome() throws Exception {

        Long testId = 1L;
        when(session.getAttribute("user")).thenReturn(UserFactory.getStudent());
        when(request.getPathInfo()).thenReturn("/" + testId);
        when(request.getServletPath()).thenReturn("/testing/test");
        when(testService.findComplexOneById(any())).thenReturn(null);
        servlet.doGet(request, response);

        ArgumentCaptor<String> attr = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(attr.capture());
        Assert.assertEquals(attr.getValue(), "/");
    }

    private void shouldShowView(String url, String view) throws Exception {

        when(request.getServletPath()).thenReturn(url);
        servlet.doGet(request, response);
        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        verify(dispatcher, only()).forward(request, response);
        Assert.assertEquals(dispatcherArgument.getValue(), view);
    }
}
