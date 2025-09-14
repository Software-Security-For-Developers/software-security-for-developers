package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Load private key (PKCS#8) from resources
            PrivateKey privateKey = KeyLoader.loadPrivateKeyFromPem("/private_key.pem");

            // 2) Load encrypted data from resources (refunds.json.rsa)
            byte[] encrypted = readAllBytesFromResource("/refunds.json.rsa");
            if (encrypted == null || encrypted.length == 0) {
                System.err.println("No data read from refunds.json.rsa. Ensure the file exists in resources.");
                return;
            }

            // 3) Decrypt using RSA/ECB/PKCS1Padding (chunked, per key size block)
            byte[] decrypted = RsaDecryptor.rsaDecryptChunked(encrypted, privateKey);

            // 4) Write result to project root as refunds.json
            File outFile = new File("refunds.json");
            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                fos.write(decrypted);
            }
            System.out.println("Decryption complete. Wrote: " + outFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to decrypt refunds.json.rsa: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static byte[] readAllBytesFromResource(String resourcePath) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            return is.readAllBytes();
        }
    }
}