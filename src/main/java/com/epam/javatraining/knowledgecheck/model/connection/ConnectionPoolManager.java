package com.epam.javatraining.knowledgecheck.model.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ConnectionPoolManager {
    private static final Logger logger = LogManager.getLogger("connection");
    private static ConnectionPool connectionPool = null;

    public static void init(String url, String user, String password)
        throws SQLException, ClassNotFoundException {
        connectionPool = new ConnectionPool(url, user, password);
    }

    public static ConnectionPool getConnectionPool() {
        if(connectionPool == null) {
            String msg = "ConnectionPoolManager is not initialized.";
            logger.error(msg);
            throw new NullPointerException(msg);
        }
        return connectionPool;
    }
}
