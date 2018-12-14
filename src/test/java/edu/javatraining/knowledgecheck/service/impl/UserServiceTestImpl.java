package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceTestImpl implements UserService {

    private static Logger logger = LogManager.getLogger("Service");

    private Map<Long, User> users = new HashMap<>();
    private Long index = 0L;

    @Override
    public Long insert(User user) {
        users.put( index, user);
        user.setId(index);
        index++;
        return user.getId();
    }

    @Override
    public boolean update(User user) {
        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return true;
        }
        return false;
    }

    @Override
    public Long save(User user) {
        if(!update(user)) {
            insert(user);
        }
        return user.getId();
    }

    @Override
    public boolean delete(User user) {
        deleteById(user.getId());
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        users.remove(id);
        return true;
    }

    @Override
    public User findOne(User user) {
        User u = findOneById(user.getId());
        if(u == null) {
            u = findOneByUsername(user.getUsername());
        }
        return u;
    }

    @Override
    public User findOneById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        List<User> all = new ArrayList<>();
        all.addAll(users.values());
        return all;
    }

    @Override
    public List<User> findAll(Long offset, Long count) {
        return findAll().subList(offset.intValue(), count.intValue());
    }

    @Override
    public Long count() {
        return (long) users.size();
    }

    @Override
    public User findOneByUsername(String username) {
        for(User u : users.values() ) {
            if(u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean updatePassword(User user) {
        User u = users.get(user.getId());
        if(u != null) {
            u.setPassword(user.getPassword());
            return true;
        }
        return false;
    }
}
