package com.example.ssfd_ch5_ex4payments.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;


@Component
public class AesGcmManager {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128; // 16 bytes tag
    private static final int IV_LENGTH_BYTES = 12;      // 12 bytes nonce

    @Value("${security.aes.secret:}")
    private String plaintextSecret;


    public byte[] decrypt(byte[] input) {
        byte[] cipherMessage = tryBase64Decode(input);
        if (cipherMessage.length < IV_LENGTH_BYTES + 1 + (GCM_TAG_LENGTH_BITS / 8)) {
            throw new IllegalArgumentException("Encrypted payload too short for AES-GCM format");
        }
        byte[] iv = new byte[IV_LENGTH_BYTES];
        System.arraycopy(cipherMessage, 0, iv, 0, IV_LENGTH_BYTES);
        byte[] cipherAndTag = new byte[cipherMessage.length - IV_LENGTH_BYTES];
        System.arraycopy(cipherMessage, IV_LENGTH_BYTES, cipherAndTag, 0, cipherAndTag.length);

        byte[] key = loadKey();
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            return cipher.doFinal(cipherAndTag);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decrypt AES-GCM payload", e);
        }
    }

    private byte[] loadKey() {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest(plaintextSecret.getBytes(StandardCharsets.UTF_8)); // 32 bytes
        } catch (Exception e) {
            throw new IllegalStateException("Failed to derive AES key from 'security.aes.secret'", e);
        }
    }

    private static byte[] tryBase64Decode(byte[] input) {
        try {
            String s = new String(input, StandardCharsets.US_ASCII).trim();
            if (s.isEmpty()) return new byte[0];
            return Base64.getDecoder().decode(s);
        } catch (IllegalArgumentException ignored) {
            return input;
        }
    }
}
