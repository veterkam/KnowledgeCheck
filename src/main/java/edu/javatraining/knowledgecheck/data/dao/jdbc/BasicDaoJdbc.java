package edu.javatraining.knowledgecheck.data.dao.jdbc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.ResultSetReader;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.StatementWriter;
import edu.javatraining.knowledgecheck.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.*;

@Singleton
public class BasicDaoJdbc {
    protected static final Logger logger = LogManager.getLogger("DAO");


    protected ConnectionPool connectionPool;

    private Connection singleConn;
    private boolean isSingleConnOwner;

    public BasicDaoJdbc(ConnectionPool pool) {
        connectionPool = pool;
        isSingleConnOwner = false;
        singleConn = null;

        logger.trace("BasicDaoJdbc constructor: ConnectionPool " + pool);
    }

    public BasicDaoJdbc(Connection connection) {
        singleConn = connection;
        isSingleConnOwner = false;
        connectionPool = null;
    }

    protected void enableTransactionControl() {
        if(singleConn == null) {
            singleConn = connectionPool.getConnection();
            isSingleConnOwner = true;
        }
    }

    protected void disableTransactionControl() {
        if(singleConn != null && isSingleConnOwner) {
            connectionPool.releaseConnection(singleConn);
            singleConn = null;
        }
    }

    protected Connection getConnection() {

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

    protected void tryCommit(Connection conn) {
        if(singleConn != conn || isSingleConnOwner) {
            try {
                conn.commit();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new DAOException(e.getMessage(), e);
            }
        }
    }

    protected void tryRollback(Connection conn) {
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

    public BigInteger getGenKey(PreparedStatement ps) throws SQLException {
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

    protected boolean update(String sql, StatementWriter writer) {

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            writer.write(statement);

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    protected void select(String sql, StatementWriter writer, ResultSetReader reader) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            writer.write(statement);
            resultSet = statement.executeQuery();

            reader.read(resultSet);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }
    }

    public Long count(String sql) {
        PrimitiveEnvelope<Long> count = new PrimitiveEnvelope<Long>();

        select(sql,
                (statement) -> {},
                (resultSet) -> {
                    if(resultSet.next()) {
                        count.value = resultSet.getLong(1);
                    }
                });

        return count.value;
    }

    protected Long insert(String sql, StatementWriter writer) {

        Long resultId;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);
            writer.write(statement);

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                resultId = getGenKey(statement).longValue();
            } else {
                DAOException e = new DAOException("Inserting data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return resultId;
    }

    protected boolean delete(String sql, StatementWriter writer) {

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            writer.write(statement);

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowDeleted;
    }
}
