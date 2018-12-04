package com.epam.javatraining.knowledgecheck.data.dao;


import com.epam.javatraining.knowledgecheck.domain.Student;
import com.epam.javatraining.knowledgecheck.domain.User;

public interface StudentDao extends BasicDao<Student, Long> {
    Student attachProfile(User user);
    Student findOneByUsername(String username);
}
