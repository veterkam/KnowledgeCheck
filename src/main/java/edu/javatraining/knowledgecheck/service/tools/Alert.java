package edu.javatraining.knowledgecheck.service.tools;

public class Alert {
    private String type;
    private String message;
    private boolean isKeyi18n = true;

    public Alert() {
    }

    public Alert(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Alert(String type, String message, boolean isKeyi18n) {
        this.type = type;
        this.message = message;
        this.isKeyi18n = isKeyi18n;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isKeyi18n() {
        return isKeyi18n;
    }

    public boolean getIsKeyi18n() {
        return isKeyi18n;
    }

    public void setKeyi18n(boolean keyi18n) {
        isKeyi18n = keyi18n;
    }
}
