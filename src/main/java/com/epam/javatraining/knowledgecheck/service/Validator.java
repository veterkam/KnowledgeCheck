package com.epam.javatraining.knowledgecheck.service;


import com.epam.javatraining.knowledgecheck.model.entity.*;
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

    private final int FIRST_NAME_MAX_LENGTH = 50;
    private final int LAST_NAME_MAX_LENGTH = 50;
    private final int EMAIL_MAX_LENGTH = 50;
    private final int USERNAME_MAX_LENGTH = 50;

    private final int POSITION_MAX_LENGTH = 100;
    private final int SCIENTIFIC_DEGREE_MAX_LENGTH = 100;
    private final int ACADEMIC_TITLE_MAX_LENGTH = 100;

    private final int SPECIALTY_MAX_LENGTH = 100;
    private final int GROUP_MAX_LENGTH = 10;

    private final int SUBJECT_NAME_MAX_LENGTH = 100;

    private final int TEST_TITLE_MAX_LENGTH = 100;
    private final int TEST_DESCRIPTION_MAX_LENGTH = 1000;
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
    
    public boolean validate(User user) {
        switch (user.getRole()) {
            case STUDENT:
                return validateStudent((Student) user);
            case TUTOR:
                return validateTutor((Tutor) user);
            default:
                return validateUser(user);
        }
    }

    public boolean validateUser(User user) {
        boolean result = true;
        if( validateFirstName(user.getFirstname()) ) {
            result = false;
        }

        if( validateLastName(user.getLastname()) ) {
            result = false;
        }

        if( validateEmail(user.getEmail()) ) {
            result = false;
        }

        if( validateUsername(user.getUsername()) ) {
            result = false;
        }

        if( validatePassword(user.getPassword()) ) {
            result = false;
        }

        return result;
    }

    public boolean validateStudent(Student student) {
        boolean result = validateUser(student);

        if ( student.getSpecialty() != null && student.getSpecialty().length() > SPECIALTY_MAX_LENGTH ) {
            errors.add("Length of field student should not be greater " + SPECIALTY_MAX_LENGTH);
            result = false;
        }

        if ( student.getGroup() != null && student.getGroup().length() > GROUP_MAX_LENGTH ) {
            errors.add("Length of field group should not be greater " + GROUP_MAX_LENGTH);
            result = false;
        }

        return result;
    }

    public boolean validateTutor(Tutor tutor) {
        boolean result = validateUser(tutor);

        if ( tutor.getPosition() != null && tutor.getPosition().length() > POSITION_MAX_LENGTH) {
            errors.add("Length of field position should not be greater " + POSITION_MAX_LENGTH);
            result = false;
        }

        if ( tutor.getScientificDegree() != null && tutor.getScientificDegree().length() > SCIENTIFIC_DEGREE_MAX_LENGTH) {
            errors.add("Length of field scientific degree should not be greater " + SCIENTIFIC_DEGREE_MAX_LENGTH);
            result = false;
        }

        if ( tutor.getAcademicTitle() != null && tutor.getAcademicTitle().length() > ACADEMIC_TITLE_MAX_LENGTH) {
            errors.add("Length of field academic title should not be greater " + ACADEMIC_TITLE_MAX_LENGTH);
            result = false;
        }

        return result;
    }

    public boolean validate(Subject subject) {
        boolean result = true;

        if ( subject.getName() != null && subject.getName().length() > SUBJECT_NAME_MAX_LENGTH) {
            errors.add("Length of field subject name should not be greater " + SUBJECT_NAME_MAX_LENGTH);
            result = false;
        }

        return result;
    }

    public boolean validate(Answer answer) {
        return validateAnswerDescritption(answer.getDescription());
    }

    public boolean validate(Question question) {
        return validateQuestionDescritption(question.getDescription());
    }

    public boolean validate(Test test) {
        boolean result = true;
        if(validateTestTitle(test.getTitle())) {
            result = false;
        }

        if(validateTestDescritption(test.getDescription())) {
            result = false;
        }

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
        return isNotBlank(password, fieldName);
    }

    public boolean validateEmail(String email) {

        String error = validate(email, EMAIL_PATTERN, EMAIL_MAX_LENGTH);

        if(error != null) {
            error = String.format(error, "e-mail");
            errors.add(error);
        }

        return error == null;
    }

    private boolean validateTestTitle(String title) {
        return isNotBlank(title, "test title", TEST_TITLE_MAX_LENGTH);
    }

    private boolean validateTestDescritption(String description) {
        return isNotBlank(description, "test description", TEST_DESCRIPTION_MAX_LENGTH);
    }

    private boolean validateQuestionDescritption(String description) {
        return isNotBlank(description, "question description", QUESTION_DESCRIPTION_MAX_LENGTH);
    }

    private boolean validateAnswerDescritption(String description) {
        return isNotBlank(description, "answer description", ANSWER_DESCRIPTION_MAX_LENGTH);
    }

    public boolean isNotBlank(String str) {
        return isNotBlank(str, "field");
    }


    public boolean isNotBlank(String str, String fieldName) {
        return isNotBlank(str, fieldName, 65000);
    }

    private boolean isNotBlank(String str, String fieldName, int maxlen) {
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
            return "Length of field %s should not be greater " + maxlen;
        }

        if(regexp != null) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(str);

            if (!matcher.matches()) {
                return "Invalid string format of field %s";
            }
        }

        return null;
    }

}


