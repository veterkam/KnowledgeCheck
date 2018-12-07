package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbc extends BasicDaoJdbc implements UserDao {

    public UserDaoJdbc(ConnectionPool pool) {
        super(pool);
    }

    public UserDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public Long save(User user) {

        Long id = user.getId();
        if(!update(user)) {
            id = insert(user);
        }

        return id;
    }

    @Override
    public User findOne(User user) {

        User result = null;

        if(user.getId() != null) {
            result = findOneById(user.getId());
        }

        if(result == null && user.getUsername() != null) {
            result = findOneByUsername(user.getUsername());
        }

        return result;
    }

    @Override
    public User findOneById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        PrimitiveEnvelope<User> user = new PrimitiveEnvelope<>();

        select(sql,

                (statement -> {
                    statement.setLong(1, id);
                }),

                (resultSet -> {
                    if(resultSet.next()) {
                        user.value = extractUserFromResultSet(resultSet);
                    }
                }));

        return user.value;
    }

    @Override
    public User findOneByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        PrimitiveEnvelope<User> user = new PrimitiveEnvelope<>();

        select(sql,

                (statement -> {
                    statement.setString(1, username);
                }),

                (resultSet -> {
                    if(resultSet.next()) {
                        user.value = extractUserFromResultSet(resultSet);
                    }
                }));

        return user.value;
    }

    @Override
    public List<User> findAllUsers() {
        return findAllUsers(null, null);
    }

    @Override
    public List<User> findAllUsers(Long offset, Long count) {

        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        if(offset != null) {
            sql += " LIMIT ?, ?";
        }

        select(sql,

                (statement -> {
                    if(offset != null) {
                        statement.setLong(1, offset);
                        statement.setLong(2, count);
                    }
                }),

                (resultSet -> {
                    while (resultSet.next()) {
                        User user = extractUserFromResultSet(resultSet);
                        userList.add(user);
                    }
                }));

        return userList;//.toArray(new User[userList.size()]);
    }

    @Override
    public Long count() {

        String sql = "SELECT COUNT(*) FROM users";
        return super.count(sql);
    }

    @Override
    public Long insert(User user) {

        String sql = "INSERT INTO users (firstname, lastname, email, role, username, password, verified) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        Long resultId = insert(sql,
                (statement) -> {
                    statement.setString(1, user.getFirstname());
                    statement.setString(2, user.getLastname());
                    statement.setString(3, user.getEmail());
                    statement.setInt(4, user.getRole().ordinal());
                    statement.setString(5, user.getUsername());
                    statement.setString(6, user.getPassword());
                    statement.setBoolean(7, user.isVerified());
                });

        user.setId(resultId);

        return resultId;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET " +
                "firstname = ?, " +
                "lastname = ?, " +
                "email = ?, " +
                "role = ?, " +
                "username = ?, " +
                "password = ?, " +
                "verified = ? " +
                "WHERE id = ?";

        return update(sql,
                (statement) -> {
                    statement.setString(1, user.getFirstname());
                    statement.setString(2, user.getLastname());
                    statement.setString(3, user.getEmail());
                    statement.setInt(4, user.getRole().ordinal());
                    statement.setString(5, user.getUsername());
                    statement.setString(6, user.getPassword());
                    statement.setBoolean(7, user.isVerified());
                    statement.setLong(8, user.getId());
                });
    }

    @Override
    public boolean delete(User user) {
        return deleteById(user.getId());
    }

    @Override
    public boolean deleteById(Long id) {

        String sql = "DELETE FROM users WHERE id = ?";
        return delete(sql, (statement) -> {
            statement.setLong(1, id);
        });
    }

    @Override
    public boolean updatePassword(User user) {

        String sql = "UPDATE users SET " +
                "password = ? " +
                "WHERE id = ?";

        return update(sql,
                (statement) -> {
                    statement.setString(1, user.getPassword());
                    statement.setLong(2, user.getId());
                });
    }


    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        extractUserFromResultSet(user, resultSet);
        return user;
    }

    protected void extractUserFromResultSet(User out, ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
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

}
