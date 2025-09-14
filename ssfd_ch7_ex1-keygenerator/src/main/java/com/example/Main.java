package com.example;

import java.nio.file.Path;
import java.security.KeyPair;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) {
        try {
            String curveName = "secp256r1"; // NIST P-256
            // Use a strong SecureRandom
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();

            // Generate ECC key pair using dedicated class
            EcKeyPairGenerator generator = new EcKeyPairGenerator(curveName, secureRandom);
            KeyPair keyPair = generator.generate();

            Path privPath = Path.of("signing_private_key.pem");
            Path pubPath= Path.of("signing_public_key.pem");

            // Write keys to files using dedicated class
            KeyFileWriter writer = new KeyFileWriter();
            writer.writePemFiles(keyPair, privPath, pubPath);

            System.out.println("Generated ECC key pair on curve " + curveName + ".");
            System.out.println("Keys written to:\n  Private: " + privPath.toAbsolutePath() + "\n  Public:  " + pubPath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error generating ECC key pair: " + e.getMessage());
        }
    }
}