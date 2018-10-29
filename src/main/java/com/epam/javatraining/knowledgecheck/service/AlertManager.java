package com.epam.javatraining.knowledgecheck.service;

import java.util.ArrayList;
import java.util.List;

public class AlertManager {
    private List<Alert> alerts = new ArrayList<>();

    public AlertManager() {
    }

    public boolean isEmpty() {
        return alerts.isEmpty();
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void reset() {
        alerts.clear();
    }

    public void push(String type, String message) {
        alerts.add(new Alert(type, message));
    }

    public void danger(String message) {
        push("danger", message);
    }

    public void warning(String message) {
        push("warning", message);
    }

    public void info(String message) {
        push("info", message);
    }

    public void success(String message) {
        push("success", message);
    }
}
