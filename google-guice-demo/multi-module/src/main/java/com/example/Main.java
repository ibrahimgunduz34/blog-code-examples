package com.example;

import com.example.billing.BillingModule;
import com.example.billing.BillingService;
import com.example.payment.PaymentModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PaymentModule(),
                new BillingModule()
        );

        BillingService billingService = injector.getInstance(BillingService.class);
        billingService.charge("afafc9a3-346a-4dce-9909-ca65dc45fadc", BigDecimal.valueOf(100));
    }
}