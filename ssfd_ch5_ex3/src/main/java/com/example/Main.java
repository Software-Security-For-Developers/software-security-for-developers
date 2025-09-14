package com.example;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AesGcmKeyManager;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws Exception {
        // Minimal AES-GCM with Google Tink
        AeadConfig.register(); // Ensure AEAD primitives are registered

        // 1) Create a fresh in-memory AES256-GCM keyset
        KeysetHandle keysetHandle = KeysetHandle.generateNew(
                AesGcmKeyManager.aes256GcmTemplate());

        // 2) Get the AEAD primitive
        Aead aead = keysetHandle.getPrimitive(Aead.class);

        // 3) Encrypt
        byte[] plaintext = "Hello, Tink AES-GCM!".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = aead.encrypt(plaintext, null);

        // 4) Decrypt
        byte[] decrypted = aead.decrypt(ciphertext, null);

        // Print results
        System.out.println("Plaintext: " + new String(plaintext, StandardCharsets.UTF_8));
        System.out.println("Ciphertext (Base64): " + Base64.getEncoder().encodeToString(ciphertext));
        System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }
}