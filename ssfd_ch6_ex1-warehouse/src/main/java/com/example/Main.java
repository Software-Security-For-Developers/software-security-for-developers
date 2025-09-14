package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;

public class Main {
    public static void main(String[] args) {
        try {
            PublicKey publicKey = KeyLoader.loadPublicKeyFromPem("public_key.pem");

            byte[] refundsBytes = KeyLoader.loadResourceBytes("refunds.json");

            System.out.println("Read refunds.json (" + refundsBytes.length + " bytes)");

            // Encrypt using RSA with PKCS#1 v1.5 padding, chunked if needed via RsaEncryptor
            byte[] encrypted = RsaEncryptor.rsaEncryptChunked(refundsBytes, publicKey);
            System.out.println("Encrypted length: " + encrypted.length + " bytes");

            // Write output to project root as refunds.json.rsa
            Path outPath = Paths.get(System.getProperty("user.dir"), "refunds.json.rsa");
            Files.write(outPath, encrypted);
            System.out.println("Wrote encrypted file to: " + outPath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error during encryption: " + e.getMessage());
        }
    }
}