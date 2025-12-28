package com.example.payment;

import com.google.inject.AbstractModule;

public class PaymentModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PaymentProvider.class).to(HsbcPaymentProvider.class);
        bind(TransactionStorage.class).to(DatabaseTransactionStorage.class);
        bind(CardVault.class).to(ExternalCardVault.class);
        bind(PaymentProcessor.class).to(CardPaymentProcessor.class);
    }
}
