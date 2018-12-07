package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.SubjectDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.SubjectDaoJdbc;

public class SubjectDaoProvider implements Provider<SubjectDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public SubjectDao get() {
        return new SubjectDaoJdbc(pool);
    }
}
