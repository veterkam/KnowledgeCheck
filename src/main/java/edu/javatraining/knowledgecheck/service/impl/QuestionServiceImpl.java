package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.QuestionDao;
import edu.javatraining.knowledgecheck.domain.Question;
import edu.javatraining.knowledgecheck.service.QuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private static Logger logger = LogManager.getLogger("Service");

    private QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;

        logger.trace("QuestionServiceImpl constructor: dao " + this.dao);
    }

    @Override
    public Long insertComplex(Question question) {
        return dao.insertComplex(question);
    }

    @Override
    public Long insertPlain(Question question) {
        return dao.insertPlain(question);
    }

    @Override
    public List<Question> findComplexAll(Long testId) {
        return dao.findComplexAll(testId);
    }

    @Override
    public List<Question> findPlainAll(Long testId) {
        return dao.findPlainAll(testId);
    }

    @Override
    public boolean delete(Question question) {
        return dao.delete(question);
    }

    @Override
    public boolean updateComplex(Question question) {
        return dao.updateComplex(question);
    }

    @Override
    public boolean updatePlain(Question question) {
        return dao.updatePlain(question);
    }

    @Override
    public Question findComplexById(Long id) {
        return dao.findComplexById(id);
    }

    @Override
    public Question findPlainById(Long id) {
        return dao.findPlainById(id);
    }
}
