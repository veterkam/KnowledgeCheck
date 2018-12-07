package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TestDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TestDaoJdbc;

public class TestDaoProvider implements Provider<TestDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public TestDao get() {
        return new TestDaoJdbc(pool);
    }
}
