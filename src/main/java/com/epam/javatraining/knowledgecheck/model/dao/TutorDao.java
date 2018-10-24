package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.model.entity.Tutor;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;

import java.sql.*;

public class TutorDao extends UserDao {

    public TutorDao(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private boolean insertProfile(Tutor tutor) throws SQLException {
        String sql = "INSERT INTO tutor_profiles (id, position, scientific_degree, academicTitle) " +
                "VALUES(?, ?, ?, ?)";

        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, tutor.getId());
        statement.setString(2, tutor.getPosition());
        statement.setString(3, tutor.getScientificDegree());
        statement.setString(4, tutor.getAcademicTitle());

        boolean isRowInserted = statement.executeUpdate() > 0;

        statement.close();
        connectionPool.releaseConnection(connection);

        if(!isRowInserted) {
            throw new SQLException("Inserting tutor profile failed, no rows affected.");
        }

        return true;
    }
    
    public boolean insert(Tutor tutor) throws SQLException {
        if(!super.insert(tutor)) {
            return false;
        }     

        return insertProfile(tutor);
    }

    public boolean delete(Tutor tutor) throws SQLException {
        // DELETE CASCADE
        return super.delete(tutor);
    }

    private boolean updateProfile(Tutor tutor) throws SQLException {
        String sql = "UPDATE tutor_profiles SET "
                +"position = ?, scientific_degree = ?, academicTitle = ?"
                + "WHERE id = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, tutor.getPosition());
        statement.setString(2, tutor.getScientificDegree());
        statement.setString(3, tutor.getAcademicTitle());
        statement.setInt(4, tutor.getId());

        boolean isRowUpdated = statement.executeUpdate() > 0;
        statement.close();
        connectionPool.releaseConnection(connection);

        return isRowUpdated;
    }

    public boolean update(Tutor tutor) throws SQLException {
        if(!super.update(tutor)) {
            return false;
        }

        if(!updateProfile(tutor)) {
            return insertProfile(tutor);
        }

        return true;
    }

    public Tutor get(int id) throws SQLException {
        Tutor tutor = null;
        final User.Role role = User.Role.TUTOR;
        String sql = "select `users`.`id`," +
                " `firstname`," +
                " `lastname`," +
                " `email`," +
                " `role`," +
                " `username`," +
                " `password`," +
                " `position`," +
                " `scientific_degree`," +
                " `academicTitle`" +
                " from users" +
                " left join tutor_profiles on users.id = tutor_profiles.id" +
                " where users.id = ? AND role = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(1, role.ordinal());
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()) {
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String position = resultSet.getString("position");
            String scientificDegree = resultSet.getString("scientific_degree");
            String academicTitle = resultSet.getString("academicTitle");

            tutor = new Tutor();
            tutor.setId(id);
            tutor.setFirstname(firstname);
            tutor.setLastname(lastname);
            tutor.setEmail(email);
            tutor.setRole(role);
            tutor.setUsername(username);
            tutor.setPassword(password);
            tutor.setPosition(position);
            tutor.setScientificDegree(scientificDegree);
            tutor.setAcademicTitle(academicTitle);
        }

        resultSet.close();
        statement.close();
        connectionPool.releaseConnection(connection);

        return tutor;
    }
}
