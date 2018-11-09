package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.entity.Student;
import com.epam.javatraining.knowledgecheck.model.entity.TestingResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestingResultsDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;

    public TestingResultsDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private void insertResultOfAnswer(int studentId, Long questionId, boolean correct)
        throws DAOException {

        String sql = "INSERT INTO testing_results (student_id, question_id, correct) " +
                "VALUES(?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, studentId);
            statement.setLong(2, questionId);
            statement.setBoolean(3, correct);

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (!isRowInserted) {
                DAOException e = new DAOException("Inserting testing result data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting testing result data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    private boolean updateResultOfAnswer(int studentId, Long questionId, boolean correct)
            throws DAOException {

        String sql = "UPDATE testing_results SET correct = ? "
                + "WHERE student_id = ? AND question_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setBoolean(1, correct);
            statement.setInt(2, studentId);
            statement.setLong(3, questionId);

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating testing result data failed.", e);
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

    public void insert(TestingResults testingResults) throws DAOException {

        Map<Long, Boolean> resultOfAnswers = testingResults.getAnswerResults();
        for(Long questionId : resultOfAnswers.keySet()) {
            boolean correct = resultOfAnswers.get(questionId);
            insertResultOfAnswer(testingResults.getStudentId(), questionId, correct);
        }
    }

    public void update(TestingResults testingResults) throws DAOException {

        Map<Long, Boolean> resultOfAnswers = testingResults.getAnswerResults();
        for(Long questionId : resultOfAnswers.keySet()) {
            boolean correct = resultOfAnswers.get(questionId);

            if( !updateResultOfAnswer(testingResults.getStudentId(), questionId, correct) ) {
                insertResultOfAnswer(testingResults.getStudentId(), questionId, correct);
            }
        }
    }

    public TestingResults get(int studenId, long testId) throws DAOException {
        TestingResults testingResults = null;
        String sql = "SELECT testing_results.question_id as question_id, testing_results.correct as correct " +
                "FROM testing_results " +
                "INNER JOIN questions ON questions.id = testing_results.question_id " +
                "WHERE testing_results.student_id = ? AND questions.test_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, studenId);
            statement.setLong(2, testId);
            resultSet = statement.executeQuery();

            Map<Long, Boolean> answerResults = new HashMap<>();
            while(resultSet.next()) {
                long questionId = resultSet.getLong("question_id");
                boolean correct = resultSet.getBoolean("correct");
                answerResults.put(questionId, correct);
            }
            testingResults = new TestingResults(studenId, testId, answerResults);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading testing results data failed.", e);
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
        return testingResults;
    }

    public List<TestingResults> get(long testId) throws DAOException {
        List<TestingResults> testingResultsList = new ArrayList<>();

        StudentDao studentDao = new StudentDao(connectionPool);
        List<Student> students = studentDao.getStudentsTookTest(testId);

        for(Student student : students) {
            TestingResults results = get(student.getId(), testId);
            results.setStudent(student);
            testingResultsList.add(results);
        }

        return testingResultsList;
    }



}
