package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.entity.Answer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;

    public AnswerDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insert(Answer answer) throws DAOException {

        String sql = "INSERT INTO answers (`question_id`, `description`, `correct`) " +
                "VALUES(?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, answer.getQuestionId());
            statement.setString(2, answer.getDescription());
            statement.setBoolean(3, answer.isCorrect());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        answer.setId(generatedKeys.getInt(1));
                    } else {
                        DAOException e = new DAOException("Creating answer data failed, no ID obtained.");
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            } else {
                DAOException e = new DAOException("Inserting answer data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting answer data failed.", e);
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

    public List<Answer> listForQuestion(long questionId) throws DAOException {
        List<Answer> answerList = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE question_id=?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, questionId);
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                Boolean correct = resultSet.getBoolean("correct");
                Answer answer = new Answer(id, questionId, description, correct);
                answerList.add(answer);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading answer data failed.", e);
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

        return answerList;
    }

    public boolean delete(Answer answer) throws DAOException {
        String sql = "DELETE FROM answers WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, answer.getId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting answer data failed.", e);
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

    public boolean update(Answer answer) throws DAOException {

        String sql = "UPDATE answers SET question_id = ?, description = ?, correct= ? " +
                " WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, answer.getQuestionId());
            statement.setString(2, answer.getDescription());
            statement.setBoolean(3, answer.isCorrect());
            statement.setLong(4, answer.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating answer data failed.", e);
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

    public Answer get(long id) throws DAOException {
        Answer answer = null;
        String sql = "SELECT * FROM answers WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Long questionId = resultSet.getLong("questionId");
                String description = resultSet.getString("description");
                Boolean correct = resultSet.getBoolean("correct");
                answer = new Answer(id, questionId, description, correct);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading answer data failed.", e);
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

        return answer;
    }
}
