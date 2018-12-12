package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger("Service");

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Long insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user);
    }

    @Override
    public Long save(User user) {
        return userDao.save(user);
    }

    @Override
    public boolean delete(User user) {
        return userDao.delete(user);
    }

    @Override
    public boolean deleteById(Long id) {
        return userDao.deleteById(id);
    }

    @Override
    public User findOne(User user) {
        return userDao.findOne(user);
    }

    @Override
    public User findOneById(Long id) {
        return userDao.findOneById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAllUsers();
    }

    @Override
    public List<User> findAll(Long offset, Long count) {
        return userDao.findAllUsers(offset, count);
    }

    @Override
    public Long count() {
        return userDao.count();
    }

    @Override
    public User findOneByUsername(String username) {
        return userDao.findOneByUsername(username);
    }

    @Override
    public boolean updatePassword(User user) {
        return userDao.updatePassword(user);
    }
}
