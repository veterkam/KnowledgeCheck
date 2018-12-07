package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.AnswerDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.Answer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDaoJdbc extends BasicDaoJdbc implements AnswerDao {

    public AnswerDaoJdbc(ConnectionPool pool) {
        super(pool);
    }

    public AnswerDaoJdbc(Connection conn) {
        super(conn);
    }

    @Override
    public Long save(Answer subject) {
        Long id = subject.getId();
        if(!update(subject)) {
            id = insert(subject);
        }

        return id;
    }

    @Override
    public List<Answer> findAll() {
        return findAll(null, null);
    }

    @Override
    public List<Answer> findAll(Long offset, Long count) {
        List<Answer> answerList = new ArrayList<>();
        String sql = "SELECT * FROM answers";

        select(sql,
                (statement -> {
                    if(offset != null) {
                        statement.setLong(1, offset);
                        statement.setLong(2, count);
                    }
                }),
                (resultSet -> {
                    while (resultSet.next()) {
                        Answer answer = extractAnswerFromResultSet(resultSet);
                        answerList.add(answer);
                    }
                }));

        return answerList;//.toArray(new Answer[answerList.size()]);
    }

    @Override
    public Long count() {
        String sql = "SELECT * FROM answers";
        return count(sql);
    }

    @Override
    public Long insert(Answer answer) {

        String sql = "INSERT INTO answers (`question_id`, `description`, `correct`) " +
                "VALUES(?, ?, ?)";

        Long resultId = insert(sql,
                (statement -> {
                    statement.setLong(1, answer.getQuestionId());
                    statement.setString(2, answer.getDescription());
                    statement.setBoolean(3, answer.isCorrect());
                }));

        answer.setId(resultId);
        return resultId;
    }

    @Override
    public List<Answer> findByQuestionId(Long questionId) {
        List<Answer> answerList = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE question_id=?";

        select(sql,
                (statement -> {
                    statement.setLong(1, questionId);
                }),
                (resultSet -> {
                    while (resultSet.next()) {
                        Answer answer = extractAnswerFromResultSet(resultSet);
                        answerList.add(answer);
                    }
                }));

        return answerList;//.toArray(new Answer[answerList.size()]);
    }

    @Override
    public boolean delete(Answer answer) {
        String sql = "DELETE FROM answers WHERE id = ?";

        return deleteById(answer.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM answers WHERE id = ?";

        return delete(sql,
                (statement -> {
                    statement.setLong(1, id);
                }));
    }

    @Override
    public boolean update(Answer answer) {

        String sql = "UPDATE answers SET description = ?, correct= ? " +
                " WHERE id = ? AND question_id = ?";

        return update(sql,
                    statement -> {
                        statement.setString(1, answer.getDescription());
                        statement.setBoolean(2, answer.isCorrect());
                        statement.setLong(3, answer.getId());
                        statement.setLong(4, answer.getQuestionId());
                    });
    }

    @Override
    public Answer findOne(Answer answer) {
        return findOneById(answer.getId());
    }
    @Override
    public Answer findOneById(Long id) {

        String sql = "SELECT * FROM answers WHERE id = ?";

        PrimitiveEnvelope<Answer> answer = new PrimitiveEnvelope<>();

        select(sql,
                (statement -> {
                    statement.setLong(1, id);
                }),
                (resultSet -> {
                    answer.value = extractAnswerFromResultSet(resultSet);
                }));

        return answer.value;
    }

    protected Answer extractAnswerFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long questionId = resultSet.getLong("question_id");
        String description = resultSet.getString("description");
        Boolean correct = resultSet.getBoolean("correct");

        return new Answer(id, questionId, description, correct);
    }
}
