package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        LoggingService loggingService = injector.getInstance(LoggingService.class);
        loggingService.info("Test message");

        LoggingService loggingService1 = injector.getInstance(LoggingService.class);
        loggingService1.info("Test message");
    }
}