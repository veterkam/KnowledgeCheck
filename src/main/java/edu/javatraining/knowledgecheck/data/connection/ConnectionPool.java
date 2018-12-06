package edu.javatraining.knowledgecheck.data.connection;

import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();
    boolean releaseConnection(Connection connection);

}
