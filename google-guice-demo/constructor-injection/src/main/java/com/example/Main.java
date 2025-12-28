package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());

        PaymentProcessor paymentProcessor = injector.getInstance(PaymentProcessor.class);
        paymentProcessor.processPayment("afafc9a3-346a-4dce-9909-ca65dc45fadc", BigDecimal.valueOf(100));
        // ...
    }
}
