package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.TestDao;
import edu.javatraining.knowledgecheck.service.TestService;
import edu.javatraining.knowledgecheck.service.impl.TestServiceImpl;

public class TestServiceProvider implements Provider<TestService> {

    @Inject
    private TestDao dao;

    @Override
    public TestService get() {
        return new TestServiceImpl(dao);
    }
}
