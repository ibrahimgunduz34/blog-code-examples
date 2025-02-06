package org.example;

public interface PaymentProcessor {
    AuthorizationResource authorize(AuthorizationCreate command);
}
