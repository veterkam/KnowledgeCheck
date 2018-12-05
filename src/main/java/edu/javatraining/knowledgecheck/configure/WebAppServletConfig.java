package edu.javatraining.knowledgecheck.configure;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;
import edu.javatraining.knowledgecheck.configure.provider.UserDaoProvider;
import edu.javatraining.knowledgecheck.controller.AccountControllerServlet;
import edu.javatraining.knowledgecheck.controller.RootControllerServlet;
import edu.javatraining.knowledgecheck.controller.TestingControllerServlet;
import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.StudentDaoJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TutorDaoJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

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

                bind(UserDao.class).toProvider(UserDaoProvider.class).in(RequestScoped.class);
                bind(TutorDao.class).to(TutorDaoJdbc.class);
                bind(StudentDao.class).to(StudentDaoJdbc.class);
            }

            @Provides
            @Named("UserDao")
            UserDao provideUserDao() {
                return new UserDaoJdbc();
            }
        });
    }


}


