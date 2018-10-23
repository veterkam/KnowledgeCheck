package com.epam.javatraining.knowledgecheck.datalayer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epam.javatraining.knowledgecheck.datalayer.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.datalayer.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LogManager.getLogger(UserDao.class.getName());
    protected ConnectionPool connectionPool;

    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public boolean insert(User user) throws SQLException {

        if(user.getFirstname() == "" ||
                user.getLastname() == "" ||
                user.getUsername() == "" ||
                user.getPassword() == "") {
            return false;
        }

        String sql = "INSERT INTO users (firstname, lastname, email, role, username, password) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.getFirstname());
        statement.setString(2, user.getLastname());
        statement.setString(3, user.getEmail());
        statement.setInt(4, user.getRole().ordinal());
        statement.setString(5, user.getUsername());
        statement.setString(6, user.getPassword());

        boolean isRowInserted = statement.executeUpdate() > 0;

        if(isRowInserted) {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } else {
            throw new SQLException("Inserting user failed, no rows affected.");
        }

        statement.close();
        connectionPool.releaseConnection(connection);

        return true;
    }

    public List<User> listAll() throws SQLException {
        List<User> listUser = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            int roleInt = resultSet.getInt("role");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            User.Role role = User.Role.fromOrdinal(roleInt);
            User user = new User(id, firstname, lastname,
                    email, role, username, password);
            listUser.add(user);
        }

        resultSet.close();
        statement.close();
        connectionPool.releaseConnection(connection);

        return listUser;
    }

    public boolean delete(User user) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());

        boolean isRowDeleted = statement.executeUpdate() > 0;
        statement.close();
        connectionPool.releaseConnection(connection);

        return isRowDeleted;
    }

    public boolean update(User user) throws SQLException {

        if(user.getFirstname() == "" ||
                user.getLastname() == "" ||
                user.getUsername() == "" ||
                user.getPassword() == "") {
            return false;
        }

        String sql = "UPDATE users SET "
                +"firstname = ?, lastname = ?, email = ?, role = ?, username = ?, password = ? "
                + "WHERE id = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getFirstname());
        statement.setString(2, user.getLastname());
        statement.setString(3, user.getEmail());
        statement.setInt(4, user.getRole().ordinal());
        statement.setString(5, user.getUsername());
        statement.setString(6, user.getPassword());
        statement.setInt(7, user.getId());

        boolean isRowUpdated = statement.executeUpdate() > 0;
        statement.close();
        connectionPool.releaseConnection(connection);

        return isRowUpdated;
    }

    public User get(int id) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()) {
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            int roleInt = resultSet.getInt("role");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            User.Role role = User.Role.fromOrdinal(roleInt);
            user = new User(id, firstname, lastname,
                    email, role, username, password);
        }

        resultSet.close();
        statement.close();
        connectionPool.releaseConnection(connection);

        return user;
    }

    public User get(String username) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            int roleInt = resultSet.getInt("role");
            String password = resultSet.getString("password");

            User.Role role = User.Role.fromOrdinal(roleInt);
            user = new User(id, firstname, lastname,
                    email, role, username, password);
        }

        resultSet.close();
        statement.close();
        connectionPool.releaseConnection(connection);

        return user;
    }
}
