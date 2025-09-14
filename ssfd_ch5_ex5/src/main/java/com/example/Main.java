package com.example;

import javax.crypto.Cipher;
import javax.crypto.AEADBadTagException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Main {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    
    public static void main(String[] args) {
        // Demonstrate AES-256-GCM with AEAD (AAD) -> encrypt -> decrypt -> cleartext
        byte[] key = generateRandomBytes(32); // 256-bit key
        String message = "Hello, AES-GCM!";
        byte[] aad = "context:demo:v1".getBytes(StandardCharsets.UTF_8);

        byte[] ivCiphertext = encryptAes256GCM(message.getBytes(StandardCharsets.UTF_8), key, aad);

        // Print Base64 of IV+ciphertext (ciphertext includes the tag)
        String encB64 = Base64.getEncoder().encodeToString(ivCiphertext);
        System.out.println("Encrypted (Base64, IV+ciphertext+tag): " + encB64);
        System.out.println("AAD used (UTF-8): " + new String(aad, StandardCharsets.UTF_8));

        // Decrypt back to cleartext using the same AAD
        byte[] clear = decryptAes256GCM(ivCiphertext, key, aad);
        System.out.println("Decrypted cleartext: " + new String(clear, StandardCharsets.UTF_8));

        // Optional: show that wrong AAD causes authentication failure
        try {
            byte[] wrongAad = "context:demo:v2".getBytes(StandardCharsets.UTF_8);
            decryptAes256GCM(ivCiphertext, key, wrongAad);
            System.out.println("[WARN] Decryption unexpectedly succeeded with wrong AAD (this should not happen)");
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof AEADBadTagException) {
                System.out.println("Decryption failed as expected with wrong AAD: " + cause);
            } else {
                System.out.println("Decryption failed (possibly due to wrong AAD): " + ex.getMessage());
            }
        }
    }


    public static byte[] encryptAes256GCM(byte[] clearText, byte[] key, byte[] aad) {
        try {
            // 12-byte IV is recommended for GCM
            byte[] iv = generateRandomBytes(12);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128-bit auth tag
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            if (aad != null && aad.length > 0) {
                cipher.updateAAD(aad);
            }

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

    public static byte[] decryptAes256GCM(byte[] ivAndCiphertext, byte[] key, byte[] aad) {
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

            if (aad != null && aad.length > 0) {
                cipher.updateAAD(aad);
            }

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