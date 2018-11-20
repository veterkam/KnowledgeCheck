package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BasicDao {

    public UserDao() {
        super();
    }

    public int insert(User user) throws DAOException {
        int resultId;
        String sql = "INSERT INTO users (firstname, lastname, email, role, username, password, verified) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getRole().ordinal());
            statement.setString(5, user.getUsername());
            statement.setString(6, user.getPassword());
            statement.setBoolean(7, user.isVerified());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                resultId = getGenKey(statement).intValue();
                user.setId(resultId);
            } else {
                DAOException e = new DAOException("Inserting user data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting user data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return resultId;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        extractUserFromResultSet(user, resultSet);
        return user;
    }

    protected void extractUserFromResultSet(User out, ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstname = resultSet.getString("firstname");
        String lastname = resultSet.getString("lastname");
        String email = resultSet.getString("email");
        int roleInt = resultSet.getInt("role");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        boolean verified = resultSet.getBoolean("verified");

        User.Role role = User.Role.fromOrdinal(roleInt);

        out.setId(id);
        out.setFirstname(firstname);
        out.setLastname(lastname);
        out.setEmail(email);
        out.setRole(role);
        out.setUsername(username);
        out.setPassword(password);
        out.setVerified(verified);
    }

    public List<User> getList(long offset, long count) throws DAOException {
        List<User> listUser = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, offset);
            statement.setLong(2, count);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                listUser.add(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return listUser;
    }

    public boolean delete(User user) throws DAOException {
        return delete(user.getId());
    }

    public boolean delete(int userId) throws DAOException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting user data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowDeleted;
    }

    public boolean update(User user) throws DAOException {

        String sql = "UPDATE users SET " +
                        "firstname = ?, " +
                        "lastname = ?, " +
                        "email = ?, " +
                        "role = ?, " +
                        "username = ?, " +
                        "password = ?, " +
                        "verified = ? " +
                        "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getRole().ordinal());
            statement.setString(5, user.getUsername());
            statement.setString(6, user.getPassword());
            statement.setBoolean(7, user.isVerified());
            statement.setInt(8, user.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating user data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    public boolean updatePassword(User user) throws DAOException {

        String sql = "UPDATE users SET " +
                "password = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getPassword());
            statement.setInt(2, user.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating user data failed.", e);
        } finally {
            closeCommunication(connection, statement);
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
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                user = extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
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
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                user = extractUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return user;
    }

    public int getUserCount() throws DAOException {
        String sql = "SELECT COUNT(*) FROM users";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int result = 0;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }
        return result;
    }
}
