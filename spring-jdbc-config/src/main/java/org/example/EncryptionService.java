package org.example;

import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class EncryptionService {
    private static final String CIPHER_ALG = "AES/GCM/NoPadding";
    private static final String KEY_ALG = "AES";
    private static final int GCM_TAG_LENGTH_IN_BYTE = 128;
    private static final int IV_LENGTH_IN_BYTES = 12;

    public String encrypt(String value, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] iv = new byte[IV_LENGTH_IN_BYTES];
        Cipher cipher = Cipher.getInstance(CIPHER_ALG);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_IN_BYTE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        byte[] combinedIvAndCipherText = new byte[iv.length + encryptedBytes.length];

        System.arraycopy(iv, 0, combinedIvAndCipherText, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedIvAndCipherText, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combinedIvAndCipherText);
    }

    public String decrypt(String value, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decodedCipherText = Base64.getDecoder().decode(value);

        byte[] iv = new byte[IV_LENGTH_IN_BYTES];
        byte[] encryptedBytes = new byte[decodedCipherText.length - IV_LENGTH_IN_BYTES];

        System.arraycopy(decodedCipherText, 0, iv, 0, iv.length);
        System.arraycopy(decodedCipherText, IV_LENGTH_IN_BYTES, encryptedBytes, 0, encryptedBytes.length);

        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_IN_BYTE, iv);
        Cipher cipher = Cipher.getInstance(CIPHER_ALG);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public SecretKey createSecretKeyFromPassphrase(String passphrase) {
        byte[] passphraseBytes = Base64.getDecoder().decode(passphrase);
        return new SecretKeySpec(passphraseBytes, KEY_ALG);
    }
}
