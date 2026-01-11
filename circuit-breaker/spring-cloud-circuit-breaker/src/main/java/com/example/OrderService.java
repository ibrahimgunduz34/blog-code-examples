package com.example;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public OrderService(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public OrderStatus getOrderStatus(String orderId) {
        return circuitBreakerFactory.create("OrderService")
                .run(
                        () -> callGetStatusApi(orderId),
                        throwable -> fallbackGetStatus(orderId, throwable)
                );
    }

    private OrderStatus callGetStatusApi(String orderId) {
        // TODO: Call the api
        return new OrderStatus();
    }

    private OrderStatus fallbackGetStatus(String orderId, Throwable throwable) {
        // TODO: Return fallback result and log the failure
        return new OrderStatus();
    }
}
