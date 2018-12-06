package edu.javatraining.knowledgecheck.data;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.*;

public class SqlScriptRunner {
    private static final Logger logger = LogManager.getLogger(SqlScriptRunner.class);
    private static final String DEFAULT_DELIMITER = ";";

    private ConnectionPoolJdbc connectionPoolJdbc;
    private String delimiter = DEFAULT_DELIMITER;

    /**
     * Default constructor
     */
    public SqlScriptRunner() {
        this.connectionPoolJdbc = ConnectionPoolManager.getConnectionPoolJdbc();
    }

    /**
     * Runs an SQL script (read in using the Reader parameter)
     *
     * @param reader
     *            - the source of the script
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        Connection connection = connectionPoolJdbc.getConnection();
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
            connectionPoolJdbc.releaseConnection(connection);
        }
    }

    private String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
