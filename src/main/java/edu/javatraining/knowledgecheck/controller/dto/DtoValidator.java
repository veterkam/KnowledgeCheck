package edu.javatraining.knowledgecheck.controller.dto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class DtoValidator {

    public static <T> Map<String, List<String>> validate(T obj) {

        Map<String, List<String>> errors = new HashMap<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> constraintViolations = validator.validate( obj );

        for( ConstraintViolation<T> error : constraintViolations ) {

            String field = error.getPropertyPath().toString();
            String msg = error.getMessage();

            if(!errors.containsKey(field)) {
                errors.put(field, new ArrayList<>());
            }

            errors.get(field).add(msg);
        }

        return errors;
    }
}
