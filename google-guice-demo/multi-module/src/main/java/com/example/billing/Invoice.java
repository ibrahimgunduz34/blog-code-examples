package com.example.billing;

import com.example.payment.PaymentReceipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Invoice {
    private final Long id;
    private final String invoiceNumber;
    private final LocalDateTime timeStamp;
    private final BigDecimal totalAmount;
    private final PaymentReceipt paymentReceipt;

    public Invoice(Long id, String invoiceNumber, LocalDateTime timeStamp, BigDecimal totalAmount, PaymentReceipt paymentReceipt) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.timeStamp = timeStamp;
        this.totalAmount = totalAmount;
        this.paymentReceipt = paymentReceipt;
    }

    public Long getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public PaymentReceipt getPaymentReceipt() {
        return paymentReceipt;
    }

    public static Invoice forPaymentReceipt(PaymentReceipt paymentReceipt) {
        return new Invoice(
                null,
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                paymentReceipt.getPaymentAmount(),
                paymentReceipt
        );
    }
}
