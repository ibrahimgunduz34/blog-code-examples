package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    private final Environment environment;

    public AppConfig(Environment environment) {this.environment = environment;}

    @Bean
    public PaymentProcessor paymentProcessor() {
        if (environment.getProperty("payment.processor.enabled", Boolean.class, false)) {
            return new PayuPaymentProcessor();
        } else {
            return new NoopPaymentProcessor();
        }
    }
}
