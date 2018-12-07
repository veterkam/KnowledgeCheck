package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.QuestionDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.QuestionDaoJdbc;

public class QuestionDaoProvider implements Provider<QuestionDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public QuestionDao get() {
        return new QuestionDaoJdbc(pool);
    }
}
