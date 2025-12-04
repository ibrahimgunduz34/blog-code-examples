package org.example;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class PropertyDecryptor {
    private final ConfigurableEnvironment environment;
    private final EncryptionService encryptionService;
    private static final String ENCRYPTED_VALUE_PREFIX = "$encrypted$";
    private static final String JDBC_PASSPHRASE_KEY = "jdbc.passphrase";

    public PropertyDecryptor(ConfigurableEnvironment environment, EncryptionService encryptionService) {
        this.environment = environment;
        this.encryptionService = encryptionService;
    }

//    @PostConstruct
    public void decrypt() {
        SecretKey secretKey = encryptionService.createSecretKeyFromPassphrase(environment.getProperty(JDBC_PASSPHRASE_KEY));
        environment.getPropertySources().forEach(ps -> {
            if (ps instanceof JdbcPropertySource jdbcPropertySource) {
                for (String key : jdbcPropertySource.getPropertyNames()) {
                    Object value = jdbcPropertySource.getProperty(key);
                    if (value instanceof String sValue && sValue.startsWith(ENCRYPTED_VALUE_PREFIX)) {
                        try {
                            String cipherText = sValue.substring(ENCRYPTED_VALUE_PREFIX.length());
                            String decrypt = encryptionService.decrypt(cipherText, secretKey);
                            jdbcPropertySource.getProperty(key);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }
}
