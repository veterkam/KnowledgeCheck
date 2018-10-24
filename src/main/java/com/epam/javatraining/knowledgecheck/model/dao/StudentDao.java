package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.model.entity.Student;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDao extends UserDao {
    public StudentDao(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private boolean insertProfile(Student student) throws SQLException {
        String sql = "INSERT INTO student_profiles (`id`, `specialty`, `group`, `year`) " +
                "VALUES(?, ?, ?, ?)";

        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, student.getId());
        statement.setString(2, student.getSpecialty());
        statement.setString(3, student.getGroup());
        statement.setInt(4, student.getYear());

        boolean isRowInserted = statement.executeUpdate() > 0;

        statement.close();
        connectionPool.releaseConnection(connection);

        if(!isRowInserted) {
            throw new SQLException("Inserting student profile failed, no rows affected.");
        }

        return true;
    }

    public boolean insert(Student student) throws SQLException {
        if(!super.insert(student)) {
            return false;
        }

        return insertProfile(student);
    }

    public boolean delete(Student student) throws SQLException {
        // DELETE CASCADE
        return super.delete(student);
    }

    private boolean updateProfile(Student student) throws SQLException {
        String sql = "UPDATE student_profiles SET "
                +"specialty = ?, group = ?, year = ?"
                + "WHERE id = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getSpecialty());
        statement.setString(2, student.getGroup());
        statement.setInt(3, student.getYear());
        statement.setInt(4, student.getId());

        boolean isRowUpdated = statement.executeUpdate() > 0;
        statement.close();
        connectionPool.releaseConnection(connection);

        return isRowUpdated;
    }

    public boolean update(Student student) throws SQLException {
        if(!super.update(student)) {
            return false;
        }

        if(!updateProfile(student)) {
            return insertProfile(student);
        }

        return true;
    }

    public Student get(int id) throws SQLException {
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
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, role.ordinal());
        ResultSet resultSet = statement.executeQuery();

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

        resultSet.close();
        statement.close();
        connectionPool.releaseConnection(connection);

        return student;
    }
}
