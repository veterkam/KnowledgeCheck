package com.epam.javatraining.knowledgecheck.service.tools;

import java.util.ArrayList;
import java.util.List;

public class AlertManager {
    private  List<Alert> alerts = new ArrayList<>();

    public  boolean isEmpty() {
        return alerts.isEmpty();
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public String getClear() {
        clear();
        return "";
    }

    public void clear() {
        alerts.clear();
    }

    public void push(String type, String message) {
        alerts.add(new Alert(type, message));
    }

    public  void push(String type, List<String> messages) {
        for( String message : messages ) {
            push(type, message);
        }
    }

    public  void danger(String message) {
        push("danger", message);
    }

    public  void danger(List<String> messages) {
        push("danger", messages);
    }

    public  void warning(String message) {
        push("warning", message);
    }

    public  void warning(List<String> messages) {
        push("warning", messages);
    }

    public  void info(String message) {
        push("info", message);
    }

    public  void info(List<String> messages) {
        push("info", messages);
    }

    public  void success(String message) {
        push("success", message);
    }

    public  void success(List<String> messages) {
        push("success", messages);
    }
}
