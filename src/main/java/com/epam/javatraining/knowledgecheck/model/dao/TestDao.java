package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;

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
        String sql = "INSERT INTO tests (`subject_id`, `tutor_id`, `description`) " +
                "VALUES(?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, test.getSubjectId());
            statement.setInt(2, test.getTutorId());
            statement.setString(3, test.getDescription());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        test.setId(generatedKeys.getInt(1));
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

    private void attachQuestions(List<Test> testList) throws DAOException {
        QuestionDao dao = new QuestionDao(connectionPool);
        for(Test test : testList) {
            List<Question> questionList = dao.listForTest(test.getId());
            test.setQuestions(questionList);
        }
    }

    public List<Test> listForTutor(int tutorId) throws DAOException {
        List<Test> testList = listForTutorSingle(tutorId);
        attachQuestions(testList);
        return testList;
    }


    public List<Test> listForTutorSingle(int tutorId) throws DAOException {
        List<Test> testList = new ArrayList<>();
        String sql = "SELECT * FROM tests WHERE tutor_id=?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, tutorId);
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                int subjectId = resultSet.getInt("subjectId");
                String description = resultSet.getString("description");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                Subject subject = new SubjectDao(connectionPool).get(subjectId);
                Tutor tutor = new TutorDao(connectionPool).get(tutorId);
                Test test = new Test(id, subject, tutor, description, updateTime);
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

    public List<Test> listForSubject(int subjectId) throws DAOException {
        List<Test> testList = listForSubjectSingle(subjectId);
        attachQuestions(testList);

        return testList;
    }


    public List<Test> listForSubjectSingle(int subjectId) throws DAOException {
        List<Test> testList = new ArrayList<>();
        String sql = "SELECT * FROM tests WHERE subject_id=?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, subjectId);
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                int tutorId = resultSet.getInt("tutorId");
                String description = resultSet.getString("description");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                Subject subject = new SubjectDao(connectionPool).get(subjectId);
                Tutor tutor = new TutorDao(connectionPool).get(tutorId);
                Test test = new Test(id, subject, tutor, description, updateTime);
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

    public boolean delete(Test test) throws DAOException {
        String sql = "DELETE FROM tests WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, test.getId());

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

        String sql = "UPDATE tests SET subject_id = ?, tutor_id = ?, description = ?, update_time = NOW() " +
                " WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, test.getSubjectId());
            statement.setInt(2, test.getTutorId());
            statement.setString(3, test.getDescription());
            statement.setLong(4, test.getId());

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
            QuestionDao dao = new QuestionDao(connectionPool);
            List<Question> questionList = dao.listForTest(id);
            test.setQuestions(questionList);
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
                int subjectId = resultSet.getInt("subjectId");
                int tutorId = resultSet.getInt("tutorId");
                String description = resultSet.getString("description");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                Subject subject = new SubjectDao(connectionPool).get(subjectId);
                Tutor tutor = new TutorDao(connectionPool).get(tutorId);
                test = new Test(id, subject, tutor, description, updateTime);
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
}
