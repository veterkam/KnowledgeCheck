package edu.javatraining.knowledgecheck.configure.provider.data.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.AnswerDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.AnswerDaoJdbc;

public class AnswerDaoProvider implements Provider<AnswerDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public AnswerDao get() {
        return new AnswerDaoJdbc(pool);
    }
}
