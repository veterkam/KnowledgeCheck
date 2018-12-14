package edu.javatraining.knowledgecheck.configure;

import com.google.inject.*;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.configure.provider.data.ConnectionPoolProvider;
import edu.javatraining.knowledgecheck.configure.provider.data.dao.*;
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
        return Guice.createInjector( new WebAppServletModule() );
    }


}


