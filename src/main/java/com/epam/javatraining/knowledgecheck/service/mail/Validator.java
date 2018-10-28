package com.epam.javatraining.knowledgecheck.service.mail;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private List<String> errors = new ArrayList<>();

    private final String NAME_PATTERN = "[a-zA-Zа-яА-ЯёЁ]+([\\sa-zA-Zа-яА-ЯёЁ-]*[a-zA-Zа-яА-ЯёЁ]+)*$";
    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final String USERNAME_PATTERN = "^[_A-Za-z0-9-]+$";
    private final String NOT_BLANK_PATTERN = "^\\S+";

    private final int FIRST_NAME_MAX_LENGTH = 20;
    private final int LAST_NAME_MAX_LENGTH = 20;
    private final int EMAIL_MAX_LENGTH = 50;
    private final int USERNAME_MAX_LENGTH = 20;
    private final int PASSWORD_MAX_LENGTH = 30;

    public Validator() {

    }

    public List<String> getErrors() {
        return errors;
    }

    public void reset() {
        errors.clear();
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    /**
     *
     * @param objects
     * @return true if list of objects contains null pointer
     */
    public static boolean containNull(Object... objects) {
        for(Object obj : objects) {
            if(obj == null) {
                return true;
            }
        }

        return false;
    }

    public boolean validateFirstName(String firstName) {
        String error = validate(firstName, NAME_PATTERN, FIRST_NAME_MAX_LENGTH);

        if(error != null) {
            error = String.format(error, "first name");
            errors.add(error);
        }

        return error == null;
    }

    public boolean validateLastName(String lastName) {
        String error = validate(lastName, NAME_PATTERN, LAST_NAME_MAX_LENGTH);

        if(error != null) {
            error = String.format(error, "last name");
            errors.add(error);
        }

        return error == null;
    }



    public boolean validateUsername(String username) {
        String error = validate(username, USERNAME_PATTERN, USERNAME_MAX_LENGTH);

        if(error != null) {
            error = String.format(error, "username");
            errors.add(error);
        }

        return error == null;
    }

    public boolean validatePassword(String password) {
        return validatePassword(password, "password");
    }

    public boolean validatePassword(String password, String fieldName) {

        if(fieldName == null) {
            fieldName = "password";
        }

        String error = validate(password, null, PASSWORD_MAX_LENGTH);

        if(error != null) {
            error = String.format(error, fieldName);
            errors.add(error);
        }

        return error == null;
    }

    public boolean validateEmail(String email) {

        String error = validate(email, EMAIL_PATTERN, EMAIL_MAX_LENGTH);

        if(error != null) {
            error = String.format(error, "e-mail");
            errors.add(error);
        }

        return error == null;
    }

    public boolean isNotBlank(String str) {
        return isNotBlank(str, "field");
    }

    public boolean isNotBlank(String str, String fieldName) {

        if(fieldName == null) {
            fieldName = "field";
        }

        String error = validate(str, NOT_BLANK_PATTERN, 65000);

        if(error != null) {
            error = String.format(error, fieldName);
            errors.add(error);
        }

        return error == null;
    }

    private String validate(String str, String regexp, int maxlen) {
        if(str == null) {
            return "%s is null";
        }

        if(str.isEmpty()) {
            return "%s is empty";
        }

        if(str.length() > maxlen) {
            return "length of %s should not be greater " + maxlen;
        }

        if(regexp != null) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(str);

            if (!matcher.matches()) {
                return "invalid string format of %s";
            }
        }

        return null;
    }

}


