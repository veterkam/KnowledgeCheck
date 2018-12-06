package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.service.UserService;
import edu.javatraining.knowledgecheck.service.impl.UserServiceImpl;

public class UserServiceProvider implements Provider<UserService> {

    @Inject
    private UserDao dao;

    @Override
    public UserService get() {
        return new UserServiceImpl(dao);
    }
}
