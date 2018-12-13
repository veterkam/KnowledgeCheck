package edu.javatraining.knowledgecheck.tools;

import javax.servlet.http.HttpServletRequest;

public class LocaleMsgReader {
    private static final String RESOURCE_BASE = "/locale/messages";
    private static final String RESOURCE_EXTENSION = ".properties";

    public static String message(String lang, String key) {

        lang = (lang != null && lang.length() != 0) ? "_" + lang : "";
        String resource = RESOURCE_BASE + lang + RESOURCE_EXTENSION;
        String msg = PropertyFileReader.read(resource, key);

        if(msg == null) {
            resource = RESOURCE_BASE + RESOURCE_EXTENSION;
            msg = PropertyFileReader.read(resource, key);
        }

        if(msg == null) {
            msg = key;
        }

        return msg;
    }

    public static String message(HttpServletRequest request, String key) {

        String lang = (String) request.getSession().getAttribute("lang");

        return message(lang, key);
    }

    public static String message(String lang, String key, Object... args) {

        String msg = message(lang, key);
        return String.format(msg, args);
    }

    public static String message(HttpServletRequest request, String key, Object... args) {

        String msg = message(request, key);
        return String.format(msg, args);
    }
}
