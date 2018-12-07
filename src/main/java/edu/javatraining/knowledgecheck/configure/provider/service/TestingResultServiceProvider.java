package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.TestingResultDao;
import edu.javatraining.knowledgecheck.service.TestingResultService;
import edu.javatraining.knowledgecheck.service.impl.TestingResultServiceImpl;

public class TestingResultServiceProvider implements Provider<TestingResultService> {

    @Inject
    private TestingResultDao dao;

    @Override
    public TestingResultService get() {
        return new TestingResultServiceImpl(dao);
    }
}
