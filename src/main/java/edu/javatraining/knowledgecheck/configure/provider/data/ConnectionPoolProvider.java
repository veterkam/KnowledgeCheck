package edu.javatraining.knowledgecheck.configure.provider.data;

import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.connection.impl.ConnectionPoolJdbc;
import edu.javatraining.knowledgecheck.tools.PropertyFileReader;
import edu.javatraining.knowledgecheck.tools.SqlScriptRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionPoolProvider implements Provider<ConnectionPool> {

    private static final Logger logger = LogManager.getLogger("Configure");

    @Override
    public ConnectionPool get() {

        Properties props = PropertyFileReader.read("/configure.properties");

        ConnectionPool pool = new ConnectionPoolJdbc(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

        prepareDataBase(pool);

        return pool;
    }

    private void prepareDataBase(ConnectionPool connectionPool) {
        logger.trace("Check&Prepare Data Base");
        try {
            URL resource = getClass().getResource("/init.sql");
            File file = new File(resource.getFile());
            Reader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));

            SqlScriptRunner runner = new SqlScriptRunner(connectionPool);
            runner.runScript(reader);
        } catch(SQLException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
