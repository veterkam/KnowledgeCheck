package edu.javatraining.knowledgecheck.domain;

import java.util.Map;

public class TestingResults {
    private Long studentId;
    private Long testId;
    private Student student = null;
    // key = question id, value = correct/incorrect answer
    Map<Long, Boolean> answerResults;

    public TestingResults(Long studentId, Long testId, Map<Long, Boolean> answerResults) {
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



    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
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
