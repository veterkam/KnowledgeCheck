package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TestingResultDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TestingResultDaoJdbc;

public class TestingResultDaoProvider implements Provider<TestingResultDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public TestingResultDao get() {
        return new TestingResultDaoJdbc(pool);
    }
}
