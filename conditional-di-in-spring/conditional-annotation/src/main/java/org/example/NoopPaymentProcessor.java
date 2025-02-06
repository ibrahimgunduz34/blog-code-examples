package org.example;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Conditional(PaymentProcessorFallbackCondition.class)
public class NoopPaymentProcessor implements PaymentProcessor {
    @Override
    public AuthorizationResource authorize(AuthorizationCreate command) {
        return new AuthorizationResource(command.getAmount(), PaymentStatus.SUCCEED, LocalDateTime.now());
    }
}
