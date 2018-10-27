package com.epam.javatraining.knowledgecheck.model.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//java.util.Date date = new Date();
//        Object param = new java.sql.Timestamp(date.getTime());
//// The JDBC driver knows what to do with a java.sql type:
//        preparedStatement.setObject(param);
//
public class Test {
    private long id;
    private Subject subject;
    private Tutor tutor;
    private String description;
    private Timestamp date;
    private List<Question> questions = new ArrayList<>();

    public Test() {
    }

    public Test(long id) {
        this.id = id;
    }

    public Test(long id, Subject subject, Tutor tutor, String description, Timestamp date) {
        this.id = id;
        this.subject = subject;
        this.tutor = tutor;
        this.description = description;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
