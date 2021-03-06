package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.domain.Question;

import java.util.List;

public interface QuestionDao{

    Long insertComplex(Question question);
    Long insertPlain(Question question);
    List<Question> findComplexAll(Long testId);
    List<Question> findPlainAll(Long testId);
    List<Question> findPlainAll();
    Long count();
    boolean delete(Question question);
    boolean deleteById(Long id);
    boolean updateComplex(Question question);
    boolean updatePlain(Question question);
    Question findComplexById(Long id);
    Question findPlainById(Long id);
    Long saveComplex(Question question);
    Long savePlain(Question question);
}