package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.UserFactory;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.StudentDaoJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.StudentDaoJdbc;
import edu.javatraining.knowledgecheck.domain.Student;
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

public class StudentDaoJdbcSuite {

    private final String SCRIPT_CLEAR_DB = "/sql/clear_db.sql";
    private final String SCRIPT_CREATE_TABLES = "/sql/create_tables.sql";
    private final String SCRIPT_CLEAR_TABLES = "/sql/clear_tables.sql";

    private ConnectionPool pool;
    private SqlScriptRunner sqlRunner;
    private StudentDaoJdbc dao;

    @BeforeClass
    public void setUp() {

        Properties props = PropertyFileReader.read("/test_configure.properties");

        pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        dao = new StudentDaoJdbc(pool);

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

        Student first = UserFactory.getStudentMark();
        Student second = UserFactory.getStudentPaul();
        second.setUsername(first.getUsername());

        dao.insert(first);
        dao.insert(second);
    }

    @Test
    public void testInsertAndFindOneById() {

        Student expected = UserFactory.getStudent();
        dao.insert(expected);
        Student stored = dao.findOneById(expected.getId());

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindOneByUsername() {

        Student expected = UserFactory.getStudent();
        dao.insert(expected);
        Student stored = dao.findOneByUsername(expected.getUsername());

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testInsertAndFindOne() {

        Student expected = UserFactory.getStudent();
        dao.insert(expected);
        Student stored = dao.findOne(expected);

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testFindAll() {

        List<Student> stored = dao.findAllStudents();
        Assert.assertTrue(stored.size() == 0);


        Student[] students = new Student[]{
                UserFactory.getStudentPaul(),
                UserFactory.getStudentMark(),
                UserFactory.getStudentNick()
        };

        for(Student u : students) {
            dao.insert(u);
        }

        stored = dao.findAllStudents();
        Assert.assertTrue(stored.size() == students.length);
        Assert.assertTrue(stored.containsAll(Arrays.asList(students)));
    }

    @Test
    public void testCount() {

        Long count = dao.count();
        Assert.assertTrue(count == 0);


        Student[] students = new Student[]{
                UserFactory.getStudentPaul(),
                UserFactory.getStudentMark(),
                UserFactory.getStudentNick()};
        for(Student u : students) {
            dao.insert(u);
        }

        count = dao.count();
        Assert.assertTrue(count == students.length);
    }

    @Test
    public void testDeleteById() {

        List<Student> students = new ArrayList<>(
                Arrays.asList(
                        UserFactory.getStudentPaul(),
                        UserFactory.getStudentMark(),
                        UserFactory.getStudentNick()));

        for(Student u : students) {
            dao.insert(u);
        }

        final int removeIndex = 1;
        final Long removeId = students.get(removeIndex).getId();
        dao.deleteById(removeId);

        students.remove(removeIndex);

        List<Student> stored = dao.findAllStudents();
        Assert.assertTrue(stored.size() == students.size());
        Assert.assertTrue(stored.containsAll(students));
    }

    @Test
    public void testDelete() {

        List<Student> students = new ArrayList<>(
                Arrays.asList(
                        UserFactory.getStudentPaul(),
                        UserFactory.getStudentMark(),
                        UserFactory.getStudentNick()));

        for(Student u : students) {
            dao.insert(u);
        }

        final int removeIndex = 1;
        final Student removeStudent = students.get(removeIndex);
        dao.delete(removeStudent);

        students.remove(removeStudent);

        List<Student> stored = dao.findAllStudents();
        Assert.assertTrue(stored.size() == students.size());
        Assert.assertTrue(stored.containsAll(students));
    }

    @Test
    public void testUpdate() {

        Student expected = UserFactory.getStudent();
        Long id = dao.insert(expected);
        expected = UserFactory.getStudentNick();
        expected.setId(id);
        dao.update(expected);

        Student stored = dao.findOne(expected);

        Assert.assertTrue(stored.fullEquals(expected));
    }

    @Test
    public void testUpdatePassword() {

        Student expected = UserFactory.getStudent();
        dao.insert(expected);
        expected.setPassword("NEW_PASSWORD");
        dao.updatePassword(expected);

        Student stored = dao.findOne(expected);
        Assert.assertTrue(stored.getPassword().equals(expected.getPassword()));
    }

    @Test
    public void testSave() {

        Student expected = UserFactory.getStudentMark();
        Long id = dao.save(expected);

        Student stored = dao.findOne(expected);
        Assert.assertEquals(stored, expected);

        expected = UserFactory.getStudentNick();
        expected.setId(id);
        dao.save(expected);

        stored = dao.findOne(expected);
        Assert.assertEquals(stored, expected);
    }
}
