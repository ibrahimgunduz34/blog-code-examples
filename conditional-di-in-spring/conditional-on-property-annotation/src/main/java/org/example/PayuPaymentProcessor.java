package org.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "payment.processor.enabled", havingValue = "true")
public class PayuPaymentProcessor implements PaymentProcessor {
    @Override
    public AuthorizationResource authorize(AuthorizationCreate command) {
        return null;
    }
}
