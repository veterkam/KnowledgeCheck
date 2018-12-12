package edu.javatraining.knowledgecheck.configure;

import com.google.inject.*;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.configure.provider.ConnectionPoolProvider;
import edu.javatraining.knowledgecheck.configure.provider.dao.*;
import edu.javatraining.knowledgecheck.configure.provider.service.*;
import edu.javatraining.knowledgecheck.controller.AccountControllerServlet;
import edu.javatraining.knowledgecheck.controller.EncodeFilter;
import edu.javatraining.knowledgecheck.controller.SessionLocaleFilter;
import edu.javatraining.knowledgecheck.controller.TestingControllerServlet;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.*;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.javatraining.knowledgecheck.service.*;

import javax.servlet.annotation.WebListener;

@WebListener
public class WebAppServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(

        new ServletModule() {

            @Override
            protected void configureServlets() {
                super.configureServlets();

                filter("/*").through(EncodeFilter.class);
                filter("/*").through(SessionLocaleFilter.class);

                serve(
                        "/account/login",
                        "/account/logout",
                        "/account/registration",
                        "/account/registration/back",
                        "/account/registration/confirm",
                        "/account/recovery",
                        "/account/recovery/back",
                        "/account/recovery/confirm",
                        "/account/myprofile",
                        "/account/myprofile/back",
                        "/account/myprofile/confirm",
                        "/account/users",
                        "/account/users/verify",
                        "/account/users/remove"
                        ).with(AccountControllerServlet.class);
                // Add URL /account/profile/{username}
                serveRegex("/account/profile/.+").with(AccountControllerServlet.class);

                serve("/",
                        "/testing",
                        "/testing/mytests",
                        "/testing/edit",
                        "/testing/remove",
                        "/testing/test",
                        "/testing/test/result",
                        "/testing/studentsresults",
                        "/testing/teststatistics",
                        "/testing/subjects"
                ).with(TestingControllerServlet.class);



                bind(ConnectionPool.class).toProvider(ConnectionPoolProvider.class).in(Singleton.class);

                bind(UserDao.class).toProvider(UserDaoProvider.class).in(RequestScoped.class);
                bind(TutorDao.class).toProvider(TutorDaoProvider.class).in(RequestScoped.class);
                bind(StudentDao.class).toProvider(StudentDaoProvider.class).in(RequestScoped.class);

                bind(AnswerDao.class).toProvider(AnswerDaoProvider.class).in(RequestScoped.class);
                bind(QuestionDao.class).toProvider(QuestionDaoProvider.class).in(RequestScoped.class);
                bind(SubjectDao.class).toProvider(SubjectDaoProvider.class).in(RequestScoped.class);
                bind(TestDao.class).toProvider(TestDaoProvider.class).in(RequestScoped.class);
                bind(TestingResultsDao.class).toProvider(TestingResultDaoProvider.class).in(RequestScoped.class);

                bind(UserService.class).toProvider(UserServiceProvider.class).in(RequestScoped.class);
                bind(TutorService.class).toProvider(TutorServiceProvider.class).in(RequestScoped.class);
                bind(StudentService.class).toProvider(StudentServiceProvider.class).in(RequestScoped.class);

                bind(AnswerService.class).toProvider(AnswerServiceProvider.class).in(RequestScoped.class);
                bind(QuestionService.class).toProvider(QuestionServiceProvider.class).in(RequestScoped.class);
                bind(SubjectService.class).toProvider(SubjectServiceProvider.class).in(RequestScoped.class);
                bind(TestService.class).toProvider(TestServiceProvider.class).in(RequestScoped.class);
                bind(TestingResultsService.class).toProvider(TestingResultServiceProvider.class).in(RequestScoped.class);
            }

        });
    }


}


