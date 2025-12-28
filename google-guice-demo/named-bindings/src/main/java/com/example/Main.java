package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        HsbcPaymentProvider paymentProvider = injector.getInstance(HsbcPaymentProvider.class);
        paymentProvider.charge(new CardData(), BigDecimal.valueOf(100));
    }
}