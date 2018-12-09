package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.TestingResultsDao;
import edu.javatraining.knowledgecheck.service.TestingResultsService;
import edu.javatraining.knowledgecheck.service.impl.TestingResultsServiceImpl;

public class TestingResultServiceProvider implements Provider<TestingResultsService> {

    @Inject
    private TestingResultsDao dao;

    @Override
    public TestingResultsService get() {
        return new TestingResultsServiceImpl(dao);
    }
}
