package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.model.dao.TestDao;
import com.epam.javatraining.knowledgecheck.model.dao.UserDao;
import com.epam.javatraining.knowledgecheck.model.entity.Test;
import com.epam.javatraining.knowledgecheck.model.entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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

        String action = request.getServletPath();
        try {
            switch (action) {
                case "/":
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/testboard");
                    dispatcher.forward(request, response);
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
