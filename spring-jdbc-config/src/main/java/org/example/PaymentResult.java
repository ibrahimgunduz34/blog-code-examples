package org.example;

public record PaymentResult(
        String accountId,
        String merchantId,
        double amount,
        String status
) {}
