package com.epam.javatraining.knowledgecheck.datalayer.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConnectionPool {

    private static int INITIAL_RESERVE_SIZE = 5;

    private String url;
    private String user;
    private String password;
    private Stack<Connection> reserve;
    private List<Connection> usedConnections;

    public ConnectionPool(String url, String user, String password)
        throws SQLException, ClassNotFoundException {

        // Allocate a database Connection object
        Class.forName("com.mysql.cj.jdbc.Driver");

        usedConnections = new ArrayList<>();
        reserve = new Stack<>();
        for(int i = 0; i < INITIAL_RESERVE_SIZE; i++) {
            Connection connection = DriverManager.getConnection(url, user, password);
            reserve.push(connection);
        }
    }

    public Connection getConnection() throws SQLException {

        if(reserve.empty()) {
            int length = reserve.capacity();
            for(int i = 0; i < length; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                reserve.push(connection);
            }
        }

        Connection connection = reserve.pop();
        usedConnections.add(connection);
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        reserve.push(connection);
        return usedConnections.remove(connection);
    }

    public void release() throws SQLException {

        for(Connection connection : usedConnections) {
            connection.close();
        }

        for (Connection connection : reserve ){
            connection.close();
        }

        usedConnections.clear();
        reserve.clear();
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
