package edu.javatraining.knowledgecheck.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    private static final Logger logger = LogManager.getLogger("service");

    public static String read(String resource, String property) {

        Properties props = read(resource);

        return props.getProperty(property);
    }

    public static Properties read(String resource) {

        String filename = PropertyFileReader.class
                .getResource(resource)
                .getFile();

        Properties props = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(filename);

            // load a properties file
            props.load(input);
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

        return props;
    }
}
