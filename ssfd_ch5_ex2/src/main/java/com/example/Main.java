package com.example;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    
    public static void main(String[] args) {
        // Demonstrate AES-256-GCM encryption -> digest -> decrypt -> cleartext
        byte[] key = generateRandomBytes(32); // 256-bit key
        String message = "Hello, AES-GCM!";
        byte[] ivCiphertext = encryptAes256GCM(message.getBytes(StandardCharsets.UTF_8), key);

        // Print Base64 of IV+ciphertext (ciphertext includes the tag)
        String encB64 = Base64.getEncoder().encodeToString(ivCiphertext);
        System.out.println("Encrypted (Base64, IV+ciphertext+tag): " + encB64);

        // Decrypt back to cleartext
        byte[] clear = decryptAes256GCM(ivCiphertext, key);
        System.out.println("Decrypted cleartext: " + new String(clear, StandardCharsets.UTF_8));
    }

    public static byte[] encryptAes256GCM(byte[] clearText, byte[] key) {
        try {
            // 12-byte IV is recommended for GCM
            byte[] iv = generateRandomBytes(12);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128-bit auth tag
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] cipherText = cipher.doFinal(clearText);

            // Concatenate IV + ciphertext (ciphertext includes the tag at the end)
            byte[] result = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("AES-256-GCM encryption failed", e);
        }
    }

    public static byte[] decryptAes256GCM(byte[] ivAndCiphertext, byte[] key) {
        try {
            // Split IV (12 bytes) and ciphertext+tag (remaining)
            byte[] iv = new byte[12];
            byte[] cipherText = new byte[ivAndCiphertext.length - 12];
            System.arraycopy(ivAndCiphertext, 0, iv, 0, 12);
            System.arraycopy(ivAndCiphertext, 12, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("AES-256-GCM decryption failed", e);
        }
    }

    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
}