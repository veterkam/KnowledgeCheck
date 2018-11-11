package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.entity.Answer;
import com.epam.javatraining.knowledgecheck.model.entity.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao extends AbstractDao{

    public QuestionDao() {
        super();
    }

    public long insertComplex(Question question) throws DAOException {
        long resultId = insertPlain(question);

        AnswerDao dao = new AnswerDao();
        for(Answer answer : question.getAnswers()) {
            dao.insert(answer);
        }

        return resultId;
    }

    public long insertPlain(Question question) throws DAOException {
        long resultId;
        String sql = "INSERT INTO questions (`test_id`, `description`) " +
                "VALUES(?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, question.getTestId());
            statement.setString(2, question.getDescription());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                resultId = getGenKey(statement).longValue();
                question.setId(resultId);

                for(Answer answer : question.getAnswers()) {
                    answer.setQuestionId(question.getId());
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
            closeCommunication(connection, statement);
        }

        return resultId;
    }

    public List<Question> getComplexList(long testId) throws DAOException {
        List<Question> questionList = getPlainList(testId);

        // attach answers
        AnswerDao dao = new AnswerDao();
        for(Question question : questionList) {
            List<Answer> answerList = dao.getList(question.getId());
            question.setAnswers(answerList);
        }

        return questionList;
    }

    private Question extractQuestionFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long testId = resultSet.getLong("test_id");
        String description = resultSet.getString("description");
        return new Question(id, testId, description);
    }


    public List<Question> getPlainList(long testId) throws DAOException {
        List<Question> questionList = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE test_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, testId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Question question = extractQuestionFromResultSet(resultSet);
                questionList.add(question);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading question data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return questionList;
    }

    public boolean delete(Question question) throws DAOException {
        String sql = "DELETE FROM questions WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, question.getId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting question data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowDeleted;
    }

    public boolean updateComplex(Question question) throws DAOException {
        if(!updatePlain(question)) {
            return false;
        }

        AnswerDao dao = new AnswerDao();
        for(Answer answer : question.getAnswers()) {
            if(!dao.update(answer)) {
                dao.insert(answer);
            }
        }

        return true;
    }

    public boolean updatePlain(Question question) throws DAOException {

        String sql = "UPDATE questions SET description = ? " +
                " WHERE id = ? AND test_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, question.getDescription());
            statement.setLong(2, question.getId());
            statement.setLong(3, question.getTestId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating question data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    public Question getComplex(long id) throws DAOException {
        Question question = getPlain(id);

        AnswerDao dao = new AnswerDao();
        if(question != null) {
            List<Answer> answerList = dao.getList(id);
            question.setAnswers(answerList);
        }

        return question;
    }

    public Question getPlain(long id) throws DAOException {
        Question question = null;
        String sql = "SELECT * FROM questions WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                question = extractQuestionFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading question data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return question;
    }
}
