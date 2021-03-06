package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.domain.Subject;

import java.util.List;

public interface SubjectDao {

    Long insert(Subject subject);

    boolean update(Subject subject);

    Long save(Subject subject);

    boolean delete(Subject subject);

    boolean deleteById(Long id);

    Subject findOne(Subject subject);

    Subject findOneById(Long id);

    List<Subject> findAll();

    List<Subject> findAll(Long offset, Long count);

    Long count();
}