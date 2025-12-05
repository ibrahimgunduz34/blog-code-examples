package org.example;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "payment.provider")
public record PaymentProviderConfig(
        String accountId,
        String merchantId
) {
}
