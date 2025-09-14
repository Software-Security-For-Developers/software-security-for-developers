package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Load keys from resources
            PrivateKey privateKey = KeyLoader.loadPrivateKeyFromPem("/private_key.pem");
            PublicKey signingPublicKey = KeyLoader.loadPublicKeyFromPem("/signing_public_key.pem");

            // 2) Load encrypted data from resources (refunds.json.rsa)
            byte[] encryptedData = ResourceLoader.readAllBytesFromResource("/refunds.json.rsa");

            // 3) Verify RSA signature of the encrypted file BEFORE decryption
            byte[] externalSig = ResourceLoader.readAllBytesFromResource("/refunds.json.rsa.sig");
            byte[] signatureBytes;

            String sigText = new String(externalSig, StandardCharsets.US_ASCII).replaceAll("\\s", "");
            signatureBytes = Base64.getDecoder().decode(sigText);

            boolean verified = SignatureVerifier.verify(encryptedData, signatureBytes, signingPublicKey);
            if (!verified) {
                throw new SecurityException("Signature verification failed for refunds.json.rsa using refunds.json.rsa.sig.");
            }

            // 4) Decrypt using RSA/ECB/PKCS1Padding (chunked, per key size block)
            byte[] decrypted = RsaDecryptor.rsaDecryptChunked(encryptedData, privateKey);

            // 5) Write result to project root as refunds.json
            File outFile = new File("refunds.json");
            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                fos.write(decrypted);
            }
            System.out.println("Signature verified and decryption complete. Wrote: " + outFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to verify signature and decrypt refunds.json.rsa: " + e.getMessage());
        }
    }

}