package edu.javatraining.knowledgecheck.configure.provider.data.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TestingResultsDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TestingResultsDaoJdbc;

public class TestingResultDaoProvider implements Provider<TestingResultsDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public TestingResultsDao get() {
        return new TestingResultsDaoJdbc(pool);
    }
}
