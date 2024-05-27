package org.example.api.dto;

public class GreetingModel {
    private final String message;

    public GreetingModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
