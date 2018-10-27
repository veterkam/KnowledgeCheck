package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.entity.Student;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

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
                +"specialty = ?, group = ?, year = ?"
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

    public Student get(int id) throws DAOException {
        Student student = null;
        final User.Role role = User.Role.STUDENT;
        String sql = "select `users`.`id`," +
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
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String specialty = resultSet.getString("specialty");
                String group = resultSet.getString("group");
                int year = resultSet.getInt("year");

                student = new Student();
                student.setId(id);
                student.setFirstname(firstname);
                student.setLastname(lastname);
                student.setEmail(email);
                student.setRole(role);
                student.setUsername(username);
                student.setPassword(password);
                student.setSpecialty(specialty);
                student.setGroup(group);
                student.setYear(year);
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
}
