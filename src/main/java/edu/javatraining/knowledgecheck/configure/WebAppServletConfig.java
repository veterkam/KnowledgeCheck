package edu.javatraining.knowledgecheck.configure;

import com.google.inject.*;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.configure.provider.ConnectionPoolProvider;
import edu.javatraining.knowledgecheck.configure.provider.dao.UserDaoProvider;
import edu.javatraining.knowledgecheck.configure.provider.dao.TutorDaoProvider;
import edu.javatraining.knowledgecheck.configure.provider.dao.StudentDaoProvider;
import edu.javatraining.knowledgecheck.configure.provider.service.StudentServiceProvider;
import edu.javatraining.knowledgecheck.configure.provider.service.TutorServiceProvider;
import edu.javatraining.knowledgecheck.configure.provider.service.UserServiceProvider;
import edu.javatraining.knowledgecheck.controller.AccountControllerServlet;
import edu.javatraining.knowledgecheck.controller.RootControllerServlet;
import edu.javatraining.knowledgecheck.controller.TestingControllerServlet;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.javatraining.knowledgecheck.service.StudentService;
import edu.javatraining.knowledgecheck.service.TutorService;
import edu.javatraining.knowledgecheck.service.UserService;

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

                serve("/").with(RootControllerServlet.class);
                serve(
                        "/account/login",
                        "/account/logout",
                        "/account/register",
                        "/account/register/back",
                        "/account/register/confirm",
                        "/account/recovery",
                        "/account/recovery/back",
                        "/account/recovery/confirm",
                        "/account/myprofile",
                        "/account/myprofile/back",
                        "/account/myprofile/confirm",
                        "/account/users",
                        "/account/users/remove",
                        "/account/profile"
                        ).with(AccountControllerServlet.class);
                serve(
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
                ).with(TestingControllerServlet.class);

                bind(ConnectionPool.class).toProvider(ConnectionPoolProvider.class).in(Singleton.class);

                bind(UserDao.class).toProvider(UserDaoProvider.class).in(RequestScoped.class);
                bind(TutorDao.class).toProvider(TutorDaoProvider.class).in(RequestScoped.class);
                bind(StudentDao.class).toProvider(StudentDaoProvider.class).in(RequestScoped.class);

                bind(UserService.class).toProvider(UserServiceProvider.class).in(Singleton.class);
                bind(TutorService.class).toProvider(TutorServiceProvider.class).in(Singleton.class);
                bind(StudentService.class).toProvider(StudentServiceProvider.class).in(Singleton.class);
            }

        });
    }


}


