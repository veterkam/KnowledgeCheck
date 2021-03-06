package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.factory.UserFactory;
import edu.javatraining.knowledgecheck.configure.WebAppServletModule;
import edu.javatraining.knowledgecheck.controller.dto.UserDto;
import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.service.StudentService;
import edu.javatraining.knowledgecheck.service.TutorService;
import edu.javatraining.knowledgecheck.service.UserService;
import edu.javatraining.knowledgecheck.tools.Cipher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@Guice(modules = WebAppServletModule.class)
public class AccountControllerSuite {

    private static final Logger logger = LogManager.getLogger("Test");

    private final String VIEW_LOGIN_FORM = "/WEB-INF/view/account/LoginForm.jsp";
    private final String VIEW_REGISTRATION_FORM = "/WEB-INF/view/account/RegistrationForm.jsp";
    private final String VIEW_PASSWORD_RECOVERY_FORM = "/WEB-INF/view/account/PasswordRecoveryForm.jsp";
    private final String VIEW_PAGE_NOT_FOUND = "/WEB-INF/view/common/PageNotFound.jsp";
    private final String VIEW_MY_PROFILE_FORM = "/WEB-INF/view/account/MyProfileForm.jsp";
    private final String VIEW_USER_LIST = "/WEB-INF/view/account/UserList.jsp";
    private final String VIEW_PROFILE = "/WEB-INF/view/account/Profile.jsp";

    @Inject
    private AccountControllerServlet servlet;

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
    private Provider<UserService> userServiceProvider;
    @Mock
    private Provider<TutorService> tutorServiceProvider;
    @Mock
    private Provider<StudentService> studentServiceProvider;
    @Mock
    private UserService userService;
    @Mock
    private TutorService tutorService;
    @Mock
    private StudentService studentService;


    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet.init(servletConfig);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(servlet.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        when(userServiceProvider.get()).thenReturn(userService);
        when(tutorServiceProvider.get()).thenReturn(tutorService);
        when(studentServiceProvider.get()).thenReturn(studentService);

        servlet.setUserServiceProvider(userServiceProvider);
        servlet.setTutorServiceProvider(tutorServiceProvider);
        servlet.setStudentServiceProvider(studentServiceProvider);
    }

    @Test
    public void getWrongUrlShouldShowPageNotFound() throws Exception {
        shouldShowView("/account/absdef", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getLoginShouldShowLoginForm() throws Exception {
        shouldShowView("/account/login", VIEW_LOGIN_FORM);
    }

    @Test
    public void getLoginShouldSetSessionAttrFID() throws Exception {

        when(request.getServletPath()).thenReturn("/account/login");
        servlet.doGet(request, response);
        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertEquals(sessionAttrName.getValue(), "FID");
    }

    private void tryLogin(User user, String username, String password) throws Exception {

        when(request.getServletPath()).thenReturn("/account/login");
        when(session.getAttribute("FID")).thenReturn("abc");
        when(request.getParameter("_FID")).thenReturn("abc");
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.findOneByUsername(username)).thenReturn(user);

        switch(user.getRole()) {
            case STUDENT:
                when(studentService.attachProfile(user)).thenReturn((Student) user);
                break;
            case TUTOR:
                when(tutorService.attachProfile(user)).thenReturn((Tutor) user);
                break;
        }

        servlet.doPost(request, response);
    }

    @Test
    public void postAfterCorrectLoginShouldSetSessionAttrUser() throws Exception {

        User user = UserFactory.getTutor();
        tryLogin(user, user.getUsername(), "123");

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> attrValue = ArgumentCaptor.forClass(User.class);
        verify(session, atLeastOnce()).setAttribute( attrName.capture(), attrValue.capture());

        Assert.assertTrue(attrName.getAllValues().contains("user"));
        int index = attrName.getAllValues().indexOf("user");
        Assert.assertEquals(attrValue.getAllValues().get(index), user);

    }

    @Test
    public void postAfterCorrectLoginShouldRedirectHome() throws Exception {

        User user = UserFactory.getStudent();
        tryLogin(user, user.getUsername(), "123");

        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        verify(response, only()).sendRedirect( url.capture());

        Assert.assertEquals(url.getValue(), "/");
    }

    @Test
    public void postAfterCorrectLoginShouldChangePasswordHash() throws Exception {

        User user = UserFactory.getStudent();
        final String passwordHash = user.getPassword();
        tryLogin(user, user.getUsername(), "123");

        servlet.doPost(request, response);

        Assert.assertNotEquals(passwordHash, user.getPassword());
    }

    @Test
    public void postAfterUnCorrectLoginShouldForwardLoginForm() throws Exception {

        User user = UserFactory.getStudent();
        tryLogin(user, user.getUsername(), "WRONG PASSWORD");

        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        Assert.assertEquals(dispatcherArgument.getValue(), VIEW_LOGIN_FORM);
    }

    @Test
    public void getLogoutShouldRedirectHome() throws Exception {

        when(request.getServletPath()).thenReturn("/account/logout");
        servlet.doGet(request, response);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(response, only()).sendRedirect( argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue(), "/");
    }

    @Test
    public void getLogoutShouldRemoveUserAttrFromSession() throws Exception {

        when(request.getServletPath()).thenReturn("/account/logout");
        servlet.doGet(request, response);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(session, atLeastOnce()).removeAttribute( argumentCaptor.capture());
        Assert.assertTrue(argumentCaptor.getAllValues().contains("user"));
    }

    @Test
    public void getRegistrationShouldShowRegistrationForm() throws Exception {
        shouldShowView("/account/registration", VIEW_REGISTRATION_FORM);
    }

    @Test
    public void getRegistrationShouldSetSessionAttrs() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration");
        servlet.doGet(request, response);

        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session, times(1)).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertEquals(sessionAttrName.getValue(), "FID");

