package com.example.payment;

import java.math.BigDecimal;

public interface PaymentProcessor {
    PaymentReceipt processPayment(String cardToken, BigDecimal paymentAmount);
}
