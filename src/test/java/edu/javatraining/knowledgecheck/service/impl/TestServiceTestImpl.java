package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.TestDao;
import edu.javatraining.knowledgecheck.domain.Test;
import edu.javatraining.knowledgecheck.service.TestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class TestServiceTestImpl implements TestService {

    private static Logger logger = LogManager.getLogger("Service");

    private TestDao dao;

    public TestServiceTestImpl(TestDao dao) {
        this.dao = dao;
    }

    @Override
    public Long insertComplex(Test test) {
        return dao.insertComplex(test);
    }

    @Override
    public Long insertPlain(Test test) {
        return dao.insertPlain(test);
    }

    @Override
    public List<Test> findAllTestsWithQuestionsAndAnswers(Long offset, Long count) {
        return dao.findAllTestsWithQuestionsAndAnswers(offset, count);
    }

    @Override
    public List<Test> findAllTestsWithQuestions(Long offset, Long count) {
        return dao.findAllTestsWithQuestions(offset, count);
    }

    @Override
    public List<Test> findPlainTests(Long offset, Long count) {
        return dao.findAllPlainTests(offset, count);
    }

    @Override
    public boolean delete(Test test) {
        return dao.delete(test);
    }

    @Override
    public void save(Test test) {
        if( !updateComplex(test) ) {
            insertComplex(test);
        }
    }

    @Override
    public boolean updateComplex(Test test) {
        return dao.updateComplex(test);
    }

    @Override
    public boolean updatePlain(Test test) {
        return dao.updatePlain(test);
    }

    @Override
    public Test findComplexOneById(Long id) {
        return dao.findComplexOneById(id);
    }

    @Override
    public Test findPlainOneById(Long id) {
        return dao.findPlainOneById(id);
    }

    @Override
    public Map<Long, List<Long>> findCorrectAnswerIdsByTestId(Long testId) {
        return dao.findCorrectAnswerIdsByTestId(testId);
    }

    @Override
    public Long count() {
        return dao.count();
    }

    @Override
    public Long getFilterTutorId() {
        return dao.getFilterTutorId();
    }

    @Override
    public void setFilterTutorId(Long filterTutorId) {
        dao.setFilterTutorId(filterTutorId);
    }

    @Override
    public Long getFilterSubjectId() {
        return dao.getFilterSubjectId();
    }

    @Override
    public void setFilterSubjectId(Long filterSubjectId) {
        dao.setFilterSubjectId(filterSubjectId);
    }

    @Override
    public String getDateOrder() {
        return dao.getDateOrder();
    }

    @Override
    public void enableDescDateOrder() {
        dao.enableDescDateOrder();
    }

    @Override
    public void enableAscDateOrder() {
        dao.enableAscDateOrder();
    }

    @Override
    public boolean isUseFilter() {
        return dao.isUseFilter();
    }

    @Override
    public void enableFilter() {
        dao.enableFilter();
    }

    @Override
    public void disableFilter() {
        dao.disableFilter();
    }

    @Override
    public boolean isUseOrder() {
        return dao.isUseOrder();
    }

    @Override
    public void enableOrder() {
        dao.enableOrder();
    }

    @Override
    public void disableOrder() {
        dao.disableOrder();
    }

    @Override
    public void resetFilterAndOrder() {
        dao.resetFilterAndOrder();
    }
}
