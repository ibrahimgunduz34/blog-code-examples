package org.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class PayuPaymentProcessor implements PaymentProcessor {
    @Override
    public AuthorizationResource authorize(AuthorizationCreate command) {
        return null;
    }
}
