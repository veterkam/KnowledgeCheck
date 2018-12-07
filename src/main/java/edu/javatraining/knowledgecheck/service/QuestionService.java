package edu.javatraining.knowledgecheck.service;

import edu.javatraining.knowledgecheck.domain.Question;

import java.util.List;

public interface QuestionService {

    Long insertComplex(Question question);
    Long insertPlain(Question question);
    List<Question> findComplexAll(Long testId);
    List<Question> findPlainAll(Long testId);
    boolean delete(Question question);
    boolean updateComplex(Question question);
    boolean updatePlain(Question question);
    Question findComplexById(Long id);
    Question findPlainById(Long id);
}