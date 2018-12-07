package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.AnswerDao;
import edu.javatraining.knowledgecheck.domain.Answer;
import edu.javatraining.knowledgecheck.service.AnswerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AnswerServiceImpl implements AnswerService {

    private static Logger logger = LogManager.getLogger("Service");

    private AnswerDao dao;

    public AnswerServiceImpl(AnswerDao dao) {
        this.dao = dao;

        logger.trace("AnswerServiceImpl constructor: dao " + this.dao);
    }

    @Override
    public List<Answer> findByQuestionId(Long questionId) {
        return dao.findByQuestionId(questionId);
    }

    @Override
    public Long insert(Answer answer) {
        return dao.insert(answer);
    }

    @Override
    public boolean update(Answer answer) {
        return dao.update(answer);
    }

    @Override
    public Long save(Answer answer) {
        return dao.save(answer);
    }

    @Override
    public boolean delete(Answer answer) {
        return dao.delete(answer);
    }

    @Override
    public boolean deleteById(Long id) {
        return dao.deleteById(id);
    }

    @Override
    public Answer findOne(Answer answer) {
        return dao.findOne(answer);
    }

    @Override
    public Answer findOneById(Long id) {
        return dao.findOneById(id);
    }

    @Override
    public List<Answer> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Answer> findAll(Long offset, Long count) {
        return dao.findAll(offset, count);
    }

    @Override
    public Long count() {
        return dao.count();
    }
}
