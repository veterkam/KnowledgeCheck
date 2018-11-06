package com.epam.javatraining.knowledgecheck.model.entity;

import java.util.Map;

public class TestingResults {
    private int studentId;
    private long testId;
    Map<Long, Boolean> answerResults;

    public TestingResults(int studentId, long testId, Map<Long, Boolean> answerResults) {
        this.studentId = studentId;
        this.testId = testId;
        this.answerResults = answerResults;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public Map<Long, Boolean> getAnswerResults() {
        return answerResults;
    }

    public void setAnswerResults(Map<Long, Boolean> answerResults) {
        this.answerResults = answerResults;
    }
}
