package edu.javatraining.knowledgecheck.service;

import edu.javatraining.knowledgecheck.domain.TestingResults;

import java.util.List;

public interface TestingResultsService {
    boolean insert(TestingResults testingResults);
    boolean update(TestingResults testingResults);
    TestingResults find(Long studenId, Long testId);
    List<TestingResults> find(Long testId);
}
