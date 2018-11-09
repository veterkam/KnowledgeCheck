package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.entity.Student;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao extends UserDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    public StudentDao(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private void insertProfile(Student student) throws DAOException{
        String sql = "INSERT INTO student_profiles (`id`, `specialty`, `group`, `year`) " +
                "VALUES(?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, student.getId());
            statement.setString(2, student.getSpecialty());
            statement.setString(3, student.getGroup());
            statement.setInt(4, student.getYear());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if(!isRowInserted) {
                throw new DAOException("Inserting student profile failed, no rows affected.");
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting student profile data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public void insert(Student student) throws DAOException {
        super.insert(student);
        insertProfile(student);
    }

    public boolean delete(Student student) throws DAOException {
        // DELETE CASCADE
        return super.delete(student);
    }

    private boolean updateProfile(Student student) throws DAOException {
        String sql = "UPDATE student_profiles SET "
                +"`specialty` = ?, `group` = ?, `year` = ?"
                + "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, student.getSpecialty());
            statement.setString(2, student.getGroup());
            statement.setInt(3, student.getYear());
            statement.setInt(4, student.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating student data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return isRowUpdated;
    }

    public boolean update(Student student) throws DAOException {
        if(!super.update(student)) {
            return false;
        }

        if(!updateProfile(student)) {
            insertProfile(student);
        }

        return true;
    }

    private Student readResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstname = resultSet.getString("firstname");
        String lastname = resultSet.getString("lastname");
        String email = resultSet.getString("email");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        int role = resultSet.getInt("role");
        String specialty = resultSet.getString("specialty");
        String group = resultSet.getString("group");
        int year = resultSet.getInt("year");


        Student student = new Student();
        student.setId(id);
        student.setFirstname(firstname);
        student.setLastname(lastname);
        student.setEmail(email);
        student.setRole(User.Role.fromOrdinal(role));
        student.setUsername(username);
        student.setPassword(password);
        student.setSpecialty(specialty);
        student.setGroup(group);
        student.setYear(year);

        return student;
    }

    public Student get(int id) throws DAOException {
        Student student = null;
        final User.Role role = User.Role.STUDENT;
        String sql = "SELECT users.`id`," +
                " `firstname`," +
                " `lastname`," +
                " `email`," +
                " `role`," +
                " `username`," +
                " `password`," +
                " `specialty`," +
                " `group`," +
                " `year`" +
                " from users" +
                " left join student_profiles on users.id = student_profiles.id" +
                " where users.id = ? and users.role = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, role.ordinal());
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                student = readResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading student data failed.", e);
        } finally {

            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return student;
    }

    public List<Student> getStudentsTookTest(long testId)
        throws DAOException {
        final User.Role role = User.Role.STUDENT;
        List<Student> students = new ArrayList<>();

        String sql = "SELECT DISTINCT users.`id`," +
                " `firstname`," +
                " `lastname`," +
                " `email`," +
                " `role`," +
                " `username`," +
                " `password`," +
                " `specialty`," +
                " `group`," +
                " `year`" +
                "FROM users " +
                "LEFT JOIN student_profiles on users.id = student_profiles.id " +
                "INNER JOIN testing_results ON users.id = testing_results.student_id " +
                "INNER JOIN questions ON questions.id = testing_results.question_id " +
                "WHERE questions.test_id = ? AND users.role = ? " +
                "ORDER BY firstname, lastname ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, testId);
            statement.setInt(2, role.ordinal());
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Student student = readResultSet(resultSet);
                students.add(student);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading student data failed.", e);
        } finally {

            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return students;
    }
}
