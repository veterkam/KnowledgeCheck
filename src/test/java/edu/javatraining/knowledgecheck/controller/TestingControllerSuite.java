package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.configure.WebAppServletModule;
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
    private final String VIEW_TEST_BOARD = "/WEB-INF/view/testing/TestList.jsp";
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
    public void getWrongPageShouldShowPageNotFound() throws Exception {
        shouldShowView("/anypath/absdef", VIEW_PAGE_NOT_FOUND);
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
