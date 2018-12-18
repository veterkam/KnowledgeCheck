package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.factory.UserFactory;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TutorDaoJdbc;
import edu.javatraining.knowledgecheck.domain.Tutor;
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

public class TutorDaoJdbcSuite {

    private final String SCRIPT_CLEAR_DB = "/sql/clear_db.sql";
    private final String SCRIPT_CREATE_TABLES = "/sql/create_tables.sql";
    private final String SCRIPT_CLEAR_TABLES = "/sql/clear_tables.sql";

    private ConnectionPool pool;
    private SqlScriptRunner sqlRunner;
    private TutorDaoJdbc dao;

    @BeforeClass
    public void setUp() {

        Properties props = PropertyFileReader.read("/test_configure.properties");

        pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        dao = new TutorDaoJdbc(pool);

        sqlRunner = new SqlScriptRunner(pool);
        sqlRunner.runScript(SCRIPT_CLEAR_DB);
        sqlRunner.runScript(SCRIPT_CREATE_TABLES);
    }

    @BeforeMethod
    public void clearTables() {
        sqlRunner.runScript(SCRIPT_CLEAR_TABLES);
    }

    @Test
    public void TestConnection() {
        Assert.assertNotNull(pool.getConnection());
    }

    @Test(expectedExceptions = DAOException.class)
    public void testInserDuplicateUsername() {

        Tutor first = UserFactory.getTutorAlex();
        Tutor second = UserFactory.getTutorAnn();
        second.setUsername(first.getUsername());

        dao.insert(first);
        dao.insert(second);
    }

    @Test
    public void testInsertAndFindOneById() {

        Tutor expected = UserFactory.getTutor();
        dao.insert(expected);
        Tutor stored = dao.findOneById(expected.getId());

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindOneByUsername() {

        Tutor expected = UserFactory.getTutor();
        dao.insert(expected);
        Tutor stored = dao.findOneByUsername(expected.getUsername());

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testInsertAndFindOne() {

        Tutor expected = UserFactory.getTutor();
        dao.insert(expected);
        Tutor stored = dao.findOne(expected);

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindAll() {

        List<Tutor> stored = dao.findAllTutors();
        Assert.assertTrue(stored.size() == 0);


        Tutor[] tutors = new Tutor[]{
                UserFactory.getTutorAlex(),
                UserFactory.getTutorAnn(),
                UserFactory.getTutorFelix()
        };

        for(Tutor u : tutors) {
            dao.insert(u);
        }

        stored = dao.findAllTutors();
        Assert.assertTrue(stored.size() == tutors.length);
        Assert.assertTrue(stored.containsAll(Arrays.asList(tutors)));
    }

    @Test
    public void testCount() {

        Long count = dao.count();
        Assert.assertTrue(count == 0);


        Tutor[] tutors = new Tutor[]{
                UserFactory.getTutorAnn(),
                UserFactory.getTutorAlex(),
                UserFactory.getTutorFelix()};
        for(Tutor u : tutors) {
            dao.insert(u);
        }

        count = dao.count();
        Assert.assertTrue(count == tutors.length);
    }

    @Test
    public void testDeleteById() {

        List<Tutor> tutors = new ArrayList<>(
                Arrays.asList(
                        UserFactory.getTutorAlex(),
                        UserFactory.getTutorAnn(),
                        UserFactory.getTutorFelix()));

        for(Tutor u : tutors) {
            dao.insert(u);
        }

        final int removeIndex = 1;
        final Long removeId = tutors.get(removeIndex).getId();
        dao.deleteById(removeId);

        tutors.remove(removeIndex);

        List<Tutor> stored = dao.findAllTutors();
        Assert.assertTrue(stored.size() == tutors.size());
        Assert.assertTrue(stored.containsAll(tutors));
    }

    @Test
    public void testDelete() {

        List<Tutor> tutors = new ArrayList<>(
                Arrays.asList(
                        UserFactory.getTutorAlex(),
                        UserFactory.getTutorAnn(),
                        UserFactory.getTutorFelix()));

        for(Tutor u : tutors) {
            dao.insert(u);
        }

        final int removeIndex = 1;
        final Tutor removeTutor = tutors.get(removeIndex);
        dao.delete(removeTutor);

        tutors.remove(removeTutor);

        List<Tutor> stored = dao.findAllTutors();
        Assert.assertTrue(stored.size() == tutors.size());
        Assert.assertTrue(stored.containsAll(tutors));
    }

    @Test
    public void testUpdate() {

        Tutor expected = UserFactory.getTutor();
        Long id = dao.insert(expected);
        expected = UserFactory.getTutorAlex();
        expected.setId(id);
        dao.update(expected);

        Tutor stored = dao.findOne(expected);

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testUpdatePassword() {

        Tutor expected = UserFactory.getTutor();
        dao.insert(expected);
        expected.setPassword("NEW_PASSWORD");
        dao.updatePassword(expected);

        Tutor stored = dao.findOne(expected);
        Assert.assertTrue(stored.getPassword().equals(expected.getPassword()));
    }

    @Test
    public void testSave() {

        Tutor expected = UserFactory.getTutorAlex();
        Long id = dao.save(expected);

        Tutor stored = dao.findOne(expected);
        Assert.assertEquals(stored, expected);

        expected = UserFactory.getTutorAnn();
        expected.setId(id);
        dao.save(expected);

        stored = dao.findOne(expected);
        Assert.assertEquals(stored, expected);
    }
}
