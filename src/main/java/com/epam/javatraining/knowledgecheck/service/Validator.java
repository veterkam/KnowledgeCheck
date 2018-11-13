package com.epam.javatraining.knowledgecheck.service;


import com.epam.javatraining.knowledgecheck.model.entity.User;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private List<String> errors = new ArrayList<>();

    private final String NAME_PATTERN = "[a-zA-Zа-яА-ЯёЁ]+([\\sa-zA-Zа-яА-ЯёЁ-]*[a-zA-Zа-яА-ЯёЁ]+)*$";
    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final String USERNAME_PATTERN = "^[_A-Za-z0-9-]+$";

    private final int FIRST_NAME_MAX_LENGTH = 20;
    private final int LAST_NAME_MAX_LENGTH = 20;
    private final int EMAIL_MAX_LENGTH = 50;
    private final int USERNAME_MAX_LENGTH = 20;
    private final int PASSWORD_MAX_LENGTH = 30;

    private final int TEST_TITLE_MAX_LENGTH = 100;
    private final int TEST_DESCRIPTION_MAX_LENGTH = 10000;
    private final int QUESTION_DESCRIPTION_MAX_LENGTH = 1000;
    private final int ANSWER_DESCRIPTION_MAX_LENGTH = 1000;

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
    
    public boolean validateUser(User user) {
        boolean result =    validateFirstName(user.getFirstname()) &&
                            validateLastName(user.getLastname()) &&
                            validateEmail(user.getEmail()) &&
                            validateUsername(user.getUsername()) &&
                            validatePassword(user.getPassword());
        return result;
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

    public boolean validateTestTitle(String title) {
        return isNotBlank(title, "title", TEST_TITLE_MAX_LENGTH);
    }

    public boolean validateTestDescritption(String description) {
        return isNotBlank(description, "description", TEST_DESCRIPTION_MAX_LENGTH);
    }

    public boolean validateQuestionDescritption(String description) {
        return isNotBlank(description, "question description", QUESTION_DESCRIPTION_MAX_LENGTH);
    }

    public boolean validateAnswerDescritption(String description) {
        return isNotBlank(description, "answer description", ANSWER_DESCRIPTION_MAX_LENGTH);
    }

    public boolean isNotBlank(String str) {
        return isNotBlank(str, "field");
    }


    public boolean isNotBlank(String str, String fieldName) {
        return isNotBlank(str, fieldName, 65000);
    }

    public boolean isNotBlank(String str, String fieldName, int maxlen) {
        if(fieldName == null) {
            fieldName = "field";
        }

        String error = "";
        if(str == null) {
            error =  "Field " + fieldName + " is null";
        } else if(Strings.isBlank(str)) {
            error = "Field " + fieldName + " is blank";
        } else if(str.length() > maxlen) {
            error =  "Length of " + fieldName + " should not be greater " + maxlen;
        }

        if(!error.isEmpty()) {
            errors.add(error);
        }

        return error.isEmpty();
    }


    private String validate(String str, String regexp, int maxlen) {
        if(str == null) {
            return "Field %s is null";
        }

        if(str.isEmpty()) {
            return "Field %s is empty";
        }

        if(str.length() > maxlen) {
            return "Length of %s should not be greater " + maxlen;
        }

        if(regexp != null) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(str);

            if (!matcher.matches()) {
                return "Invalid string format of %s";
            }
        }

        return null;
    }

}


