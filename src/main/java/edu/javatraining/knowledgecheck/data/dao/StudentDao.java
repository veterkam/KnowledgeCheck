package edu.javatraining.knowledgecheck.data.dao;


import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;

import java.util.List;

public interface StudentDao {
    Student attachProfile(User user);
    Student findOneByUsername(String username);

    Long insert(Student student);
    boolean update(Student student);
    Long save(Student student);
    boolean delete(Student student);
    boolean deleteById(Long id);
    Student findOne(Student student);
    Student findOneById(Long id);
    List<Student> findAllStudents();
    List<Student> findAllStudents(Long offset, Long count);
    Long count();
}
