package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Load encrypted data from resources (refunds.json.ecc)
            byte[] encrypted = readAllBytesFromResource("/refunds.json.ecc");
            if (encrypted == null || encrypted.length == 0) {
                System.err.println("No data read from refunds.json.ecc. Ensure the file exists in resources.");
                return;
            }

            // 1a) Load signature bytes and public key from resources
            byte[] sigBytes = EccKeyLoader.readAllBytesFromResource("/refunds.json.ecc.sig");
            if (sigBytes == null || sigBytes.length == 0) {
                System.err.println("No data read from refunds.json.ecc.sig. Ensure the file exists in resources.");
                return;
            }
            PublicKey publicKey = EccKeyLoader.loadPublicKeyFromPem("/signing_public_key.pem");

            // 2) Verify signature BEFORE any decryption
            System.out.println("Verifying signature for refunds.json.ecc...");
            boolean ok = EccSignatureVerifier.verifyResourceSignature(
                    encrypted,
                    sigBytes,
                    publicKey);
            if (!ok) {
                System.err.println("Signature verification FAILED. Aborting decryption.");
                return;
            }
            System.out.println("Signature verification PASSED. Proceeding to decryption...");

            // 3) Load EC private key (PKCS#8) from resources
            PrivateKey privateKey = EccKeyLoader.loadPrivateKeyFromPem("/private_key.pem");

            // 4) Decrypt using ECIES (single step)
            byte[] decrypted = EccDecryptor.eciesDecrypt(encrypted, privateKey);

            // 5) Write result to project root as refunds.json
            File outFile = new File("refunds.json");
            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                fos.write(decrypted);
            }
            System.out.println("ECC decryption complete. Wrote: " + outFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to process refunds.json.ecc: " + e.getMessage());
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