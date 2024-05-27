package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.security")
public class SecurityConfigProperties {
    private String secretKey;
    private Long jwtTtl;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getJwtTtl() {
        return jwtTtl;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setJwtTtl(Long jwtTtl) {
        this.jwtTtl = jwtTtl;
    }
}
