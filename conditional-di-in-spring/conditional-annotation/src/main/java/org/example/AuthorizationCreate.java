package org.example;

import java.math.BigDecimal;

public class AuthorizationCreate {
    private final BigDecimal amount;
    private final String token;

    public AuthorizationCreate(BigDecimal amount, String token) {
        this.amount = amount;
        this.token = token;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getToken() {
        return token;
    }
}
