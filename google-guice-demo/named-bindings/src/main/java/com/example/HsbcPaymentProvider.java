package com.example;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;

import java.math.BigDecimal;

public class HsbcPaymentProvider implements PaymentProvider {
    private final Logger logger;

    @Inject
    public HsbcPaymentProvider(@Named("appLogger") Logger logger) {
        this.logger = logger;
    }


    @Override
    public PaymentResult charge(CardData cardData, BigDecimal paymentAmount) {
        logger.info("The payment processed via HSBC");
        return new PaymentResult(
                "00000000000",
                PaymentStatus.SUCCESSFUL,
                "APPROVED"
        );
    }
}
