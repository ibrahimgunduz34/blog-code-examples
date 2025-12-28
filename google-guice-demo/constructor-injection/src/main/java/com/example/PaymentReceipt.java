package com.example;

import java.math.BigDecimal;

public class PaymentReceipt {
    private final String transactionId;
    private final PaymentStatus paymentStatus;
    private final BigDecimal paymentAmount;
    private final String description;

    public PaymentReceipt(String transactionId, PaymentStatus paymentStatus, BigDecimal paymentAmount, String description) {
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
        this.description = description;
    }

    public static PaymentReceipt forSuccess(PaymentResult paymentResult, BigDecimal paymentAmount) {
        return new PaymentReceipt(
                paymentResult.getTransactionId(),
                PaymentStatus.SUCCESSFUL,
                paymentAmount,
                null);
    }

    public static PaymentReceipt forFailure(PaymentResult paymentResult, BigDecimal paymentAmount) {
        return new PaymentReceipt(
                null,
                PaymentStatus.FAILED,
                paymentAmount,
                paymentResult.getMessage()
        );
    }

    public static PaymentReceipt forException(PaymentProcessingException exception, BigDecimal paymentAmount) {
        return new PaymentReceipt(
                null,
                PaymentStatus.FAILED,
                paymentAmount,
                exception.getMessage()
        );
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
}
