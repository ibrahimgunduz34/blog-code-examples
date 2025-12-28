package com.example.payment;

import java.math.BigDecimal;

public class HsbcPaymentProvider implements PaymentProvider {
    @Override
    public PaymentResult charge(CardData cardData, BigDecimal paymentAmount) {
        return new PaymentResult(
                "00000000000",
                PaymentStatus.SUCCESSFUL,
                "APPROVED"
        );
    }
}
