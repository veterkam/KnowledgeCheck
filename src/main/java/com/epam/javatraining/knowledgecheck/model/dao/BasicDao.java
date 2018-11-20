package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPool;
import com.epam.javatraining.knowledgecheck.model.connection.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.*;

public class BasicDao {
    protected static final Logger logger = LogManager.getLogger("DAO");
    protected ConnectionPool connectionPool;
    private Connection singleConn;
    private boolean isSingleConnOwner;


    public BasicDao() {
        connectionPool = ConnectionPoolManager.getConnectionPool();
        isSingleConnOwner = false;
        singleConn = null;
    }

    public BasicDao(Connection connection) {
        singleConn = connection;
        isSingleConnOwner = false;
        connectionPool = null;
    }

    protected void activateTransactionControl() throws DAOException {
        if(singleConn == null) {
            try {
                singleConn = connectionPool.getConnection();
                isSingleConnOwner = true;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new DAOException(e.getMessage(), e);
            }
        }
    }

    protected void deactivateTransactionControl() throws DAOException {
        if(singleConn != null && isSingleConnOwner) {
            connectionPool.releaseConnection(singleConn);
            singleConn = null;
        }
    }

    protected Connection getConnection() throws DAOException {

        Connection conn;
        try {
            conn = (singleConn != null) ? singleConn : connectionPool.getConnection();
            conn.setAutoCommit(singleConn == null);
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(e.getMessage(), e);
        }

        return conn;
    }

    protected void tryCommit(Connection conn) throws DAOException {
        if(singleConn != conn || isSingleConnOwner) {
            try {
                conn.commit();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new DAOException(e.getMessage(), e);
            }
        }
    }

    protected void tryRollback(Connection conn) throws DAOException {
        if (singleConn != conn || isSingleConnOwner) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new DAOException(e.getMessage(), e);
            }
        }
    }

    protected void closeCommunication(Connection connection) {
        closeCommunication(connection, null, null);
    }

    protected void closeCommunication(Connection connection, Statement statement) {
        closeCommunication(connection, statement, null);
    }

    protected void closeCommunication(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if(resultSet != null) {
                resultSet.close();
            }
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // do nothing
            logger.error(e.getMessage(), e);
        } finally {
            if(connection != null && connection != singleConn) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public BigInteger getGenKey(PreparedStatement ps) throws SQLException, DAOException {
        BigInteger resultId;
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                resultId = (BigInteger) generatedKeys.getObject(1);
            } else {
                DAOException e = new DAOException("Creating Data Base record failed, no ID obtained.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        }

        return resultId;
    }
}
