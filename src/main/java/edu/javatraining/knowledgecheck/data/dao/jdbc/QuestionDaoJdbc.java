package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.domain.Answer;
import edu.javatraining.knowledgecheck.domain.Question;

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

    public long insertComplex(Question question) {
        enableTransactionControl();
        long resultId;
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

    public long insertPlain(Question question) {

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

    public List<Question> getComplexList(long testId) {
        List<Question> questionList = getPlainList(testId);

        // attach answers
        AnswerDaoJdbc dao = new AnswerDaoJdbc();
        for(Question question : questionList) {
            Answer[] answers = dao.findByQuestionId(question.getId());
            question.setAnswers(answers);
        }

        return questionList;
    }

    public List<Question> getPlainList(long testId) {
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

    public boolean delete(Question question) {
        String sql = "DELETE FROM questions WHERE id = ?";

        return delete(sql,
                (statement -> {
                    statement.setLong(1, question.getId());
                }));
    }

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

    public boolean updatePlain(Question question) {

        String sql = "UPDATE questions SET description = ? " +
                " WHERE id = ?";

        return update(sql,
                statement -> {
                    statement.setString(1, question.getDescription());
                    statement.setLong(2, question.getId());
                });
    }

    public Question findComplexById(Long id) {
        Question question = findPlainById(id);

        AnswerDaoJdbc dao = new AnswerDaoJdbc();
        if(question != null) {
            Answer[] answers = dao.findByQuestionId(id);
            question.setAnswers(answers);
        }

        return question;
    }

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
