package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentGatewayTest {
    @Autowired
    private PaymentGateway paymentGateway;

    @Test
    void processPaymentTest() {
        PaymentResult paymentResult = paymentGateway.processPayment(100.0);

        String expectedAccountId = "1234567";
        String expectedMerchantId = "098765";

        assertThat(paymentResult.accountId()).isEqualTo(expectedAccountId);
        assertThat(paymentResult.merchantId()).isEqualTo(expectedMerchantId);
    }
}
