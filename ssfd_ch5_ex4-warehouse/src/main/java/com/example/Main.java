package com.example;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Load refunds.json from classpath resources
            byte[] plaintext = ResourceReader.readResourceFully("refunds.json");

            // 2) Use a simple 16-byte AES key (hardcoded for didactic purposes only)
            String aesKeyString = "0123456789abcdef"; // 16 chars -> 16 bytes
            byte[] aesKey = aesKeyString.getBytes(StandardCharsets.UTF_8);

            // 3) Encrypt with AES-GCM and get Base64 of IV||CIPHERTEXT+TAG
            String base64Cipher = AesGcmEncryptor.encryptToBase64(aesKey, plaintext);

            // 4) Print and persist the ciphertext to the project root as refunds.json.aesgcm
            System.out.println("AES-GCM encryption of refunds.json");
            System.out.println("Base64(IV||CIPHERTEXT+TAG): " + base64Cipher);

            Path outPath = Path.of("").toAbsolutePath().resolve("refunds.json.aesgcm");
            Files.writeString(outPath, base64Cipher, StandardCharsets.UTF_8);
            System.out.println("Wrote encrypted data to: " + outPath);
        } catch (Exception e) {
            System.err.println("Error encrypting with AES-GCM: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}