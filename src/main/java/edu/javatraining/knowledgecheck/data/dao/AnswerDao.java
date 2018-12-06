package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.domain.Answer;

public interface AnswerDao extends BasicDao<Answer, Long>  {
    Answer[] findByQuestionId(Long questionId);
}
