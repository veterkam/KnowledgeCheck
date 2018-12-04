package com.epam.javatraining.knowledgecheck.domain;

public class Tutor extends User {
    private String position;
    private String scientificDegree;
    private String academicTitle;

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
