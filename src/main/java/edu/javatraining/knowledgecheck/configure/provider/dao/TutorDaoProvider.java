package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TutorDaoJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;

public class TutorDaoProvider implements Provider<TutorDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public TutorDao get() {
        return new TutorDaoJdbc(pool);
    }
}
