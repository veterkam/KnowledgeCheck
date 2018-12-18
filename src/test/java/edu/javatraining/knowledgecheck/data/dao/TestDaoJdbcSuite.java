package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.QuestionDaoJdbc;
import edu.javatraining.knowledgecheck.domain.Question;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.tools.PropertyFileReader;
import edu.javatraining.knowledgecheck.tools.SqlScriptRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TestDaoJdbcSuite {

    private final String SCRIPT_CLEAR_DB = "/sql/clear_db.sql";
    private final String SCRIPT_CREATE_TABLES = "/sql/create_tables.sql";
    private final String SCRIPT_CLEAR_TABLES = "/sql/clear_tables_for_question_test.sql";
    private final String SCRIPT_FILL_TABLES = "/sql/fill_tables_for_question_test.sql";

    private List<Question> questions;

    private ConnectionPool pool;
    private SqlScriptRunner sqlRunner;
    private QuestionDaoJdbc dao;

    @BeforeClass
    public void setUp() {

        Properties props = PropertyFileReader.read("/test_configure.properties");

        pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        dao = new QuestionDaoJdbc(pool);

        sqlRunner = new SqlScriptRunner(pool);
        sqlRunner.runScript(SCRIPT_CLEAR_DB);
        sqlRunner.runScript(SCRIPT_CREATE_TABLES);
        sqlRunner.runScript(SCRIPT_FILL_TABLES);
    }

    @BeforeMethod
    public void prepare() {
        sqlRunner.runScript(SCRIPT_CLEAR_TABLES);

        questions = new ArrayList<>(Arrays.asList(
                new Question(null, 1L, "Question 1"),
                new Question(null, 2L, "Question 2"),
                new Question(null, 1L, "Question 3")));
    }

    @Test
    public void TestConnection() {
        Assert.assertNotNull(pool.getConnection());
    }

    @Test(expectedExceptions = DAOException.class)
    public void testTryInsertWithWrongTestId() {

        Question question = new Question();
        question.setId(1L);
        question.setTestId(1000L);
        question.setDescription("question");
        dao.insertPlain(question);
    }

    @Test
    public void testTryInsertWithCorrectTestId() {

        Question expected = new Question();
        expected.setTestId(1L);
        expected.setDescription("question");
        dao.insertPlain(expected);

        Question stored = dao.findPlainById(expected.getId());
        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindByTestID() {
        for(Question q : questions) {
            dao.insertPlain(q);
        }

        // Remove Question of test with id==2
        // Use only Question of test with id==1
        questions.remove(1);
        List<Question> stored = dao.findPlainAll(questions.get(0).getTestId());
        Assert.assertTrue(stored.size() == questions.size());
        Assert.assertTrue(stored.containsAll(questions));
    }

    @Test
    public void testCount() {
        Long count = dao.count();
        Assert.assertTrue(count == 0);


        for(Question q : questions) {
            dao.insertPlain(q);
        }

        count = dao.count();
        Assert.assertTrue(count == questions.size());
    }

    @Test
    public void testDeleteById() {
        for(Question q : questions) {
            dao.insertPlain(q);
        }

        final int removeIndex = 1;
        final Long removeId = questions.get(removeIndex).getId();
        dao.deleteById(removeId);


        questions.remove(removeIndex);
        List<Question> stored = dao.findPlainAll();
        Assert.assertTrue(stored.size() == questions.size());
        Assert.assertTrue(stored.containsAll(questions));
    }

    @Test
    public void testDelete() {
        for(Question a : questions) {
            dao.insertPlain(a);
        }

        final int removeIndex = 1;
        dao.delete(questions.get(removeIndex));


        questions.remove(removeIndex);
        List<Question> stored = dao.findPlainAll();
        Assert.assertTrue(stored.size() == questions.size());
        Assert.assertTrue(stored.containsAll(questions));
    }



    @Test
    public void testUpdate() {
        Question expected = questions.get(0);
        Long id = dao.insertPlain(expected);
        expected = questions.get(2);
        expected.setId(id);
        dao.updatePlain(expected);

        Question stored = dao.findPlainById(expected.getId());
        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testUpdateWithWrongQuestionIDShouldNotUpdate() {

        Question expected = questions.get(0);
        Long id = dao.insertPlain(expected);
        Question wrong = questions.get(2);
        wrong.setTestId(expected.getTestId() + 10);
        wrong.setId(id);
        dao.updatePlain(wrong);

        Question stored = dao.findPlainById(wrong.getId());
        Assert.assertFalse(stored.fullEquals(wrong));
    }

    @Test
    public void testSave() {
        Question expected = questions.get(0);
        Long id = dao.savePlain(expected);

        expected = questions.get(2);
        expected.setId(id);
        dao.savePlain(expected);

        Question stored = dao.findPlainById(expected.getId());
        Assert.assertTrue(stored.fullEquals(expected));
    }
}
