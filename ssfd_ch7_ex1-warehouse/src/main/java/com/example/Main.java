package com.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.Security;

public class Main {
    public static void main(String[] args) {
        try {
            // Register Bouncy Castle provider explicitly
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            // Load EC public key directly from PEM in resources (use public_key.pem)
            PublicKey ecPublicKey = KeyLoader.loadEcPublicKeyFromPem("public_key.pem");

            byte[] refundsBytes = KeyLoader.loadResourceBytes("refunds.json");

            System.out.println("Read refunds.json (" + refundsBytes.length + " bytes)");

            // Encrypt using ECC (ECIES) via EccEncryptor
            byte[] encrypted = EccEncryptor.eccEncrypt(refundsBytes, ecPublicKey);
            System.out.println("Encrypted length: " + encrypted.length + " bytes");

            // Write output to project root as refunds.json.ecc
            Path outPath = Paths.get(System.getProperty("user.dir"), "refunds.json.ecc");
            Files.write(outPath, encrypted);
            System.out.println("Wrote encrypted file to: " + outPath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error during encryption: " + e.getMessage());
        }
    }
}