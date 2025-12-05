package org.example;

import org.springframework.stereotype.Component;

@Component
public class PaymentGateway {
    private final PaymentProviderConfig config;

    public PaymentGateway(PaymentProviderConfig config) {
        this.config = config;
    }

    public PaymentResult processPayment(double amount) {
        // ... logic to process payment with the external provider
        return new PaymentResult(
                config.accountId(),
                config.merchantId(),
                amount,
                "APPROVED"
        );
    }
}
