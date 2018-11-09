package com.epam.javatraining.knowledgecheck.model.entity;

import java.util.Map;

public class TestingResults {
    private int studentId;
    private long testId;
    private Student student = null;
    // key = question id, value = correct/incorrect answer
    Map<Long, Boolean> answerResults;

    public TestingResults(int studentId, long testId, Map<Long, Boolean> answerResults) {
        this.studentId = studentId;
        this.testId = testId;
        this.answerResults = answerResults;
    }

    public int getScore() {

        if( answerResults.isEmpty() ) {
            return -1;
        }

        int correctCount = 0;
        for(Long id : answerResults.keySet()) {
            if(answerResults.get(id)) {
                correctCount++;
            }
        }

        return Math.round(100.0f * ((float) correctCount) / ((float) answerResults.size()) );
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student.getId();
    }
}
