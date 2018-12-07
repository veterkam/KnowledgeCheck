package edu.javatraining.knowledgecheck.service;

import edu.javatraining.knowledgecheck.domain.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> findByQuestionId(Long questionId);    
    Long insert(Answer answer);
    boolean update(Answer answer);
    Long save(Answer answer);
    boolean delete(Answer answer);
    boolean deleteById(Long id);
    Answer findOne(Answer answer);
    Answer findOneById(Long id);
    List<Answer> findAll();
    List<Answer> findAll(Long offset, Long count);
    Long count();
}
