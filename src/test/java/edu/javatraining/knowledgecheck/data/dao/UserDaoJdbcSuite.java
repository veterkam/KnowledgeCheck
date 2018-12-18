package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.factory.UserFactory;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;
import edu.javatraining.knowledgecheck.domain.User;
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

public class UserDaoJdbcSuite {

    private final String SCRIPT_CLEAR_DB = "/sql/clear_db.sql";
    private final String SCRIPT_CREATE_TABLES = "/sql/create_tables.sql";
    private final String SCRIPT_CLEAR_TABLES = "/sql/clear_tables.sql";

    private ConnectionPool pool;
    private SqlScriptRunner sqlRunner;
    private UserDaoJdbc dao;

    @BeforeClass
    public void setUp() {

        Properties props = PropertyFileReader.read("/test_configure.properties");

        pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        dao = new UserDaoJdbc(pool);

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

        User rob = UserFactory.getRob();
        User dup = UserFactory.getTom();
        dup.setUsername(rob.getUsername());

        dao.insert(rob);
        dao.insert(dup);
    }

    @Test
    public void testInsertAndFindOneById() {

        User expected = UserFactory.getAdmin();
        dao.insert(expected);
        User stored = dao.findOneById(expected.getId());

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindOneByUsername() {

        User expected = UserFactory.getAdmin();
        dao.insert(expected);
        User stored = dao.findOneByUsername(expected.getUsername());

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testInsertAndFindOne() {

        User expected = UserFactory.getAdmin();
        dao.insert(expected);
        User stored = dao.findOne(expected);

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindAll() {

        List<User> stored = dao.findAllUsers();
        Assert.assertTrue(stored.size() == 0);


        User[] users = new User[]{UserFactory.getBob(), UserFactory.getRob(), UserFactory.getTom()};
        for(User u : users) {
            dao.insert(u);
        }

        stored = dao.findAllUsers();
        Assert.assertTrue(stored.size() == users.length);
        Assert.assertTrue(stored.containsAll(Arrays.asList(users)));
    }

    @Test
    public void testCount() {

        Long count = dao.count();
        Assert.assertTrue(count == 0);


        User[] users = new User[]{UserFactory.getBob(), UserFactory.getRob(), UserFactory.getTom()};
        for(User u : users) {
            dao.insert(u);
        }

        count = dao.count();
        Assert.assertTrue(count == users.length);
    }

    @Test
    public void testDeleteById() {

        List<User> users = new ArrayList<>(
                Arrays.asList(UserFactory.getBob(), UserFactory.getRob(), UserFactory.getTom()));

        for(User u : users) {
            dao.insert(u);
        }

        final int removeIndex = 1;
        final Long removeId = users.get(removeIndex).getId();
        dao.deleteById(removeId);

        users.remove(removeIndex);

        List<User> stored = dao.findAllUsers();
        Assert.assertTrue(stored.size() == users.size());
        Assert.assertTrue(stored.containsAll(users));
    }

    @Test
    public void testDelete() {

        List<User> users = new ArrayList<>(
                Arrays.asList(UserFactory.getBob(), UserFactory.getRob(), UserFactory.getTom()));

        for(User u : users) {
            dao.insert(u);
        }

        final int removeIndex = 1;
        final User removeUser = users.get(removeIndex);
        dao.delete(removeUser);

        users.remove(removeUser);

        List<User> stored = dao.findAllUsers();
        Assert.assertTrue(stored.size() == users.size());
        Assert.assertTrue(stored.containsAll(users));
    }

    @Test
    public void testUpdate() {

        User expected = UserFactory.getAdmin();
        Long id = dao.insert(expected);
        expected = UserFactory.getBob();
        expected.setId(id);
        dao.update(expected);

        User stored = dao.findOne(expected);

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testUpdatePassword() {

        User expected = UserFactory.getAdmin();
        dao.insert(expected);
        expected.setPassword("NEW_PASSWORD");
        dao.updatePassword(expected);

        User stored = dao.findOne(expected);
        Assert.assertTrue(stored.getPassword().equals(expected.getPassword()));
    }

    @Test
    public void testSave() {

        User expected = UserFactory.getAdmin();
        Long id = dao.save(expected);

        User stored = dao.findOne(expected);
        Assert.assertEquals(stored, expected);

        expected = UserFactory.getTom();
        expected.setId(id);
        dao.save(expected);

        stored = dao.findOne(expected);
        Assert.assertEquals(stored, expected);
    }
}
