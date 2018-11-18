import com.epam.javatraining.knowledgecheck.service.Cipher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class MainSuite {
    private static final Logger logger = LogManager.getLogger("MainSuite");
    @Test
    public void main() {
        String original = "Licensed to the Apache Software Foundation (ASF) under one or more\n" +
                "  contributor license agreements.  See the NOTICE file distributed with\n" +
                "  this work for additional information regarding copyright ownership.\n" +
                "\tThe ASF licenses this file to You under the Apache License, Version 2.0\n" +
                "  (the \"License\"); you may not use this file except in compliance with\n" +
                "  the License.  You may obtain a copy of the License at" +
                "NOTE:  By default, no user is included in the \"manager-gui\" role required\n" +
                "  to operate the \"/manager/html\" web application.  If you wish to use this app,\n" +
                "  you must define such a user - the username and password are arbitrary. It is\n" +
                "  strongly recommended that you do NOT use one of the users in the commented out\n" +
                "  section below since they are intended for use with the examples web\n" +
                "  application.\n" +
                "-->\n" +
                "<!--\n" +
                "  NOTE:  The sample user and role entries below are intended for use with the\n" +
                "  examples web application. They are wrapped in a comment and thus are ignored\n" +
                "  when reading this file. If you wish to configure these users for use with the\n" +
                "  examples web application, do not forget to remove the <!.. ..> that surrounds\n" +
                "  them. You will also need to set the passwords to something appropriate.";

        String test = "Licensed to the Apache Software Foundation (ASF) under one or more\n" +
                "  contributor license agreements.  See the NOTICE file distributed with\n" +
                "  this work for additional information regarding copyright ownership.\n" +
                "\tThe ASF licenses this file to You under the Apache License, Version 2.0\n" +
                "  (the \"License\"); you may not use this file except in compliance with\n" +
                "  the License.  You may obtain a copy of the License at" +
                "NOTE:  By default, no user is included in the \"manager-gui\" role required\n" +
                "  to operate the \"/manager/html\" web application.  If you wish to use this app,\n" +
                "  you must define such a user - the username and password are arbitrary. It is\n" +
                "  strongly recommended that you do NOT use one of the users in the commented out\n" +
                "  section below since they are intended for use with the examples web\n" +
                "  application.\n" +
                "-->\n" +
                "<!--\n" +
                "  NOTE:  The sample user and role entries below are intended for use with the\n" +
                "  examples web application. They are wrapped in a comment and thus are ignored\n" +
                "  when reading this file. If you wish to configure these users for use with the\n" +
                "  examples web application, do not forget to remove the <!.. ...> that surrounds\n" +
                "  them. You will also need to set the passwords to something appropriate.";



        try {
            String code = Cipher.encode(original);
            boolean equal = Cipher.validate(test, code);
            if(equal) {
                logger.trace("success!");
            } else {
                logger.trace("failed!");
            }

            equal = Cipher.validate(original, code);
            if(equal) {
                logger.trace("success!");
            } else {
                logger.trace("failed!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
