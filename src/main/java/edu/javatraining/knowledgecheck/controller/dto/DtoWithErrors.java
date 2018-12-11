package edu.javatraining.knowledgecheck.controller.dto;

import java.util.List;
import java.util.Map;

public interface DtoWithErrors {
    void setErrors(Map<String, List<String>> errors);
    Map<String, List<String>> getErrors();
}
