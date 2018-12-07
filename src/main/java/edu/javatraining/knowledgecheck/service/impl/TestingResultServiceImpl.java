package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.TestingResultDao;
import edu.javatraining.knowledgecheck.domain.TestingResults;
import edu.javatraining.knowledgecheck.service.TestingResultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TestingResultServiceImpl implements TestingResultService {

    private static Logger logger = LogManager.getLogger("Service");

    private TestingResultDao dao;

    public TestingResultServiceImpl(TestingResultDao dao) {
        this.dao = dao;

        logger.trace("TestingResultServiceImpl constructor: subjectDao " + this.dao);
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
