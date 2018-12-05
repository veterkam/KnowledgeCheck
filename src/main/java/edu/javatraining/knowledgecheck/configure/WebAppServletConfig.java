package edu.javatraining.knowledgecheck.configure;

import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import javax.servlet.annotation.WebListener;

@WebListener
public class WebAppServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                //serve("*.html").with(TestingControllerServlet.class);
                //bind(UserDao.class).to(UserDaoJdbc.class);
            }
        });
    }


}


