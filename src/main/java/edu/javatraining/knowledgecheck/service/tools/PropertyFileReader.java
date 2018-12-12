package edu.javatraining.knowledgecheck.service.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    private static final Logger logger = LogManager.getLogger("service");

    public static String read(String filename, String property) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(filename);

            // load a properties file
            prop.load(input);
            return prop.getProperty(property);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
