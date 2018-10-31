package com.epam.javatraining.knowledgecheck.service;

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
    private int filterBySubjectId;
    private String orderByDate;
    private HttpServletRequest request;

    public Presentation(HttpServletRequest request, List<Subject> subjects) {
        this.request = request;
        this.subjects = subjects;

        HttpSession session = request.getSession();
        // Filter on subject id
        String subjectFilterStr = request.getParameter("subjectFilter");
        try {
            filterBySubjectId = Integer.parseInt(subjectFilterStr);
        } catch (NumberFormatException e) {
            subjectFilterStr = (String) session.getAttribute("subjectFilter");
            filterBySubjectId = (Strings.isBlank(subjectFilterStr)) ? 0 : Integer.parseInt(subjectFilterStr);
        }

        // Order by date
        orderByDate = request.getParameter("dateOrder");
        if(Strings.isBlank(orderByDate)) {
            orderByDate = (String) session.getAttribute("dateOrder");
            if(Strings.isBlank(orderByDate)) {
                orderByDate = DATE_DESCENDING;
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

    public int getFilterBySubjectId() {
        return filterBySubjectId;
    }

    public void setFilterBySubjectId(int filterBySubjectId) {
        this.filterBySubjectId = filterBySubjectId;
    }

    public String getOrderByDate() {
        return orderByDate;
    }

    public void setOrderByDate(String orderByDate) {
        this.orderByDate = orderByDate;
    }

    public void store() {
        request.setAttribute("subjects", subjects);
        request.setAttribute("orders", orders);
        request.getSession().setAttribute("subjectFilter", String.valueOf(filterBySubjectId));
        request.getSession().setAttribute("dateOrder", orderByDate);
    }
}
