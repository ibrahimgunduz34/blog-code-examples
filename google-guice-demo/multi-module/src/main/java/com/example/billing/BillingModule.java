package com.example.billing;

import com.google.inject.AbstractModule;

public class BillingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BillingStorage.class).to(DatabaseBillingStorage.class);
    }
}
