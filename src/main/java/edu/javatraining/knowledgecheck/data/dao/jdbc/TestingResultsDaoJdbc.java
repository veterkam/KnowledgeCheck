package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TestingResultsDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.TestingResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestingResultsDaoJdbc extends BasicDaoJdbc implements TestingResultsDao {


    public TestingResultsDaoJdbc(ConnectionPool pool) {
        super(pool);
    }

    private boolean insertResultOfAnswer(Long studentId, Long questionId, boolean correct) {

        String sql = "INSERT INTO testing_results (student_id, question_id, correct) " +
                "VALUES(?, ?, ?)";

        insert(sql,
                (statement -> {
                    statement.setLong(1, studentId);
                    statement.setLong(2, questionId);
                    statement.setBoolean(3, correct);
                }),
                false);

        return true;
    }

    private boolean updateResultOfAnswer(Long studentId, Long questionId, boolean correct) {

        String sql = "UPDATE testing_results SET correct = ? "
                + "WHERE student_id = ? AND question_id = ?";

        return update(sql,
                (statement -> {
                    statement.setBoolean(1, correct);
                    statement.setLong(2, studentId);
                    statement.setLong(3, questionId);
                }));
    }

    @Override
    public boolean insert(TestingResults testingResults) {

        Map<Long, Boolean> resultOfAnswers = testingResults.getAnswerResults();
        for(Long questionId : resultOfAnswers.keySet()) {
            boolean correct = resultOfAnswers.get(questionId);
            if(insertResultOfAnswer(testingResults.getStudentId(), questionId, correct)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean update(TestingResults testingResults) {

        Map<Long, Boolean> resultOfAnswers = testingResults.getAnswerResults();
        for(Long questionId : resultOfAnswers.keySet()) {
            boolean correct = resultOfAnswers.get(questionId);

            if( !updateResultOfAnswer(testingResults.getStudentId(), questionId, correct) ) {
                if(!insertResultOfAnswer(testingResults.getStudentId(), questionId, correct)) {
                    return false;
                }

            }
        }

        return true;
    }

    @Override
    public TestingResults find(Long studenId, Long testId) {

        String sql = "SELECT testing_results.question_id as question_id, testing_results.correct as correct " +
                "FROM testing_results " +
                "INNER JOIN questions ON questions.id = testing_results.question_id " +
                "WHERE testing_results.student_id = ? AND questions.test_id = ?";

        PrimitiveEnvelope<TestingResults> testingResults = new PrimitiveEnvelope<>();

        select(sql,
                (statement -> {
                    statement.setLong(1, studenId);
                    statement.setLong(2, testId);
                }),
                (resultSet -> {
                    Map<Long, Boolean> answerResults = new HashMap<>();
                    while(resultSet.next()) {
                        Long questionId = resultSet.getLong("question_id");
                        boolean correct = resultSet.getBoolean("correct");
                        answerResults.put(questionId, correct);
                    }
                    testingResults.value = new TestingResults(studenId, testId, answerResults);
                }));

        return testingResults.value;
    }

    @Override
    public List<TestingResults> find(Long testId) {
        // Find all students who passed current test
        // with their results
        List<TestingResults> testingResultsList = new ArrayList<>();

        StudentDaoJdbc studentDao = new StudentDaoJdbc(connectionPool);
        List<Student> students = studentDao.getStudentsTookTest(testId);

        for(Student student : students) {
            TestingResults results = find(student.getId(), testId);
            results.setStudent(student);
            testingResultsList.add(results);
        }

        return testingResultsList;
    }
}
