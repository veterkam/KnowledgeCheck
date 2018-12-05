package edu.javatraining.knowledgecheck.data.dao;


import edu.javatraining.knowledgecheck.domain.User;

public interface UserDao{

    Long insert(User entity);
    boolean update(User entity);
    Long save(User entity);
    boolean delete(User entity);
    boolean deleteById(Long id);
    User findOne(User entity);
    User findOneById(Long id);
    User[] findAll();
    User[] findAll(Long offset, Long count);
    Long count();
    User findOneByUsername(String username);
    boolean updatePassword(User user);
}
