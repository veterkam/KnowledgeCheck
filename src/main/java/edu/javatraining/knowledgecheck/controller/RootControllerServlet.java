package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet acts as a page controller for the application,
 * handling all requests from the user.
 */
@WebServlet(urlPatterns = {"/"}, loadOnStartup = 1)
@Singleton
public class RootControllerServlet extends AbstractBaseControllerServlet {

    @Override
    public void init() throws ServletException {
        // RootControllerServlet start up first (loadOnStartup = 1)
        // that's why we MUST call super class init
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        pageNotFound(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();
        try {
            switch (action) {
                case "/":
                    forward(request, response,"/testing");
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
        }
    }
}
