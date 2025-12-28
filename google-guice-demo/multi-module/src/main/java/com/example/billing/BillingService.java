package com.example.billing;

import com.example.payment.PaymentProcessor;
import com.example.payment.PaymentReceipt;
import com.google.inject.Inject;

import java.math.BigDecimal;

public class BillingService {
    private final BillingStorage billingStorage;
    private final PaymentProcessor paymentProcessor;

    @Inject
    public BillingService(BillingStorage billingStorage, PaymentProcessor paymentProcessor) {
        this.billingStorage = billingStorage;
        this.paymentProcessor = paymentProcessor;
    }

    public Invoice charge(String paymentToken, BigDecimal paymentAmount) {
        PaymentReceipt paymentReceipt = paymentProcessor.processPayment(paymentToken, paymentAmount);
        Invoice invoice = Invoice.forPaymentReceipt(paymentReceipt);
        return billingStorage.saveInvoice(invoice);
    }
}
