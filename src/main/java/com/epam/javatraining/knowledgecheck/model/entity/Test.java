package com.epam.javatraining.knowledgecheck.model.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//java.util.Date updateTime = new Date();
//        Object param = new java.sql.Timestamp(updateTime.getTime());
//// The JDBC driver knows what to do with a java.sql type:
//        preparedStatement.setObject(param);
//
public class Test {

    private long id;
    private Subject subject;
    private Tutor tutor;
    private String title;
    private String description;
    private Timestamp updateTime;
    private List<Question> questions = new ArrayList<>();

    public Test() {
    }

    public Test(long id) {
        this.id = id;
    }

    public Test(long id, Subject subject, Tutor tutor, String title, String description, Timestamp updateTime) {
        this.id = id;
        this.subject = subject;
        this.tutor = tutor;
        this.description = description;
        this.title = title;
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        this.questions = questions;
    }

    public int getSubjectId() {
        return (subject == null) ? 0 : subject.getId();
    }

    public int getTutorId() {
        return (tutor == null) ? 0 : tutor.getId();
    }

}
