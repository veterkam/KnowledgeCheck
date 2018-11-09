package com.epam.javatraining.knowledgecheck.service;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.dao.SubjectDao;
import com.epam.javatraining.knowledgecheck.model.entity.Subject;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class Presentation {
    public static final String DATE_DESCENDING = "Date descending";
    public static final String DATE_ASCENDING = "Date ascending";

    private List<Subject> subjects;
    private String[] orders = new String[] {DATE_DESCENDING, DATE_ASCENDING};
    private int subjectId;
    private String dateOrder;
    private HttpServletRequest request;

    public Presentation(HttpServletRequest request, ConnectionPool pool)
        throws DAOException {
        this.request = request;

        // Read subject list for subject filter
        SubjectDao subjectDao = new SubjectDao(pool);
        this.subjects = subjects = subjectDao.getList();

        HttpSession session = request.getSession();
        // Filter on subject id
        String strSubjectId = request.getParameter("presentationSubjectId");
        try {
            subjectId = Integer.parseInt(strSubjectId);
        } catch (NumberFormatException e) {
            strSubjectId = (String) session.getAttribute("presentationSubjectId");
            subjectId = (Strings.isBlank(strSubjectId)) ? 0 : Integer.parseInt(strSubjectId);
        }

        // Order by date
        dateOrder = request.getParameter("presentationDateOrder");
        if(Strings.isBlank(dateOrder)) {
            dateOrder = (String) session.getAttribute("presentationDateOrder");
            if(Strings.isBlank(dateOrder)) {
                dateOrder = DATE_DESCENDING;
            }
        }
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public String[] getOrders() {
        return orders;
    }

    public void setOrders(String[] orders) {
        this.orders = orders;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public void store() {
        // Data for short period store in request
        request.setAttribute("presentationSubjects", subjects);
        request.setAttribute("presentationOrders", orders);
        // Data for long period store in session
        request.getSession().setAttribute("presentationSubjectId", String.valueOf(subjectId));
        request.getSession().setAttribute("presentationDateOrder", dateOrder);
    }
}
