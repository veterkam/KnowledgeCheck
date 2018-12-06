package edu.javatraining.knowledgecheck.service;

import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.User;

import java.util.List;

public interface StudentService {

    Long insert(Student student);
    boolean update(Student student);
    Long save(Student student);
    boolean delete(Student student);
    boolean deleteById(Long id);
    Student findOne(Student student);
    Student findOneById(Long id);
    List<Student> findAll();
    List<Student> findAll(Long offset, Long count);
    Long count();
    Student findOneByUsername(String username);
    Student attachProfile(User user);
}
