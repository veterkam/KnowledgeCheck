package edu.javatraining.knowledgecheck.configure.provider;

import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;


public class UserDaoProvider implements Provider<UserDao> {

    @Override
    public UserDao get() {
        return new UserDaoJdbc();
    }
}
