package com.epam.javatraining.knowledgecheck.service;

import com.epam.javatraining.knowledgecheck.model.entity.Subject;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class Presentation {
    public static final String DATE_DESCENDING = "Date descending";
    public static final String DATE_ASCENDING = "Date ascending";

    private List<Subject> subjectFilters;
    private String[] dateOrders = new String[] {DATE_DESCENDING, DATE_ASCENDING};
    private int filterBySubjectId;
    private String orderByDate;
    private HttpServletRequest request;

    public Presentation(HttpServletRequest request, List<Subject> subjectFilters) {
        this.request = request;
        this.subjectFilters = subjectFilters;

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

    public List<Subject> getSubjectFilters() {
        return subjectFilters;
    }

    public void setSubjectFilters(List<Subject> subjectFilters) {
        this.subjectFilters = subjectFilters;
    }

    public String[] getDateOrders() {
        return dateOrders;
    }

    public void setDateOrders(String[] dateOrders) {
        this.dateOrders = dateOrders;
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
        request.setAttribute("subjects", subjectFilters);
        request.setAttribute("orders", dateOrders);
        request.getSession().setAttribute("subjectFilter", String.valueOf(filterBySubjectId));
        request.getSession().setAttribute("dateOrder", orderByDate);
    }
}
