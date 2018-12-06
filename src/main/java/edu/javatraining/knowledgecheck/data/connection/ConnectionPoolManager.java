package edu.javatraining.knowledgecheck.data.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ConnectionPoolManager {
    private static final Logger logger = LogManager.getLogger("connection");
    private static ConnectionPoolJdbc connectionPoolJdbc = null;

    public static void init(String url, String user, String password)
        throws SQLException, ClassNotFoundException {
        connectionPoolJdbc = new ConnectionPoolJdbc(url, user, password);
    }

    public static ConnectionPoolJdbc getConnectionPoolJdbc() {
        if(connectionPoolJdbc == null) {
            String msg = "ConnectionPoolManager is not initialized.";
            logger.error(msg);
            throw new NullPointerException(msg);
        }
        return connectionPoolJdbc;
    }
}
