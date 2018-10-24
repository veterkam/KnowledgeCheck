package com.epam.javatraining.knowledgecheck.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Tutor extends User {
    private String position;
    private String scientificDegree;
    private String academicTitle;
    private List<String> subjects = new ArrayList<>();

    public static Tutor getInstance() {
        return new Tutor(new User("login", "password"));
    }

    public Tutor() {

    }

    public Tutor(User user) {
        super(user);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getScientificDegree() {
        return scientificDegree;
    }

    public void setScientificDegree(String scientificDegree) {
        this.scientificDegree = scientificDegree;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }
}
