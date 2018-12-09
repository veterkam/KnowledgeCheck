package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.TestingResultsDao;
import edu.javatraining.knowledgecheck.domain.TestingResults;
import edu.javatraining.knowledgecheck.service.TestingResultsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TestingResultsServiceImpl implements TestingResultsService {

    private static Logger logger = LogManager.getLogger("Service");

    private TestingResultsDao dao;

    public TestingResultsServiceImpl(TestingResultsDao dao) {
        this.dao = dao;

        logger.trace("TestingResultsServiceImpl constructor: subjectDao " + this.dao);
    }

    @Override
    public boolean insert(TestingResults testingResults) {
        return dao.insert(testingResults);
    }

    @Override
    public boolean update(TestingResults testingResults) {
        return dao.update(testingResults);
    }

    @Override
    public TestingResults find(Long studenId, Long testId) {
        return dao.find(studenId, testId);
    }

    @Override
    public List<TestingResults> find(Long testId) {
        return dao.find(testId);
    }
}
