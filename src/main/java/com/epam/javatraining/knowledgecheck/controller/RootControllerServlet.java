package com.epam.javatraining.knowledgecheck.controller;

import javax.servlet.RequestDispatcher;
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
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getServletPath();
        try {
            switch (action) {
                case "/":
                    home(request, response);
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e);
        }
    }

    private void home(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/Home.jsp");
        dispatcher.forward(request, response);
    }

    private void pageNotFound(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/PageNotFound.jsp");
        dispatcher.forward(request, response);
    }
}
