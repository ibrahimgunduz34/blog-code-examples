package com.example;

import com.google.inject.Inject;

import java.math.BigDecimal;

public class CardPaymentProcessor implements PaymentProcessor {
    private CardVault cardVault;
    private PaymentProvider paymentProvider;
    private TransactionStorage transactionStorage;

    @Inject
    public void setCardVault(CardVault cardVault) {
        this.cardVault = cardVault;
    }

    @Inject
    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    @Inject
    public void setTransactionStorage(TransactionStorage transactionStorage) {
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
