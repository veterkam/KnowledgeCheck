package com.epam.javatraining.knowledgecheck.controller;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.dao.TestDao;
import com.epam.javatraining.knowledgecheck.model.entity.Test;
import com.epam.javatraining.knowledgecheck.service.Pagination;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/testboard",
        "/testboard/browse",
        "/testboard/edit",
        "/testboard/add",
        "/testboard/remove"
})
public class TestBoardControllerServlet extends AbstractBaseControllerServlet {

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
                case "/testboard":
                    showTestList(request, response);
                    break;
                case "/testboard/browse":
                    break;
                case "/testboard/edit":
                    break;
                case "/testboard/add":
                    break;
                case "/testboard/remove":
                    break;
                default:
                    pageNotFound(request, response);
                    break;
            }
        } catch (IOException | DAOException e) {
            logger.error(e.getMessage(), e);

            throw new ServletException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void showTestList(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {

        final int COUNT_TEST_ON_PAGE = 10;
        final int PAGINATION_LIMIT = 5;

        String pageNoStr = request.getParameter("pageNo");
        int pageNo;
        try {
            pageNo = Integer.parseInt(pageNoStr);
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        int offset = (pageNo - 1) * COUNT_TEST_ON_PAGE;

        TestDao dao = new TestDao(getConnectionPool());
        int count = dao.getTestCount();
        int pageCount = count / COUNT_TEST_ON_PAGE + 1;

        Pagination pagination = new Pagination(pageNo, pageCount, PAGINATION_LIMIT);

        List<Test> tests = dao.getListSingle(offset, COUNT_TEST_ON_PAGE);
        request.setAttribute("tests", tests);
        request.setAttribute("pagination", pagination);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(VIEW_TEST_BOARD);
        dispatcher.forward(request, response);
    }
}
