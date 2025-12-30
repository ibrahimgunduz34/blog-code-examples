package com.example;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryKMS {
    private static final String KEY_ALGORITHM = "AES";
    private static final int AES_KEY_LENGTH = 256;
    private static final String WRAP_TRANSFORMATION = "AESWrap";
    private final Map<String, List<SecretKey>> keyStore = new HashMap<>();

    public String createKEK() {
        SecretKey key = generateKey();
        String keyId = UUID.randomUUID().toString();
        keyStore.computeIfAbsent(keyId, k -> new ArrayList<>()).add(key);
        return keyId;
    }

    public void rotateKEK(String keyId) {
        List<SecretKey> versions = keyStore.get(keyId);
        if (versions == null) {
            throw new IllegalArgumentException("Unknown kekId: " + keyId);
        }

        SecretKey newKek = generateKey();
        versions.add(newKek);
    }

    public byte[] wrapDEK(String keyId, SecretKey dataEncryptionKey) {
        try {
            SecretKey activeKek = getLatestKey(keyId);
            Cipher cipher = Cipher.getInstance(WRAP_TRANSFORMATION);
            cipher.init(Cipher.WRAP_MODE, activeKek);

            return cipher.wrap(dataEncryptionKey);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey unwrapDEK(String keyId, byte[] wrappedDataEncryptionKey) {
        try {
            List<SecretKey> versions = keyStore.get(keyId);
            if (versions == null) {
                throw new IllegalArgumentException("Unknown kekId: " + keyId);
            }

            // Try each KEK version (newest first), just like cloud KMS
            for (int i = versions.size() - 1; i >= 0; i--) {
                SecretKey kek = versions.get(i);

                try {
                    Cipher cipher = Cipher.getInstance(WRAP_TRANSFORMATION);
                    cipher.init(Cipher.UNWRAP_MODE, kek);

                    return (SecretKey) cipher.unwrap(wrappedDataEncryptionKey, KEY_ALGORITHM, Cipher.SECRET_KEY);

                } catch (GeneralSecurityException ignored) {
                    // Try next version
                }
            }

            throw new IllegalStateException("Unable to unwrap DEK with any KEK version");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to unwrap DEK", e);
        }
    }

    private SecretKey generateKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
            generator.init(AES_KEY_LENGTH);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey getLatestKey(String keyId) {
        List<SecretKey> versions = keyStore.get(keyId);
        if (versions == null || versions.isEmpty()) {
            throw new IllegalArgumentException("Unknown kekId: " + keyId);
        }
        return versions.getLast();
    }
}
