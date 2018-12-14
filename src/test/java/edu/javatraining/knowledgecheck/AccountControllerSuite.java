package edu.javatraining.knowledgecheck;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.deploy.net.HttpResponse;
import edu.javatraining.knowledgecheck.configure.WebAppServletModule;
import edu.javatraining.knowledgecheck.controller.AccountControllerServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@Guice(modules = WebAppServletModule.class)
public class AccountControllerSuite {

    private static final Logger logger = LogManager.getLogger("Test");

    private final String VIEW_LOGIN_FORM = "/WEB-INF/view/account/LoginForm.jsp";
    private final String VIEW_REGISTRATION_FORM = "/WEB-INF/view/account/RegistrationForm.jsp";
    private final String VIEW_PASSWORD_RECOVERY_FORM = "/WEB-INF/view/account/PasswordRecoveryForm.jsp";
    private final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/common/PageNotFound.jsp";
    private final String VIEW_TEST_BOARD = "/WEB-INF/view/testing/TestList.jsp";
    private final String VIEW_MY_TESTS = "/WEB-INF/view/testing/MyTests.jsp";
    private final String VIEW_EDIT_TEST = "/WEB-INF/view/testing/EditTest.jsp";
    private final String VIEW_TEST = "/WEB-INF/view/testing/Test.jsp";
    private final String VIEW_STUDENTS_RESULTS = "/WEB-INF/view/testing/StudentsResults.jsp";
    private final String VIEW_TEST_STATISTICS = "/WEB-INF/view/testing/TestStatistics.jsp";
    private final String VIEW_MY_PROFILE_FORM = "/WEB-INF/view/account/MyProfileForm.jsp";
    private final String VIEW_USER_LIST = "/WEB-INF/view/account/UserList.jsp";
    private final String VIEW_SUBJECTS_FORM = "/WEB-INF/view/testing/SubjectsForm.jsp";
    private final String VIEW_PROFILE = "/WEB-INF/view/account/Profile.jsp";

    @Inject
    AccountControllerServlet servlet;

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

    @BeforeClass
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet.init(servletConfig);
        when(request.getSession()).thenReturn(session);
        when(servlet.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testGetAccountWrongPageShouldShowPageNotFound() throws Exception {
        shouldShowView("/account/absdef", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void testGetAccountLoginShouldShowLoginForm() throws Exception {
        shouldShowView("/account/login", VIEW_LOGIN_FORM);
    }

    @Test
    public void testGetAccountLoginShouldSetSessionAttrFID() throws Exception {

        when(request.getServletPath()).thenReturn("/account/login");
        servlet.doGet(request, response);
        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertEquals(sessionAttrName.getValue(), "FID");
    }

    @Test
    public void testGetAccountRegistrationShouldShowRegistrationForm() throws Exception {
        shouldShowView("/account/registration", VIEW_REGISTRATION_FORM);
    }

    @Test
    public void testGetAccountRegistrationShouldSetSessionAttrs() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration");
        servlet.doGet(request, response);
        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session, times(2)).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertEquals(sessionAttrName.getAllValues().get(0), "userDto");
        Assert.assertEquals(sessionAttrVal.getAllValues().get(0), null);
        Assert.assertEquals(sessionAttrName.getAllValues().get(1), "FID");
    }

    @Test
    public void testGetAccountRecoveryShouldShowRecoveryForm() throws Exception {
        shouldShowView("/account/recovery", VIEW_PASSWORD_RECOVERY_FORM);
    }

    @Test
    public void testGetAccountRecoveryShouldSetSessionAttrs() throws Exception {

        when(request.getServletPath()).thenReturn("/account/recovery");
        servlet.doGet(request, response);
        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session, times(2)).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertEquals(sessionAttrName.getAllValues().get(0), "userDto");
        Assert.assertEquals(sessionAttrVal.getAllValues().get(0), null);
        Assert.assertEquals(sessionAttrName.getAllValues().get(1), "FID");
    }

    @Test
    public void testAnonymGetAccountMyProfileShouldShowPageNotFound() throws Exception {
        shouldShowView("/account/myprofile", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void testGetAccountMyProfileShouldShowMyProfileForm() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/account/myprofile", VIEW_MY_PROFILE_FORM);
    }

    @Test
    public void testGetAccountMyProfileShouldSetSessionAttrs() throws Exception {

        when(request.getServletPath()).thenReturn("/account/myprofile");
        servlet.doGet(request, response);
        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session, times(2)).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertEquals(sessionAttrName.getAllValues().get(0), "userDto");
        Assert.assertEquals(sessionAttrVal.getAllValues().get(0), null);
        Assert.assertEquals(sessionAttrName.getAllValues().get(1), "FID");
    }

    private void shouldShowView(String url, String view) throws Exception {

        when(request.getServletPath()).thenReturn(url);
        servlet.doGet(request, response);
        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        Assert.assertEquals(dispatcherArgument.getValue(), view);
    }

}
