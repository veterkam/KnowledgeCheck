package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.dao.TestDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.Question;
import edu.javatraining.knowledgecheck.domain.Subject;
import edu.javatraining.knowledgecheck.domain.Test;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDaoJdbc extends BasicDaoJdbc implements TestDao {

    public final static String ORDER_ASC = "ASC";
    public final static String ORDER_DESC = "DESC";

    private Long filterTutorId = null;
    private Long filterSubjectId = null;
    private String dateOrder;   // DESC, ASC
    private boolean useFilter = false;
    private boolean useOrder = false;

    public TestDaoJdbc() {
        super();
    }

    @Override
    public Long insertComplex(Test test) {
        enableTransactionControl();
        Long resultId;
        try {
            resultId = insertPlain(test);

            Connection conn = getConnection();

            try {
                QuestionDaoJdbc dao = new QuestionDaoJdbc(conn);
                for (Question question : test.getQuestions()) {
                    dao.insertComplex(question);
                }

                tryCommit(conn);
            } catch (DAOException e) {
                tryRollback(conn);
                throw e;
            } finally {
                closeCommunication(conn);
            }

        } finally {
            disableTransactionControl();
        }

        return resultId;
    }

    @Override
    public Long insertPlain(Test test)  {
        // Insert update_time as default TIMESTAMP
        String sql = "INSERT INTO tests (`subject_id`, `tutor_id`, `title`, `description`) " +
                "VALUES(?, ?, ?, ?)";

        Long resultId = insert(sql,
                (statement -> {
                    statement.setLong(1, test.getSubjectId());
                    statement.setLong(2, test.getTutorId());
                    statement.setString(3, test.getTitle());
                    statement.setString(4, test.getDescription());
                }));

        test.setId(resultId);

        for(Question question : test.getQuestions()) {
            question.setTestId(test.getId());
        }

        return resultId;
    }

    private void attachQuestions(Test test)  {
        QuestionDaoJdbc dao = new QuestionDaoJdbc();
        List<Question> questionList = dao.findComplexAll(test.getId());
        test.setQuestions(questionList);
    }

    private void attachQuestions(List<Test> testList)  {
        QuestionDaoJdbc dao = new QuestionDaoJdbc();
        for(Test test : testList) {
            List<Question> questionList = dao.findComplexAll(test.getId());
            test.setQuestions(questionList);
        }
    }

    private String getOrder() {
        String order = "";
        if(isUseOrder()) {
            order = " ORDER BY `update_time` ";
            order += ("ASC".equals(dateOrder)) ? "ASC" : "DESC";
        }

        return order;
    }

    public Long count()  {

        String sql = "SELECT COUNT(*) FROM tests " +
                "WHERE " +
                "(? IS NULL OR tutor_id = ? ) AND " +
                "(? IS NULL OR subject_id = ?) ";

        PrimitiveEnvelope<Long> count = new PrimitiveEnvelope<>();

        select(sql,
                (statement -> {
                    statement.setObject(1, getFilterTutorId(), Types.BIGINT);
                    statement.setObject(2, getFilterTutorId(), Types.BIGINT);
                    statement.setObject(3, getFilterSubjectId(), Types.BIGINT);
                    statement.setObject(4, getFilterSubjectId(), Types.BIGINT);
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        count.value = resultSet.getLong(1);
                    }
                }));

        return count.value;
    }

    private Test extractTestFromResultSet(Test test, ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");
        Long subjectId = resultSet.getLong("subject_id");
        Long tutorId = resultSet.getLong("tutor_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Timestamp updateTime = resultSet.getTimestamp("update_time");

        Subject subject = new SubjectDaoJdbc().findOneById(subjectId);
        Tutor tutor = new TutorDaoJdbc().findOneById(tutorId);

        test.setId(id);
        test.setSubject(subject);
        test.setTutor(tutor);
        test.setTitle(title);
        test.setDescription(description);
        test.setUpdateTime(updateTime);

        return test;
    }

    @Override
    public List<Test> findComplexAll(Long offset, Long count)  {
        List<Test> testList = findPlainAll(offset, count);
        attachQuestions(testList);
        return testList;
    }

    @Override
    public List<Test> findPlainAll(Long offset, Long count) {
        List<Test> testList = new ArrayList<>();
        String sql = "SELECT * FROM tests " +
                "WHERE " +
                "(? IS NULL OR tutor_id = ? ) AND " +
                "(? IS NULL OR subject_id = ?) "
                + getOrder() + " LIMIT ?, ?";


        select(sql,
                (statement -> {
                    statement.setObject(1, getFilterTutorId(), Types.BIGINT);
                    statement.setObject(2, getFilterTutorId(), Types.BIGINT);
                    statement.setObject(3, getFilterSubjectId(), Types.BIGINT);
                    statement.setObject(4, getFilterSubjectId(), Types.BIGINT);
                    statement.setLong(5, offset);
                    statement.setLong(6, count);
                }),
                (resultSet -> {
                    while (resultSet.next()) {
                        Test test = new Test();
                        extractTestFromResultSet(test, resultSet);
                        testList.add(test);
                    }
                }));

        return testList;
    }

    @Override
    public boolean delete(Test test)  {
        String sql = "DELETE FROM tests WHERE id = ? AND tutor_id = ?";

        return delete(sql,
                (statement -> {
                    statement.setLong(1, test.getId());
                    statement.setLong(2, test.getTutorId());
                }));
    }

    @Override
    public boolean updateComplex(Test test)  {
        enableTransactionControl();
        try {
            if (!updatePlain(test)) {
                disableTransactionControl();
                return false;
            }

            Connection conn = getConnection();

            try {
                QuestionDaoJdbc dao = new QuestionDaoJdbc(conn);
                for (Question question : test.getQuestions()) {
                    if (!dao.updateComplex(question)) {
                        dao.insertComplex(question);
                    }
                }

                tryCommit(conn);
            } catch (DAOException e) {
                tryRollback(conn);
                throw e;
            } finally {
                closeCommunication(conn);
            }

        } finally {
            disableTransactionControl();
        }

        return true;
    }

    @Override
    public boolean updatePlain(Test test)  {

        String sql = "UPDATE tests SET subject_id = ?, title = ?, description = ?, update_time = NOW() " +
                " WHERE id = ? AND tutor_id = ?";

        return update(sql,
                (statement -> {
                    statement.setLong(1, test.getSubjectId());
                    statement.setString(2, test.getTitle());
                    statement.setString(3, test.getDescription());
                    statement.setLong(4, test.getId());
                    statement.setLong(5, test.getTutorId());
                }));
    }

    @Override
    public Test findComplexOneById(Long id)  {
        Test test = findPlainOneById(id);

        if(test != null) {
            // attach questions
            attachQuestions(test);
        }

        return test;
    }

    @Override
    public Test findPlainOneById(Long id)  {

        String sql = "SELECT * FROM tests WHERE id = ?";

        Test test = new Test();

        select(sql,
                (statement -> {
                    statement.setLong(1, id);
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        extractTestFromResultSet(test, resultSet);
                    }
                }));
        return test;
    }

    @Override
    public Map<Long, List<Long>> findCorrectAnswerIdsByTestId(Long testId) {
        // Method returns map of lists of correct answer ids,
        // for each question of test with id = testId
        Map<Long, List<Long>> result = new HashMap<>();

        String sql = "SELECT answers.question_id as question_id, answers.id as answer_id FROM tests " +
                "INNER JOIN questions ON tests.id = questions.test_id " +
                "INNER JOIN answers ON questions.id = answers.question_id " +
                "WHERE answers.correct = TRUE AND tests.id = ? " +
                "ORDER BY question_id";

        select(sql,
                (statement -> {
                    statement.setLong(1, testId);
                }),
                (resultSet -> {
                    while (resultSet.next()) {
                        Long questionId = resultSet.getLong("question_id");
                        Long answerId = resultSet.getLong("answer_id");

                        if(!result.containsKey(questionId)) {
                            result.put(questionId, new ArrayList<>());
                        }

                        result.get(questionId).add(answerId);
                    }
                })
                );

        return result;
    }

    @Override
    public Long getFilterTutorId() {
        return filterTutorId;
    }

    @Override
    public void setFilterTutorId(Long filterTutorId) {
        this.filterTutorId = filterTutorId;
    }

    @Override
    public Long getFilterSubjectId() {
        return filterSubjectId;
    }

    @Override
    public void setFilterSubjectId(Long filterSubjectId) {
        this.filterSubjectId = filterSubjectId;
    }

    @Override
    public String getDateOrder() {
        return dateOrder;
    }

    @Override
    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    @Override
    public boolean isUseFilter() {
        return useFilter;
    }

    @Override
    public void enableFilter() {
        this.useFilter = true;
    }

    @Override
    public void disableFilter() {
        this.useFilter = false;
    }

    @Override
    public boolean isUseOrder() {
        return useOrder;
    }

    @Override
    public void enableOrder() {
        this.useOrder = true;
    }

    @Override
    public void disableOrder() {
        this.useOrder = false;
    }

    @Override
    public void resetFilterAndOrder() {
        filterSubjectId = null;
        filterTutorId = null;
        disableFilter();
        disableOrder();
    }
}
