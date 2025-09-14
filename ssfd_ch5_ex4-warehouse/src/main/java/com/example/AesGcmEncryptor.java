package com.example;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Simplest possible AES-GCM encryptor for didactic purposes.
 * Produces Base64 of IV (12 bytes) concatenated with ciphertext+tag (from GCM).
 */
public final class AesGcmEncryptor {
    private static final int GCM_TAG_BITS = 128; // 16 bytes tag
    private static final int GCM_IV_BYTES = 12;  // recommended 12-byte IV

    private AesGcmEncryptor() {}

    /**
     * Encrypts the given plaintext with AES-GCM and returns Base64 encoding of (IV || CIPHERTEXT+TAG).
     * - Key must be 16, 24, or 32 bytes for AES-128/192/256 (here we will use 16 bytes in Main).
     */
    public static String encryptToBase64(byte[] key, byte[] plaintext) throws Exception {
        // 1) Generate a random 12-byte IV
        byte[] iv = new byte[GCM_IV_BYTES];
        new SecureRandom().nextBytes(iv);

        // 2) Init AES/GCM cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_BITS, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

        // 3) Encrypt
        byte[] cipherTextWithTag = cipher.doFinal(plaintext);

        // 4) Combine IV + ciphertext+tag
        byte[] out = new byte[iv.length + cipherTextWithTag.length];
        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(cipherTextWithTag, 0, out, iv.length, cipherTextWithTag.length);

        // 5) Base64 encode for simple text output
        return Base64.getEncoder().encodeToString(out);
    }
}
