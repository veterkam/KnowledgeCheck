package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.factory.UserFactory;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TestDaoJdbc;
import edu.javatraining.knowledgecheck.domain.Subject;
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
    private final String SCRIPT_CLEAR_TABLES = "/sql/clear_tables_for_test_test.sql";
    private final String SCRIPT_FILL_TABLES = "/sql/fill_tables_for_test_test.sql";

    private List<edu.javatraining.knowledgecheck.domain.Test> tests;

    private ConnectionPool pool;
    private SqlScriptRunner sqlRunner;
    private TestDaoJdbc dao;

    @BeforeClass
    public void setUp() {

        Properties props = PropertyFileReader.read("/test_configure.properties");

        pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        dao = new TestDaoJdbc(pool);

        sqlRunner = new SqlScriptRunner(pool);
        sqlRunner.runScript(SCRIPT_CLEAR_DB);
        sqlRunner.runScript(SCRIPT_CREATE_TABLES);
        sqlRunner.runScript(SCRIPT_FILL_TABLES);
    }

    @BeforeMethod
    public void prepare() {
        sqlRunner.runScript(SCRIPT_CLEAR_TABLES);

        tests = new ArrayList<>(Arrays.asList(
                new edu.javatraining.knowledgecheck.domain.Test(
                        null,
                        new Subject(1L, "Mathematics"),
                        UserFactory.getTutor(),
                        "Question 1",
                        "Description 1",
                        null),
                new edu.javatraining.knowledgecheck.domain.Test(
                        null,
                        new Subject(1L, "Mathematics"),
                        UserFactory.getTutor(),
                        "Question 2",
                        "Description 2",
                        null),
                new edu.javatraining.knowledgecheck.domain.Test(
                        null,
                        new Subject(1L, "Mathematics"),
                        UserFactory.getTutor(),
                        "Question 3",
                        "Description 3",
                        null)));
    }

    @Test
    public void TestConnection() {
        Assert.assertNotNull(pool.getConnection());
    }

    @Test
    public void testFindPlainOneByID() {
        edu.javatraining.knowledgecheck.domain.Test expected = tests.get(0);
        dao.insertPlain(expected);

        edu.javatraining.knowledgecheck.domain.Test stored =
                dao.findPlainOneById(expected.getId());
        Assert.assertEquals(stored, expected);
    }

    @Test
    public void testCount() {
        Long count = dao.count();
        Assert.assertTrue(count == 0);


        for(edu.javatraining.knowledgecheck.domain.Test t : tests) {
            dao.insertPlain(t);
        }

        count = dao.count();
        Assert.assertTrue(count == tests.size());
    }

    @Test
    public void testDeleteById() {
        for(edu.javatraining.knowledgecheck.domain.Test q : tests) {
            dao.insertPlain(q);
        }

        final int removeIndex = 1;
        final Long removeId = tests.get(removeIndex).getId();
        dao.delete(tests.get(removeIndex));


        tests.remove(removeIndex);
        List<edu.javatraining.knowledgecheck.domain.Test> stored = dao.findAllPlainTests();
        Assert.assertTrue(stored.size() == tests.size());
        Assert.assertTrue(stored.containsAll(tests));
    }

    @Test
    public void testDelete() {
        for(edu.javatraining.knowledgecheck.domain.Test a : tests) {
            dao.insertPlain(a);
        }

        final int removeIndex = 1;
        dao.delete(tests.get(removeIndex));


        tests.remove(removeIndex);
        List<edu.javatraining.knowledgecheck.domain.Test> stored = dao.findAllPlainTests();
        Assert.assertTrue(stored.size() == tests.size());
        Assert.assertTrue(stored.containsAll(tests));
    }



    @Test
    public void testUpdate() {
        edu.javatraining.knowledgecheck.domain.Test expected = tests.get(0);
        Long id = dao.insertPlain(expected);
        expected = tests.get(2);
        expected.setId(id);
        dao.updatePlain(expected);

        edu.javatraining.knowledgecheck.domain.Test stored = dao.findPlainOneById(expected.getId());
        Assert.assertEquals(stored, expected);
    }

}
