package edu.javatraining.knowledgecheck.data.connection.impl;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolJdbc implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger("connection");

    private final int INITIAL_RESERVE_SIZE = 5;

    private String url;
    private String user;
    private String password;

    private Stack<Connection> reserve;
    private List<Connection> usedConnections;
    private Lock locker;

    public ConnectionPoolJdbc(String url, String user, String password) {

        this.url = url;
        this.user = user;
        this.password = password;

        try {
            // Allocate a database Connection object
            Class.forName("com.mysql.cj.jdbc.Driver");

            usedConnections = new ArrayList<>();
            reserve = new Stack<>();
            addConnections(INITIAL_RESERVE_SIZE);

        } catch(ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(e.getMessage(), e);
        }

        locker = new ReentrantLock();
    }

    @Override
    public Connection getConnection() {

        locker.lock();
        try {
            if (reserve.empty()) {
                addConnections( reserve.capacity() );
            }
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(e.getMessage(), e);
        }

        Connection connection = reserve.pop();
        usedConnections.add(connection);
        locker.unlock();
        return connection;
    }

    private void addConnections(int count) throws SQLException {

        for (int i = 0; i < count; i++) {
            Connection connection = DriverManager.getConnection(url, user, password);
            reserve.push(connection);
        }
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        locker.lock();
        try {
            if (connection.isValid(0)) {
                reserve.push(connection);
            }
        } catch (SQLException e) {
            // do nothing
            logger.error(e.getMessage(), e);
        }

        boolean result = usedConnections.remove(connection);
        locker.unlock();

        return result;
    }

    public void release() throws DAOException {
        locker.lock();
        try {
            for (Connection connection : usedConnections) {
                connection.close();
            }

            for (Connection connection : reserve) {
                connection.close();
            }
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(e.getMessage(), e);
        }

        usedConnections.clear();
        reserve.clear();
        locker.unlock();
    }

    public int getSize() {
        return reserve.size() + usedConnections.size();
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
