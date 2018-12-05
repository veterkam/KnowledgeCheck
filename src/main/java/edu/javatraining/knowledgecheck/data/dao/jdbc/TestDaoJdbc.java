package edu.javatraining.knowledgecheck.data.dao.jdbc;

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

public class TestDaoJdbc extends BasicDaoJdbc {

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

    public Long insertComplex(Test test) {
        activateTransactionControl();
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
            deactivateTransactionControl();
        }

        return resultId;
    }

    public Long insertPlain(Test test) throws DAOException {
        Long resultId;
        // Insert update_time as default TIMESTAMP
        String sql = "INSERT INTO tests (`subject_id`, `tutor_id`, `title`, `description`) " +
                "VALUES(?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, test.getSubjectId());
            statement.setLong(2, test.getTutorId());
            statement.setString(3, test.getTitle());
            statement.setString(4, test.getDescription());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                resultId = getGenKey(statement).longValue();
                test.setId(resultId);

                for(Question question : test.getQuestions()) {
                    question.setTestId(test.getId());
                }
            } else {
                DAOException e = new DAOException("Inserting test data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting test data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return resultId;
    }

    private void attachQuestions(Test test) throws DAOException {
        QuestionDaoJdbc dao = new QuestionDaoJdbc();
        List<Question> questionList = dao.getComplexList(test.getId());
        test.setQuestions(questionList);
    }

    private void attachQuestions(List<Test> testList) throws DAOException {
        QuestionDaoJdbc dao = new QuestionDaoJdbc();
        for(Test test : testList) {
            List<Question> questionList = dao.getComplexList(test.getId());
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

    private int setFilter(PreparedStatement statement) throws  SQLException {
        return setFilter(statement, 0);
    }

    private int setFilter(PreparedStatement statement, int offset) throws SQLException {

        statement.setObject(++offset, getFilterTutorId(), Types.BIGINT);
        statement.setObject(++offset, getFilterTutorId(), Types.BIGINT);
        statement.setObject(++offset, getFilterSubjectId(), Types.BIGINT);
        statement.setObject(++offset, getFilterSubjectId(), Types.BIGINT);

        return offset;
    }

    public int getTestCount() throws DAOException {

        String sql = "SELECT COUNT(*) FROM tests " +
                "WHERE " +
                "(? IS NULL OR tutor_id = ? ) AND " +
                "(? IS NULL OR subject_id = ?) ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int result = 0;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            setFilter(statement);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }
        return result;
    }

    private Test extractTestFromResultSet(ResultSet resultSet)
            throws SQLException, DAOException {

        Long id = resultSet.getLong("id");
        Long subjectId = resultSet.getLong("subject_id");
        Long tutorId = resultSet.getLong("tutor_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Timestamp updateTime = resultSet.getTimestamp("update_time");

        Subject subject = new SubjectDaoJdbc().get(subjectId);
        Tutor tutor = new TutorDaoJdbc().findOneById(tutorId);

        Test test = new Test(id, subject, tutor, title, description, updateTime);
        return test;
    }

    public List<Test> getComplexList(Long offset, Long count) throws DAOException {
        List<Test> testList = getPlainList(offset, count);
        attachQuestions(testList);
        return testList;
    }

    public List<Test> getPlainList(Long offset, Long count)
        throws DAOException {
        List<Test> testList = new ArrayList<>();
        String sql = "SELECT * FROM tests " +
                "WHERE " +
                "(? IS NULL OR tutor_id = ? ) AND " +
                "(? IS NULL OR subject_id = ?) "
                + getOrder() + " LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            int index = setFilter(statement);
            statement.setLong(++index, offset);
            statement.setLong(++index, count);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Test test = extractTestFromResultSet(resultSet);
                testList.add(test);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return testList;
    }

    public boolean delete(Test test) throws DAOException {
        String sql = "DELETE FROM tests WHERE id = ? AND tutor_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, test.getId());
            statement.setLong(2, test.getTutorId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting test data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowDeleted;
    }

    public boolean updateComplex(Test test) throws DAOException {
        activateTransactionControl();
        try {
            if (!updatePlain(test)) {
                deactivateTransactionControl();
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
            deactivateTransactionControl();
        }

        return true;
    }

    public boolean updatePlain(Test test) throws DAOException {

        String sql = "UPDATE tests SET subject_id = ?, title = ?, description = ?, update_time = NOW() " +
                " WHERE id = ? AND tutor_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, test.getSubjectId());
            statement.setString(2, test.getTitle());
            statement.setString(3, test.getDescription());
            statement.setLong(4, test.getId());
            statement.setLong(5, test.getTutorId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating test data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    public Test getComplex(Long id) throws DAOException {
        Test test = getPlain(id);

        if(test != null) {
            // attach questions
            attachQuestions(test);
        }

        return test;
    }

    public Test getPlain(Long id) throws DAOException {
        Test test = null;
        String sql = "SELECT * FROM tests WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                test = extractTestFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return test;
    }

    public Map<Long, List<Long>> getCorrectAnswerIds(Long testId)
        throws DAOException {
        // Method returns map of lists of correct answer ids,
        // for each question of test with id = testId
        Map<Long, List<Long>> result = new HashMap<>();

        String sql = "SELECT answers.question_id as question_id, answers.id as answer_id FROM tests " +
                "INNER JOIN questions ON tests.id = questions.test_id " +
                "INNER JOIN answers ON questions.id = answers.question_id " +
                "WHERE answers.correct = TRUE AND tests.id = ? " +
                "ORDER BY question_id";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, testId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long questionId = resultSet.getLong("question_id");
                Long answerId = resultSet.getLong("answer_id");

                if(!result.containsKey(questionId)) {
                    result.put(questionId, new ArrayList<>());
                }

                result.get(questionId).add(answerId);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return result;
    }

    public Long getFilterTutorId() {
        return filterTutorId;
    }

    public void setFilterTutorId(Long filterTutorId) {
        this.filterTutorId = filterTutorId;
    }

    public Long getFilterSubjectId() {
        return filterSubjectId;
    }

    public void setFilterSubjectId(Long filterSubjectId) {
        this.filterSubjectId = filterSubjectId;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public boolean isUseFilter() {
        return useFilter;
    }

    public void enableFilter() {
        this.useFilter = true;
    }

    public void disableFilter() {
        this.useFilter = false;
    }

    public boolean isUseOrder() {
        return useOrder;
    }

    public void enableOrder() {
        this.useOrder = true;
    }

    public void disableOrder() {
        this.useOrder = false;
    }

    public void resetFilterAndOrder() {
        filterSubjectId = null;
        filterTutorId = null;
        disableFilter();
        disableOrder();
    }
}
