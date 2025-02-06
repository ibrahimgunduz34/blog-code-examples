package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuthorizationResource {
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final LocalDateTime createdAt;

    public AuthorizationResource(BigDecimal amount, PaymentStatus status, LocalDateTime createdAt) {
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
