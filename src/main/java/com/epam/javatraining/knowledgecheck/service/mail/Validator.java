package com.epam.javatraining.knowledgecheck.service.mail;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean validateEmail(String email) {
        if(email == null) {
            return false;
        }
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static String blankIfNull(String s) {
        return (s == null) ? "" : s;
    }

}


