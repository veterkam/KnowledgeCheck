package edu.javatraining.knowledgecheck.service.tools;

import javax.servlet.http.HttpServletRequest;

public class LocaleMsgReader {
    private static final String FILE_BASE = "/locale/messages";
    private static final String FILE_EXTENSION = ".properties";

    public static String message(String lang, String key) {

        lang = (lang != null && lang.length() != 0) ? "_" + lang : "";
        String filename = LocaleMsgReader.class
                .getResource(FILE_BASE + lang + FILE_EXTENSION)
                .getFile();
        String msg = PropertyFileReader.read(filename, key);

        if(msg == null) {
            filename = FILE_BASE + FILE_EXTENSION;
            msg = PropertyFileReader.read(filename, key);
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

        String messge = message(lang, key);
        return String.format(messge, args);
    }

    public static String message(HttpServletRequest request, String key, Object... args) {

        String messge = message(request, key);
        return String.format(messge, args);
    }
}
