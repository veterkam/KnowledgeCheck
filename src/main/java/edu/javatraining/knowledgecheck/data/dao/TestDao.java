package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.domain.Test;

import java.util.List;
import java.util.Map;

public interface TestDao {

    Long count();
    Long insertComplex(Test test);
    Long insertPlain(Test test);
    List<Test> findAllTestsWithQuestionsAndAnswers(Long offset, Long count);
    List<Test> findAllTestsWithQuestions(Long offset, Long count);
    List<Test> findAllPlainTests(Long offset, Long count);
    List<Test> findAllPlainTests();
    boolean delete(Test test);
    boolean updateComplex(Test test);
    boolean updatePlain(Test test);
    Test findComplexOneById(Long id);
    Test findPlainOneById(Long id);
    Map<Long, List<Long>> findCorrectAnswerIdsByTestId(Long testId);
    Long getFilterTutorId();
    void setFilterTutorId(Long filterTutorId);
    Long getFilterSubjectId();
    void setFilterSubjectId(Long filterSubjectId);
    String getDateOrder();
    void enableDescDateOrder();
    void enableAscDateOrder();
    boolean isUseFilter();
    void enableFilter();
    void disableFilter();
    boolean isUseOrder();
    void enableOrder();
    void disableOrder();
    void resetFilterAndOrder();
}
