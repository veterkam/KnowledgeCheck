package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.AnswerDaoJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;
import edu.javatraining.knowledgecheck.domain.Answer;
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

public class AnswerDaoJdbcSuite {

    private final String SCRIPT_CLEAR_DB = "/sql/clear_db.sql";
    private final String SCRIPT_CREATE_TABLES = "/sql/create_tables.sql";
    private final String SCRIPT_CLEAR_TABLES = "/sql/clear_tables.sql";
    private final String SCRIPT_FILL_TABLES = "/sql/fill_tables_for_answer_test.sql";

    private List<Answer> answers;

    private ConnectionPool pool;
    private SqlScriptRunner sqlRunner;
    private AnswerDaoJdbc dao;

    private UserDaoJdbc userDaoJdbc;

    @BeforeClass
    public void setUp() {

        Properties props = PropertyFileReader.read("/test_configure.properties");

        pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        dao = new AnswerDaoJdbc(pool);

        sqlRunner = new SqlScriptRunner(pool);
        sqlRunner.runScript(SCRIPT_CLEAR_DB);
        sqlRunner.runScript(SCRIPT_CREATE_TABLES);
    }

    @BeforeMethod
    public void prepare() {
        sqlRunner.runScript(SCRIPT_CLEAR_TABLES);

        answers = new ArrayList<>(Arrays.asList(
                new Answer(null, 1L, "Answer 1.a", true),
                new Answer(null, 2L, "Answer 2.a", false),
                new Answer(null, 1L, "Answer 1.b", false)));
    }

    @Test
    public void TestConnection() {
        Assert.assertNotNull(pool.getConnection());
    }

    @Test(expectedExceptions = DAOException.class)
    public void testTryInsertWithWrongQuestionId() {

        Answer expected = new Answer();
        expected.setId(1L);
        expected.setQuestionId(1L);
        expected.setDescription("answer");
        dao.insert(expected);
    }

    @Test
    public void testTryInsertWithCorrectQuestionId() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);

        Answer expected = new Answer();
        expected.setQuestionId(1L);
        expected.setDescription("answer");
        dao.insert(expected);

        Answer stored = dao.findOneById(expected.getId());
        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindAll() {

        List<Answer> stored = dao.findAll();
        Assert.assertTrue(stored.size() == 0);

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        for(Answer a : answers) {
            dao.insert(a);
        }

        stored = dao.findAll();
        Assert.assertTrue(stored.size() == answers.size());
        Assert.assertTrue(stored.containsAll(answers));
    }

    @Test
    public void testFindByQuestionID() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        Long count = dao.count();
        Assert.assertTrue(count == 0);
        for(Answer a : answers) {
            dao.insert(a);
        }

        // Remove Answer of question with id==2
        // Use only Answer of question with id==1
        answers.remove(1);
        List<Answer> stored = dao.findByQuestionId(answers.get(0).getQuestionId());
        Assert.assertTrue(stored.size() == answers.size());
        Assert.assertTrue(stored.containsAll(answers));
    }

    @Test
    public void testCount() {

        Long count = dao.count();
        Assert.assertTrue(count == 0);

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        for(Answer a : answers) {
            dao.insert(a);
        }

        count = dao.count();
        Assert.assertTrue(count == answers.size());
    }

    @Test
    public void testDeleteById() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        for(Answer a : answers) {
            dao.insert(a);
        }

        final int removeIndex = 1;
        final Long removeId = answers.get(removeIndex).getId();
        dao.deleteById(removeId);


        answers.remove(removeIndex);
        List<Answer> stored = dao.findAll();
        Assert.assertTrue(stored.size() == answers.size());
        Assert.assertTrue(stored.containsAll(answers));
    }

    @Test
    public void testDelete() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        for(Answer a : answers) {
            dao.insert(a);
        }

        final int removeIndex = 1;
        dao.delete(answers.get(removeIndex));


        answers.remove(removeIndex);
        List<Answer> stored = dao.findAll();
        Assert.assertTrue(stored.size() == answers.size());
        Assert.assertTrue(stored.containsAll(answers));
    }



    @Test
    public void testUpdate() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        Answer expected = answers.get(0);
        Long id = dao.insert(expected);
        expected = answers.get(2);
        expected.setId(id);
        dao.update(expected);

        Answer stored = dao.findOne(expected);
        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testUpdateWithWrongQuestionIDShouldNotUpdate() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        Answer expected = answers.get(0);
        Long id = dao.insert(expected);
        Answer wrong = answers.get(2);
        wrong.setQuestionId(expected.getQuestionId() + 1);
        wrong.setId(id);
        dao.update(wrong);

        Answer stored = dao.findOne(wrong);
        Assert.assertFalse(stored.fullEquals(wrong));
    }

    @Test
    public void testSave() {

        sqlRunner.runScript(SCRIPT_FILL_TABLES);
        Answer expected = answers.get(0);
        Long id = dao.save(expected);

        expected = answers.get(2);
        expected.setId(id);
        dao.save(expected);

        Answer stored = dao.findOne(expected);
        Assert.assertTrue(stored.fullEquals(expected));
    }
}
