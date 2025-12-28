package com.example.payment;

import com.google.inject.Inject;

import java.math.BigDecimal;

public class CardPaymentProcessor implements PaymentProcessor {
    private final CardVault cardVault;
    private final PaymentProvider paymentProvider;
    private final TransactionStorage transactionStorage;

    @Inject
    public CardPaymentProcessor(CardVault cardVault, PaymentProvider paymentProvider, TransactionStorage transactionStorage) {
        this.cardVault = cardVault;
        this.paymentProvider = paymentProvider;
        this.transactionStorage = transactionStorage;
    }

    public PaymentReceipt processPayment(String cardToken, BigDecimal paymentAmount) {
        try {
            CardData cardData = cardVault.getCardData(cardToken);
            PaymentResult paymentResult = paymentProvider.charge(cardData, paymentAmount);
            transactionStorage.savePaymentResult(paymentResult);

            return paymentResult.isSuccessful() ?
                    PaymentReceipt.forSuccess(paymentResult, paymentAmount) :
                    PaymentReceipt.forFailure(paymentResult, paymentAmount);
        } catch (PaymentProcessingException exception) {
            return PaymentReceipt.forException(exception, paymentAmount);
        }
    }
}
