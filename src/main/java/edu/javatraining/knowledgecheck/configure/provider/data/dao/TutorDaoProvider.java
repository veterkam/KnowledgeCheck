package edu.javatraining.knowledgecheck.configure.provider.data.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TutorDaoJdbc;

public class TutorDaoProvider implements Provider<TutorDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public TutorDao get() {
        return new TutorDaoJdbc(pool);
    }
}
