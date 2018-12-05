package edu.javatraining.knowledgecheck.data.dao;


import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.User;

public interface StudentDao extends BasicDao<Student, Long> {
    Student attachProfile(User user);
    Student findOneByUsername(String username);
}
