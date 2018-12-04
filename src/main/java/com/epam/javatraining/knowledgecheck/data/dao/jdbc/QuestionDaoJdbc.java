package com.epam.javatraining.knowledgecheck.data.dao.jdbc;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.domain.Answer;
import com.epam.javatraining.knowledgecheck.domain.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoJdbc extends BasicDaoJdbc {

    public QuestionDaoJdbc() {
        super();
    }

    public QuestionDaoJdbc(Connection conn) {
        super(conn);
    }

    public long insertComplex(Question question) throws DAOException {
        activateTransactionControl();
        long resultId;
        try {
            resultId = insertPlain(question);

            Connection conn = getConnection();

            try {
                AnswerDaoJdbcJdbc dao = new AnswerDaoJdbcJdbc(conn);
                for (Answer answer : question.getAnswers()) {
                    dao.insert(answer);
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
        AnswerDaoJdbcJdbc dao = new AnswerDaoJdbcJdbc();
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
        activateTransactionControl();
        try {
            if(!updatePlain(question)) {
                deactivateTransactionControl();
                return false;
            }

            Connection conn = getConnection();

            try {
                AnswerDaoJdbcJdbc dao = new AnswerDaoJdbcJdbc(conn);
                for(Answer answer : question.getAnswers()) {
                    if(!dao.update(answer)) {
                        dao.insert(answer);
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

        AnswerDaoJdbcJdbc dao = new AnswerDaoJdbcJdbc();
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
