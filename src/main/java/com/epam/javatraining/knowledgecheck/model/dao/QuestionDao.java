package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.entity.Answer;
import com.epam.javatraining.knowledgecheck.model.entity.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;

    public QuestionDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insert(Question question) throws DAOException {
        insertSingle(question);

        AnswerDao dao = new AnswerDao(connectionPool);
        for(Answer answer : question.getAnswers()) {
            dao.insert(answer);
        }
    }

    public void insertSingle(Question question) throws DAOException {

        String sql = "INSERT INTO questions (`test_id`, `description`) " +
                "VALUES(?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, question.getTestId());
            statement.setString(2, question.getDescription());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        question.setId(generatedKeys.getInt(1));
                    } else {
                        DAOException e = new DAOException("Creating question data failed, no ID obtained.");
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            } else {
                DAOException e = new DAOException("Inserting question data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting question data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        for(Answer answer : question.getAnswers()) {
            answer.setQuestionId(question.getId());
        }
    }

    public List<Question> listForTest(long testId) throws DAOException {
        List<Question> questionList = listForTestSingle(testId);

        AnswerDao dao = new AnswerDao(connectionPool);
        for(Question question : questionList) {
            List<Answer> answerList = dao.listForQuestion(question.getId());
            question.setAnswers(answerList);
        }

        return questionList;
    }


    public List<Question> listForTestSingle(long testId) throws DAOException {
        List<Question> questionList = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE test_id=?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, testId);
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                Question question = new Question(id, testId, description);
                questionList.add(question);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading question data failed.", e);
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

        return questionList;
    }

    public boolean delete(Question question) throws DAOException {
        String sql = "DELETE FROM questions WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, question.getId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting question data failed.", e);
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

    public boolean update(Question question) throws DAOException {
        if(!updateSingle(question)) {
            return false;
        }

        AnswerDao dao = new AnswerDao(connectionPool);
        for(Answer answer : question.getAnswers()) {
            if(!dao.update(answer)) {
                dao.insert(answer);
            }
        }

        return true;
    }

    public boolean updateSingle(Question question) throws DAOException {

        String sql = "UPDATE questions SET test_id = ?, description = ? " +
                " WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, question.getTestId());
            statement.setString(2, question.getDescription());
            statement.setLong(3, question.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating question data failed.", e);
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

    public Question get(long id) throws DAOException {
        Question question = getSingle(id);

        AnswerDao dao = new AnswerDao(connectionPool);
        if(question != null) {
            List<Answer> answerList = dao.listForQuestion(id);
            question.setAnswers(answerList);
        }

        return question;
    }

    public Question getSingle(long id) throws DAOException {
        Question question = null;
        String sql = "SELECT * FROM questions WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Long testId = resultSet.getLong("testId");
                String description = resultSet.getString("description");
                question = new Question(id, testId, description);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading question data failed.", e);
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

        return question;
    }
}
