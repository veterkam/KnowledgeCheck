package com.epam.javatraining.knowledgecheck.domain;

import org.apache.logging.log4j.util.Strings;

public class Subject {
    private int id;
    private String name;

    public Subject(){

    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIncorrect() {
        return  Strings.isBlank(name);
    }

    public boolean isCorrect() {
        return !isIncorrect();
    }
}
