package edu.javatraining.knowledgecheck.tools;

import edu.javatraining.knowledgecheck.domain.Subject;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class Presentation {
    public static final String DATE_DESCENDING = "app.presentation.date_descending";
    public static final String DATE_ASCENDING = "app.presentation.date_ascending";

    private List<Subject> subjects;
    private String[] orders = new String[] {DATE_DESCENDING, DATE_ASCENDING};
    private Long subjectId;
    private String dateOrder;
    private HttpServletRequest request;

    public Presentation() {

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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        if(Strings.isBlank(dateOrder)) {
            this.dateOrder = DATE_DESCENDING;
        } else {
            this.dateOrder = dateOrder;
        }
    }
}
