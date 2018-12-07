package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.QuestionDao;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.domain.Answer;
import edu.javatraining.knowledgecheck.domain.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoJdbc extends BasicDaoJdbc implements QuestionDao {

    public QuestionDaoJdbc(ConnectionPool pool) {
        super(pool);
    }

    public QuestionDaoJdbc(Connection conn) {
        super(conn);
    }

    @Override
    public Long insertComplex(Question question) {
        enableTransactionControl();
        Long resultId;
        try {
            resultId = insertPlain(question);

            Connection conn = getConnection();

            try {
                AnswerDaoJdbc dao = new AnswerDaoJdbc(conn);
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
            disableTransactionControl();
        }

        return resultId;
    }

    @Override
    public Long insertPlain(Question question) {

        String sql = "INSERT INTO questions (`test_id`, `description`) " +
                "VALUES(?, ?)";

        Long resultId = insert(sql,
                (statement -> {
                    statement.setLong(1, question.getTestId());
                    statement.setString(2, question.getDescription());
                }));

        question.setId(resultId);

        for(Answer answer : question.getAnswers()) {
            answer.setQuestionId(question.getId());
        }

        return resultId;
    }

    @Override
    public List<Question> findComplexAll(Long testId) {
        List<Question> questionList = findPlainAll(testId);

        // attach answers
        AnswerDaoJdbc dao = new AnswerDaoJdbc(connectionPool);
        for(Question question : questionList) {
            List<Answer> answers = dao.findByQuestionId(question.getId());
            question.setAnswers(answers);
        }

        return questionList;
    }

    @Override
    public List<Question> findPlainAll(Long testId) {
        List<Question> questionList = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE test_id = ?";

        select(sql,
                (statement -> {
                    statement.setLong(1, testId);
                }),
                (resultSet -> {
                    while (resultSet.next()) {
                        Question question = new Question();
                        extractQuestionFromResultSet(resultSet, question);
                        questionList.add(question);
                    }
                }));

        return questionList;
    }

    @Override
    public boolean delete(Question question) {
        String sql = "DELETE FROM questions WHERE id = ?";

        return delete(sql,
                (statement -> {
                    statement.setLong(1, question.getId());
                }));
    }

    @Override
    public boolean updateComplex(Question question) {
        enableTransactionControl();
        try {
            if(!updatePlain(question)) {
                disableTransactionControl();
                return false;
            }

            Connection conn = getConnection();

            try {
                AnswerDaoJdbc dao = new AnswerDaoJdbc(conn);
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
            disableTransactionControl();
        }

        return true;
    }

    @Override
    public boolean updatePlain(Question question) {

        String sql = "UPDATE questions SET description = ? " +
                " WHERE id = ?";

        return update(sql,
                statement -> {
                    statement.setString(1, question.getDescription());
                    statement.setLong(2, question.getId());
                });
    }

    @Override
    public Question findComplexById(Long id) {
        Question question = findPlainById(id);

        AnswerDaoJdbc dao = new AnswerDaoJdbc(connectionPool);
        if(question != null) {
            List<Answer> answers = dao.findByQuestionId(id);
            question.setAnswers(answers);
        }

        return question;
    }

    @Override
    public Question findPlainById(Long id) {
        Question question = new Question();
        String sql = "SELECT * FROM questions WHERE id = ?";

        select(sql,
                (statement -> {
                    statement.setLong(1, id);
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        extractQuestionFromResultSet(resultSet, question);
                    }
                }));

        return question;
    }

    private void extractQuestionFromResultSet(ResultSet resultSet, Question question) throws SQLException {
        question.setId( resultSet.getLong("id") );
        question.setTestId( resultSet.getLong("test_id") );
        question.setDescription( resultSet.getString("description") );
    }
}
