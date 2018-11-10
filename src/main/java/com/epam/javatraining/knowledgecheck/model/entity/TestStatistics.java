package com.epam.javatraining.knowledgecheck.model.entity;

import java.util.HashMap;
import java.util.Map;

public class TestStatistics {
    int studentCount;
    Map<Long, Integer> scores;

    public TestStatistics() {}

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public Map<Long, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<Long, Integer> scores) {
        this.scores = scores;
    }
}
