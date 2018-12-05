package edu.javatraining.knowledgecheck.exception;

public class DAOException extends RuntimeException {
    private String description;

    public DAOException(String description) {
        this.description = description;
    }

    public DAOException(String message, Exception e) {
        super(message, e);
        this.description = message;
    }

    public String toString() {
        return "DAOException: " + description;
    }
}
