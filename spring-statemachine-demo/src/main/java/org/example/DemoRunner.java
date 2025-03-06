package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DemoRunner implements CommandLineRunner {
    private final PaymentService paymentService;

    public DemoRunner(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create a payment
        PaymentEntity payment = paymentService.create(BigDecimal.valueOf(100));

        System.out.println("Current state of payment(" + payment.getId() + ") is " + payment.getPaymentState().toString());

        // Perform authorize() call to trigger the relevant state transition
        PaymentEntity paymentAfterAuth = paymentService.authorize(payment.getId());

        System.out.println("Current state of payment(" + paymentAfterAuth.getId() + ") is " + paymentAfterAuth.getPaymentState().toString() + " after authorization");

        // Retrieve the payment to see if the current state is persisted in the database.
        PaymentEntity currentPayment = paymentService.getPayment(payment.getId());

        System.out.println("Current state of the retrieved payment(" + currentPayment.getId() + ") is " + currentPayment.getPaymentState().toString());
    }
}
