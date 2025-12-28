package com.example;

import java.math.BigDecimal;

public interface PaymentProcessor {
    PaymentReceipt processPayment(String cardToken, BigDecimal paymentAmount);
}