        verify(session, times(1)).removeAttribute("userDto");
    }

    @Test
    public void postWrongUserDataToRegistrationShouldReturnToRegistrationFormWithError() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration");

        when(request.getParameter("email")).thenReturn("email@mail.box");
        when(request.getParameter("username")).thenReturn("robert");
        when(request.getParameter("password")).thenReturn("123");
        when(request.getParameter("confirmPassword")).thenReturn("WRONG PASSWORD CONFIRM");
        when(request.getParameter("firstName")).thenReturn("Robert");
        when(request.getParameter("lastName")).thenReturn("Black");
        when(request.getParameter("role")).thenReturn(User.Role.TUTOR.getCaption());

        when(session.getAttribute("FID")).thenReturn("abc");
        when(request.getParameter("_FID")).thenReturn("abc");

        servlet.doPost(request, response);

        ArgumentCaptor<String> reqAttr = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute(reqAttr.capture(), any());
        Assert.assertTrue(reqAttr.getAllValues().contains("errors"));

        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        verify(dispatcher, only()).forward(request, response);
        Assert.assertEquals(dispatcherArgument.getValue(), VIEW_REGISTRATION_FORM);
    }

    @Test
    public void postCorrectUserDataToRegistrationShouldReturnToEmailConfirm() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration");

        when(request.getParameter("email")).thenReturn("email@mail.box");
        when(request.getParameter("username")).thenReturn("robert");
        when(request.getParameter("password")).thenReturn("123");
        when(request.getParameter("confirmPassword")).thenReturn("123");
        when(request.getParameter("firstName")).thenReturn("Robert");
        when(request.getParameter("lastName")).thenReturn("Black");
        when(request.getParameter("role")).thenReturn(User.Role.TUTOR.getCaption());

        when(session.getAttribute("FID")).thenReturn("abc");
        when(request.getParameter("_FID")).thenReturn("abc");

        servlet.doPost(request, response);

        ArgumentCaptor<String> attr = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute(attr.capture(), any());
        Assert.assertTrue(attr.getAllValues().contains("verifyEmail"));

        verify(session, atLeastOnce()).setAttribute(attr.capture(), any());
        Assert.assertTrue(attr.getAllValues().contains("verificationCode"));

        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        verify(dispatcher, only()).forward(request, response);
        Assert.assertEquals(dispatcherArgument.getValue(), VIEW_REGISTRATION_FORM);
    }

    @Test
    public void postWrongFidToRegistrationShouldShowPageNotFound() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration");

        when(session.getAttribute("FID")).thenReturn("abcd");
        when(request.getParameter("_FID")).thenReturn("abc");

        servlet.doPost(request, response);

        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        verify(dispatcher, only()).forward(request, response);
        Assert.assertEquals(dispatcherArgument.getValue(), VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void postWrongVerificationCodeToRegistrationShouldReturnToRegistrationForm() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration/confirm");

        when(session.getAttribute("FID")).thenReturn("abc");
        when(request.getParameter("_FID")).thenReturn("abc");

        when(request.getParameter("verificationCode")).thenReturn("123456");
        when(session.getAttribute("verificationCode")).thenReturn("WRONG");

        servlet.doPost(request, response);

        ArgumentCaptor<String> attr = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute(attr.capture(), any());
        Assert.assertTrue(attr.getAllValues().contains("verifyEmail"));

        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        verify(dispatcher, only()).forward(request, response);
        Assert.assertEquals(dispatcherArgument.getValue(), VIEW_REGISTRATION_FORM);
    }

    @Test
    public void postCorrectVerificationCodeToRegistrationShouldInsertUserToDB() throws Exception {

        UserDto u = postCorrectVerificationCodeToRegistration();

        ArgumentCaptor<User> attr = ArgumentCaptor.forClass(User.class);
        verify(userService, only()).insert(attr.capture());
        Assert.assertTrue( attr.getValue().getUsername().equals( u.getUsername() ) );
    }

    @Test
    public void postCorrectVerificationCodeToRegistrationShouldEncodePassword() throws Exception {

        UserDto u = postCorrectVerificationCodeToRegistration();

        ArgumentCaptor<User> attr = ArgumentCaptor.forClass(User.class);
        verify(userService, only()).insert(attr.capture());

        Assert.assertTrue( Cipher.validate(u.getPassword(), attr.getValue().getPassword()) );
    }

    @Test
    public void postCorrectVerificationCodeToRegistrationShouldRedirectHome() throws Exception {

        UserDto u = postCorrectVerificationCodeToRegistration();

        ArgumentCaptor<User> attr = ArgumentCaptor.forClass(User.class);
        verify(userService, only()).insert(attr.capture());

        Assert.assertTrue( Cipher.validate(u.getPassword(), attr.getValue().getPassword()) );

        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(arg.capture());
        Assert.assertEquals(arg.getValue(), "/");
    }


    public UserDto postCorrectVerificationCodeToRegistration() throws Exception {

        when(request.getServletPath()).thenReturn("/account/registration/confirm");

        when(session.getAttribute("FID")).thenReturn("abc");
        when(request.getParameter("_FID")).thenReturn("abc");

        when(request.getParameter("verificationCode")).thenReturn("123456");
        when(session.getAttribute("verificationCode")).thenReturn("123456");

        UserDto u = new UserDto();
        u.setFirstName("John");
        u.setLastName("Smith");
        u.setUsername("john");
        u.setEmail("john@mail.edu");
        u.setRole(User.Role.TUTOR.getCaption());
        u.setPassword("123");
        u.setConfirmPassword("123");

        when(session.getAttribute("userDto")).thenReturn(u);

        servlet.doPost(request, response);

        return u;
    }


    @Test
    public void getRecoveryShouldShowRecoveryForm() throws Exception {
        shouldShowView("/account/recovery", VIEW_PASSWORD_RECOVERY_FORM);
    }

    @Test
    public void getRecoveryShouldSetSessionAttrs() throws Exception {

        when(request.getServletPath()).thenReturn("/account/recovery");
        servlet.doGet(request, response);

        verify(session).removeAttribute("userDto");
        ArgumentCaptor<String> sessionAttrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sessionAttrVal = ArgumentCaptor.forClass(String.class);
        verify(session).setAttribute( sessionAttrName.capture(), sessionAttrVal.capture());
        Assert.assertTrue(sessionAttrName.getAllValues().contains("FID"));
    }

    @Test
    public void testAnonymGetMyProfileShouldShowPageNotFound() throws Exception {
        shouldShowView("/account/myprofile", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getMyProfileShouldShowMyProfileForm() throws Exception {
        when(session.getAttribute("user")).thenReturn(UserFactory.getAdmin());
        shouldShowView("/account/myprofile", VIEW_MY_PROFILE_FORM);
    }

    @Test
    public void getProfileShouldShowUserProfile() throws Exception {

        User user = UserFactory.getStudent();
        String username = user.getUsername();
        when(request.getPathInfo()).thenReturn("/" + username);
        when(userService.findOneByUsername(username)).thenReturn(user);

        shouldShowView("/account/profile", VIEW_PROFILE);
    }

    @Test
    public void getProfileShouldSetRequestAttrUserData() throws Exception {

        User user = UserFactory.getStudent();
        getProfile(user, user.getUsername());

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any(User.class));
        Assert.assertTrue(attrName.getAllValues().contains("userData"));
    }

    @Test
    public void getProfileTestRequestAttrUserData() throws Exception {

        User user = UserFactory.getStudent();
        getProfile(user, user.getUsername());

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> attrVal = ArgumentCaptor.forClass(User.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), attrVal.capture());
        int index = attrName.getAllValues().indexOf("userData");
        Assert.assertEquals(attrVal.getAllValues().get(index), user);
    }

    @Test
    public void getProfileIfWrongUsernameShouldShowPageNotFound() throws Exception {

        User user = UserFactory.getStudent();
        getProfile(null, "WRONG_USERNAME");

        ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
        verify(context).getRequestDispatcher( dispatcherArgument.capture());
        verify(dispatcher, only()).forward(request, response);
        Assert.assertEquals(dispatcherArgument.getValue(), VIEW_PAGE_NOT_FOUND);
    }

    private void getProfile(User user, String username) throws Exception {
        when(request.getServletPath()).thenReturn("/account/profile");
        when(request.getPathInfo()).thenReturn("/" + username);
        when(userService.findOneByUsername(username)).thenReturn(user);

        if(user != null) {
            switch (user.getRole()) {
                case STUDENT:
                    when(studentService.attachProfile(user)).thenReturn((Student) user);
                    break;
                case TUTOR:
                    when(tutorService.attachProfile(user)).thenReturn((Tutor) user);
                    break;
            }
        }

        servlet.doGet(request, response);
    }

    @Test
    public void getUsersIfAdminShouldShowUsers() throws Exception {

        List<User> users = Arrays.asList(UserFactory.getAdmin(), UserFactory.getTutor(), UserFactory.getStudent());
        getUsers(UserFactory.getAdmin(), users);

        shouldShowView("/account/users", VIEW_USER_LIST);
    }

    @Test
    public void getUsersIfAdminShouldSetRequestAttrUsers() throws Exception {

        List<User> users = Arrays.asList(UserFactory.getAdmin(), UserFactory.getTutor(), UserFactory.getStudent());
        getUsers(UserFactory.getAdmin(), users);
        when(request.getServletPath()).thenReturn("/account/users");
        servlet.doGet(request, response);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), any());
        Assert.assertTrue(attrName.getAllValues().contains("users"));
    }

    @Test
    public void getUsersIfAdminTestRequestAttrUsers() throws Exception {

        User[] users = new User[]{UserFactory.getAdmin(), UserFactory.getTutor(), UserFactory.getStudent()};

        getUsers(UserFactory.getAdmin(), Arrays.asList(users.clone()));
        when(request.getServletPath()).thenReturn("/account/users");
        servlet.doGet(request, response);

        ArgumentCaptor<String> attrName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> attrVal = ArgumentCaptor.forClass(User.class);
        verify(request, atLeastOnce()).setAttribute( attrName.capture(), attrVal.capture());
        int index = attrName.getAllValues().indexOf("users");
        List<User> attrUsers = (List<User>) attrVal.getAllValues().get(index);
        Assert.assertTrue(attrUsers != null && attrUsers.size() == users.length);

        for(User u : users) {
            Assert.assertTrue(attrUsers.contains(u));
        }
    }

    @Test
    public void getUsersIfAnonymShouldShowPageNotFound() throws Exception {
        List<User> users = Arrays.asList(UserFactory.getAdmin(), UserFactory.getTutor(), UserFactory.getStudent());
        getUsers(null, users);

        shouldShowView("/account/users", VIEW_PAGE_NOT_FOUND);
    }

    @Test
    public void getUsersIfNotAdminShouldShowPageNotFound() throws Exception {
        List<User> users = Arrays.asList(UserFactory.getAdmin(), UserFactory.getTutor(), UserFactory.getStudent());
        getUsers(UserFactory.getStudent(), users);

        shouldShowView("/account/users", VIEW_PAGE_NOT_FOUND);
    }

    public void getUsers(User user, List<User> users) {

        when(session.getAttribute("user")).thenReturn(user);
        when(userService.count()).thenReturn(3L);
        when(userService.findAll(anyLong(), anyLong())).thenReturn(users);
        when(tutorService.findOneById(2L)).thenReturn((Tutor) UserFactory.getById(2));
        when(studentService.findOneById(3L)).thenReturn((Student) UserFactory.getById(3));
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
