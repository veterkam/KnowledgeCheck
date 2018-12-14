package edu.javatraining.knowledgecheck.tools;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.*;

public class SqlScriptRunner {
    private static final Logger logger = LogManager.getLogger("Tools");
    private static final String DEFAULT_DELIMITER = ";";

    private ConnectionPool connectionPool;
    private String delimiter = DEFAULT_DELIMITER;

    /**
     * Default constructor
     */
    public SqlScriptRunner(ConnectionPool pool) {
        this.connectionPool = pool;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter)
     *
     * @param reader
     *            - the source of the script
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        Connection connection = connectionPool.getConnection();
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.endsWith(getDelimiter())) {

                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    Statement statement = connection.createStatement();
                    statement.execute(command.toString());

                    command = null;
                    try {
                        statement.close();
                    } catch (Exception e) {
                        // Ignore
                    }

                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
        } catch (SQLException | IOException  e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
