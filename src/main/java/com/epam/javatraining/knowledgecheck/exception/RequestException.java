package com.epam.javatraining.knowledgecheck.exception;

public class RequestException extends Exception {
    private String description;

    public RequestException(String description) {
        this.description = description;
    }

    public RequestException(String message, Exception e) {
        super(message, e);
        this.description = message;
    }

    public String toString() {
        return "RequestException: " + description;
    }
}
