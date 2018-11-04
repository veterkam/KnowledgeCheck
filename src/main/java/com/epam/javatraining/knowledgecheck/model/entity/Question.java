package com.epam.javatraining.knowledgecheck.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private long id = -1;
    private long testId;
    private String description;
    private List<Answer> answers = new ArrayList<>();

    public Question(long id, long testId, String description) {
        this.id = id;
        this.testId = testId;
        this.description = description;
    }

    public Question(long id) {
        this.id = id;
    }

    public Question() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers.clear();
        this.answers = answers;
    }
}
