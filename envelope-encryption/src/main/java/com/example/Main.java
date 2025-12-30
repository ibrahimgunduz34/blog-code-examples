package com.example;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Main {
    private static final String KEY_ALGORITHM = "AES";
    private static final String ENC_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_LENGTH = 256;
    private static final int GCM_TAG_LENGTH = 128;

    record EncryptionResult(
            String iv,
            String cipherText,
            String kekId,
            String wrappedDek
    ) {}

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        InMemoryKMS kms = new InMemoryKMS();

        String plainText = "5105105105105100";

        EncryptionResult encryptionResult = encrypt(plainText, kms);

        System.out.println("Base64 Encoded IV: " + encryptionResult.iv);
        System.out.println("Base64 Encoded Cipher: " + encryptionResult.cipherText);
        System.out.println("KEK ID: " + encryptionResult.kekId);
        System.out.println("Base64 Encoded WrappedDEK: " + encryptionResult.wrappedDek);

        String decryptedText = decrypt(encryptionResult, kms);

        System.out.println("Decrypted Text: " + decryptedText);
    }

    private static String decrypt(EncryptionResult encryptionResult, InMemoryKMS kms) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] wrappedDek = Base64.getDecoder().decode(encryptionResult.wrappedDek);
        byte[] iv = Base64.getDecoder().decode(encryptionResult.iv);
        byte[] cipherText = Base64.getDecoder().decode(encryptionResult.cipherText);

        SecretKey dataEncryptionKey = kms.unwrapDEK(encryptionResult.kekId, wrappedDek);

        Cipher cipher = Cipher.getInstance(ENC_ALGORITHM);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, dataEncryptionKey, gcmParameterSpec);
        byte[] decrypted = cipher.doFinal(cipherText);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static EncryptionResult encrypt(String plainText, InMemoryKMS kms) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Generate DEK
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(AES_KEY_LENGTH);
        SecretKey dataEncryptionKey = keyGenerator.generateKey();

        // Encrypt the data in the plainText variable
        byte[] bPlainText = plainText.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance(ENC_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, dataEncryptionKey);

        byte[] cipherText = cipher.doFinal(bPlainText);
        byte[] iv = cipher.getIV();


        String encodedIv = Base64.getEncoder().encodeToString(iv);
        String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);

        // Wrap the Data Encryption Key
        String kekId = kms.createKEK();

        byte[] wrappedDEK = kms.wrapDEK(kekId, dataEncryptionKey);
        String encodedWrappedDEK = Base64.getEncoder().encodeToString(wrappedDEK);

        return new EncryptionResult(encodedIv, encodedCipherText, kekId, encodedWrappedDEK);
    }


}