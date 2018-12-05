package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.dao.AnswerDao;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.domain.Answer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDaoJdbcJdbc extends BasicDaoJdbc implements AnswerDao {

    public AnswerDaoJdbcJdbc() {
        super();
    }

    public AnswerDaoJdbcJdbc(Connection conn) {
        super(conn);
    }

    public Long insert(Answer answer) throws DAOException {
        Long resultId;
        String sql = "INSERT INTO answers (`question_id`, `description`, `correct`) " +
                "VALUES(?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, answer.getQuestionId());
            statement.setString(2, answer.getDescription());
            statement.setBoolean(3, answer.isCorrect());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                resultId = getGenKey(statement).longValue();
                answer.setId(resultId);
            } else {
                DAOException e = new DAOException("Inserting answer data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting answer data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return resultId;
    }

    protected Answer extractAnswerFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long questionId = resultSet.getLong("question_id");
        String description = resultSet.getString("description");
        Boolean correct = resultSet.getBoolean("correct");

        return new Answer(id, questionId, description, correct);
    }

    public List<Answer> getList(Long questionId) throws DAOException {
        List<Answer> answerList = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE question_id=?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, questionId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Answer answer = extractAnswerFromResultSet(resultSet);
                answerList.add(answer);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading answer data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return answerList;
    }

    public boolean delete(Answer answer) throws DAOException {
        String sql = "DELETE FROM answers WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, answer.getId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting answer data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowDeleted;
    }

    public boolean update(Answer answer) throws DAOException {

        String sql = "UPDATE answers SET description = ?, correct= ? " +
                " WHERE id = ? AND question_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, answer.getDescription());
            statement.setBoolean(2, answer.isCorrect());
            statement.setLong(3, answer.getId());
            statement.setLong(4, answer.getQuestionId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating answer data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    public Answer get(Long id) throws DAOException {
        Answer answer = null;
        String sql = "SELECT * FROM answers WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                answer = extractAnswerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading answer data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return answer;
    }
}
