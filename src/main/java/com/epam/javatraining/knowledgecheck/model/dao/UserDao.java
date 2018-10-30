package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;

    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insert(User user) throws DAOException {

        String sql = "INSERT INTO users (firstname, lastname, email, role, username, password) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getRole().ordinal());
            statement.setString(5, user.getUsername());
            statement.setString(6, user.getPassword());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    } else {
                        DAOException e = new DAOException("Creating user data failed, no ID obtained.");
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            } else {
                DAOException e = new DAOException("Inserting user data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting user data failed.", e);
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

    private User scanUser(ResultSet resultSet) throws SQLException {
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
        return user;
    }

    public List<User> listAll() throws DAOException {
        List<User> listUser = new ArrayList<>();
        String sql = "SELECT * FROM users";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = scanUser(resultSet);
                listUser.add(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
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

        return listUser;
    }

    public boolean delete(User user) throws DAOException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting user data failed.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // do nothing
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }

        return isRowDeleted;
    }

    public boolean update(User user) throws DAOException {

        String sql = "UPDATE users SET "
                +"firstname = ?, lastname = ?, email = ?, role = ?, username = ?, password = ? "
                + "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getRole().ordinal());
            statement.setString(5, user.getUsername());
            statement.setString(6, user.getPassword());
            statement.setInt(7, user.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating user data failed.", e);
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

    public User get(int id) throws DAOException {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                user = scanUser(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
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

        return user;
    }

    public User get(String username) throws DAOException{
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                user = scanUser(resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
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

        return user;
    }
}
