package com.example;

public class PaymentResult {
    private final String transactionId;
    private final PaymentStatus paymentStatus;
    private final String message;

    public PaymentResult(String transactionId, PaymentStatus paymentStatus, String message) {
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public boolean isSuccessful() {
        return paymentStatus.equals(PaymentStatus.SUCCESSFUL);
    }

    public String getMessage() {
        return message;
    }
}
