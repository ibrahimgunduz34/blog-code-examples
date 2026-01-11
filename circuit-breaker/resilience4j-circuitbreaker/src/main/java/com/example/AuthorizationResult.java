package com.example;

public record AuthorizationResult(
        String transactionId,
        String message
) {
}
