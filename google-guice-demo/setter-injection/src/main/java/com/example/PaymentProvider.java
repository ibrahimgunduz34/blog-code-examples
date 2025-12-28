package com.example;

import java.math.BigDecimal;

public interface PaymentProvider {
    PaymentResult charge(CardData cardData, BigDecimal paymentAmount);
}
