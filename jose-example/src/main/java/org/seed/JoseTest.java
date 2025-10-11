package org.seed;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JoseTest {
    @Test
    public void sign_end_verify() throws NoSuchAlgorithmException, JOSEException, ParseException {
        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);

        String plainText = "{\"uid\": \"ibrahim.gunduz\", \"exp\": 1759589987057}";

        String jwsString = sign(plainText, sharedSecret);

        JWSObject jwsObject = JWSObject.parse(jwsString);
        JWSVerifier verifier = new MACVerifier(sharedSecret);

        assertTrue(jwsObject.verify(verifier));
    }

    @Test
    public void encrypt_and_decrypt_using_rsa() throws JOSEException, ParseException {
        RSAKey rsaKey = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .issueTime(new Date())
                .generate();

        String plainText = "{“cardToken”: \"5105105105105100”, \"email\": \"john.doe@test.net\"}";

        String jweString = encrypt(plainText, rsaKey.toPublicJWK());
        String decrypted = decrypt(jweString, rsaKey);

        assertEquals(plainText, decrypted);
    }

    @Test
    public void encrypt_and_decrypt_using_shared_key() throws NoSuchAlgorithmException, JOSEException, ParseException {
        int keyBitLength = EncryptionMethod.A128GCM.cekBitLength();

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyBitLength);

        SecretKey sharedKey = keyGen.generateKey();

        String plainText = "{“cardToken”: \"5105105105105100”, \"email\": \"john.doe@test.net\"}";

        String encrypted = encrypt(plainText, sharedKey);
        String decrypted = decrypt(encrypted, sharedKey);

        assertEquals(plainText, decrypted);
    }

    private String decrypt(String jweString, SecretKey sharedKey) throws JOSEException, ParseException {
        JWEObject jweObject = JWEObject.parse(jweString);
        DirectDecrypter decrypter = new DirectDecrypter(sharedKey);
        jweObject.decrypt(decrypter);

        return jweObject.getPayload().toString();
    }

    private static String encrypt(String plainText, SecretKey sharedKey) throws JOSEException {
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM);
        Payload payload = new Payload(plainText);
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(new DirectEncrypter(sharedKey));

        return jweObject.serialize();
    }

    private String encrypt(String plainText, RSAKey rsaKey) throws JOSEException {
        RSAKey publicJWK = rsaKey.toPublicJWK();

        JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
        Payload payload = new Payload(plainText);
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(new RSAEncrypter(publicJWK));

        return jweObject.serialize();
    }

    private String decrypt(String jweString, RSAKey jwk) throws JOSEException, ParseException {
        RSADecrypter decrypter = new RSADecrypter(jwk);
        JWEObject jweObject = JWEObject.parse(jweString);
        jweObject.decrypt(decrypter);
        return jweObject.getPayload().toString();
    }

    private String sign(String plainText, byte[] sharedSecret) throws JOSEException {
        JWSSigner signer = new MACSigner(sharedSecret);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Payload payload = new Payload(plainText);
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }
}