package org.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("default")
public class NoopPaymentProcessor implements PaymentProcessor {
    @Override
    public AuthorizationResource authorize(AuthorizationCreate command) {
        return new AuthorizationResource(command.getAmount(), PaymentStatus.SUCCEED, LocalDateTime.now());
    }
}
