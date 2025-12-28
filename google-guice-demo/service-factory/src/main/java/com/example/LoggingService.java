package com.example;

import com.google.inject.Inject;

import java.net.http.HttpClient;

public class LoggingService {
    private final HttpClient httpClient;

    @Inject
    public LoggingService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void info(String message) {
        // ...
    }
}
