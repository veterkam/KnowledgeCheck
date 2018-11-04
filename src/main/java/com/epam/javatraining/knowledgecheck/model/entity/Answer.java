package com.epam.javatraining.knowledgecheck.model.entity;

public class Answer {
    private long id = -1;
    private long questionId;
    private String description;
    private boolean correct;

    public Answer(long id) {
        this.id = id;
    }

    public Answer() {

    }

    public Answer(long id, long questionId, String description, boolean correct) {
        this.id = id;
        this.questionId = questionId;
        this.description = description;
        this.correct = correct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
