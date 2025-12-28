package com.example;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DatabaseTransactionStorage implements TransactionStorage {
    private final String datasourceUrl;
    private final String dbUser;
    private final String dbPassword;

    @Inject
    public DatabaseTransactionStorage(
            @Named("datasource.url") String datasourceUrl,
            @Named("datasource.username") String dbUser,
            @Named("datasource.password") String dbPassword) {
        this.datasourceUrl = datasourceUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    @Override
    public void savePaymentResult(PaymentResult paymentResult) {
        System.out.println(datasourceUrl);
    }
}
