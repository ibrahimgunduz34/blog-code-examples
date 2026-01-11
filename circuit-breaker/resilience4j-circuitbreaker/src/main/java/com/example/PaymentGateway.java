package com.example;

import java.math.BigDecimal;

public interface PaymentGateway {
    AuthorizationResult authorize(String token, Integer amount);
    String getStatus(String transactionId);
}
