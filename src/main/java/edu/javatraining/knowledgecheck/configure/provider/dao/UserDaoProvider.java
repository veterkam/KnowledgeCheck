package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;


public class UserDaoProvider implements Provider<UserDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public UserDao get() {
        return new UserDaoJdbc(pool);
    }
}
