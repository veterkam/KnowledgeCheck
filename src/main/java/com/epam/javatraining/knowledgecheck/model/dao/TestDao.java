package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;

    public final static String ORDER_ASC = "ASC";
    public final static String ORDER_DESC = "DESC";

    private int filterTutorId = 0;
    private int filterSubjectId = 0;
    private String dateOrder;   // DESC, ASC
    private boolean useFilter = false;
    private boolean useOrder = false;

    public TestDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insert(Test test) throws DAOException {
        insertSingle(test);

        QuestionDao dao = new QuestionDao(connectionPool);
        for(Question question : test.getQuestions()) {
            dao.insert(question);
        }
    }

    public void insertSingle(Test test) throws DAOException {

        // Insert update_time as default TIMESTAMP
        String sql = "INSERT INTO tests (`subject_id`, `tutor_id`, `title`, `description`) " +
                "VALUES(?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, test.getSubjectId());
            statement.setInt(2, test.getTutorId());
            statement.setString(3, test.getTitle());
            statement.setString(4, test.getDescription());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        test.setId(generatedKeys.getInt(1));

                        for(Question question : test.getQuestions()) {
                            question.setTestId(test.getId());
                        }
                    } else {
                        DAOException e = new DAOException("Creating test data failed, no ID obtained.");
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
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
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        for(Question question : test.getQuestions()) {
            question.setTestId(test.getId());
        }
    }

    private void attachQuestions(Test test) throws DAOException {
        QuestionDao dao = new QuestionDao(connectionPool);
        List<Question> questionList = dao.listForTest(test.getId());
        test.setQuestions(questionList);
    }

    private void attachQuestions(List<Test> testList) throws DAOException {
        QuestionDao dao = new QuestionDao(connectionPool);
        for(Test test : testList) {
            List<Question> questionList = dao.listForTest(test.getId());
            test.setQuestions(questionList);
        }
    }

    private String getFilter() {
        String where = "";
        if(isUseFilter()) {

            if(filterTutorId > 0) {
                where += " `tutor_id` = '" + filterTutorId + "' ";
            }
            if(filterSubjectId > 0) {
                if(!where.isEmpty()) {
                    where += " AND ";
                }
                where += " `subject_id` = '" + filterSubjectId + "' ";
            }

            where = " WHERE " + where;
        }

        return where;
    }

    private String getOrder() {
        String order = "";
        if(isUseOrder()) {
            order += " ORDER BY `update_time` " + dateOrder;
        }

        return order;
    }

    public int getTestCount() throws DAOException {
        String sql = "SELECT COUNT(*) FROM tests";
        sql += getFilter();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int result = 0;

        try {
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {

            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
        return result;
    }

    private Test scanTest(ResultSet resultSet)
            throws SQLException, DAOException {

        long id = resultSet.getLong("id");
        int subjectId = resultSet.getInt("subject_id");
        int tutorId = resultSet.getInt("tutor_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Timestamp updateTime = resultSet.getTimestamp("update_time");

        Subject subject = new SubjectDao(connectionPool).get(subjectId);
        Tutor tutor = new TutorDao(connectionPool).get(tutorId);

        Test test = new Test(id, subject, tutor, title, description, updateTime);
        return test;
    }

    public List<Test> getList(long offset, long count) throws DAOException {
        List<Test> testList = getListSingle(offset, count);
        attachQuestions(testList);
        return testList;
    }

    public List<Test> getListSingle(long offset, long count)
        throws DAOException {
        List<Test> testList = new ArrayList<>();
        String sql = "SELECT * FROM tests" + getFilter() + getOrder() + " LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, offset);
            statement.setLong(2, count);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Test test = scanTest(resultSet);
                testList.add(test);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {

            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return testList;
    }

//    Deprecated, use filter
//
//    public List<Test> getListForTutor(int tutorId) throws DAOException {
//        List<Test> testList = getListForTutorSingle(tutorId);
//        attachQuestions(testList);
//        return testList;
//    }
//
//
//    public List<Test> getListForTutorSingle(int tutorId) throws DAOException {
//        List<Test> testList = new ArrayList<>();
//        String sql = "SELECT * FROM tests WHERE tutor_id=?";
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = connectionPool.getConnection();
//
//            statement = connection.prepareStatement(sql);
//            statement.setLong(1, tutorId);
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Test test = scanTest(resultSet);
//                testList.add(test);
//            }
//        } catch (SQLException e) {
//            logger.error(e.getMessage(), e);
//            throw new DAOException("Reading test data failed.", e);
//        } finally {
//
//            try {
//                resultSet.close();
//                statement.close();
//            } catch (SQLException e) {
//                // do nothing
//            } finally {
//                connectionPool.releaseConnection(connection);
//            }
//        }
//
//        return testList;
//    }
//
//    public List<Test> getListForSubject(int subjectId) throws DAOException {
//        List<Test> testList = getListForSubjectSingle(subjectId);
//        attachQuestions(testList);
//
//        return testList;
//    }
//
//
//    public List<Test> getListForSubjectSingle(int subjectId) throws DAOException {
//        List<Test> testList = new ArrayList<>();
//        String sql = "SELECT * FROM tests WHERE subject_id=?";
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = connectionPool.getConnection();
//
//            statement = connection.prepareStatement(sql);
//            statement.setLong(1, subjectId);
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Test test = scanTest(resultSet);
//                testList.add(test);
//            }
//        } catch (SQLException e) {
//            logger.error(e.getMessage(), e);
//            throw new DAOException("Reading test data failed.", e);
//        } finally {
//
//            try {
//                resultSet.close();
//                statement.close();
//            } catch (SQLException e) {
//                // do nothing
//            } finally {
//                connectionPool.releaseConnection(connection);
//            }
//        }
//
//        return testList;
//    }

    public boolean delete(Test test) throws DAOException {
        String sql = "DELETE FROM tests WHERE id = ? AND tutor_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, test.getId());
            statement.setLong(2, test.getTutorId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting test data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return isRowDeleted;
    }

    public boolean update(Test test) throws DAOException {
        if(!updateSingle(test)) {
            return false;
        }

        QuestionDao dao = new QuestionDao(connectionPool);
        for(Question question : test.getQuestions()) {
            if(!dao.update(question)) {
                dao.insert(question);
            }
        }

        return true;
    }

    public boolean updateSingle(Test test) throws DAOException {

        String sql = "UPDATE tests SET subject_id = ?, title = ?, description = ?, update_time = NOW() " +
                " WHERE id = ? AND tutor_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, test.getSubjectId());
            statement.setString(2, test.getTitle());
            statement.setString(3, test.getDescription());
            statement.setLong(4, test.getId());
            statement.setInt(5, test.getTutorId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating test data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return isRowUpdated;
    }

    public Test get(long id) throws DAOException {
        Test test = getSingle(id);

        if(test != null) {
            // attach questions
            attachQuestions(test);
        }

        return test;
    }

    public Test getSingle(long id) throws DAOException {
        Test test = null;
        String sql = "SELECT * FROM tests WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                test = scanTest(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading test data failed.", e);
        } finally {

            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
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
            connection = connectionPool.getConnection();

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

            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return result;
    }

    public int getFilterTutorId() {
        return filterTutorId;
    }

    public void setFilterTutorId(int filterTutorId) {
        this.filterTutorId = filterTutorId;
    }

    public int getFilterSubjectId() {
        return filterSubjectId;
    }

    public void setFilterSubjectId(int filterSubjectId) {
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
        filterSubjectId = 0;
        filterTutorId = 0;
        disableFilter();
        disableOrder();
    }
}
