package edu.javatraining.knowledgecheck.service;

import edu.javatraining.knowledgecheck.domain.User;

import java.util.List;

public interface UserService {

    Long insert(User user);
    boolean update(User user);
    Long save(User user);
    boolean delete(User user);
    boolean deleteById(Long id);
    User findOne(User user);
    User findOneById(Long id);
    List<User> findAll();
    List<User> findAll(Long offset, Long count);
    Long count();
    User findOneByUsername(String username);
    boolean updatePassword(User user);
}
