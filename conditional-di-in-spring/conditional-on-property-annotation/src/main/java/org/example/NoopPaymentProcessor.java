package org.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(name = "payment.processor.enabled", havingValue = "false", matchIfMissing = true)
public class NoopPaymentProcessor implements PaymentProcessor {
    @Override
    public AuthorizationResource authorize(AuthorizationCreate command) {
        return new AuthorizationResource(command.getAmount(), PaymentStatus.SUCCEED, LocalDateTime.now());
    }
}
