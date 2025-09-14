package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        try {
            PublicKey publicKey = KeyLoader.loadPublicKeyFromPem("public_key.pem");

            byte[] refundsBytes = ResourceLoader.loadResourceBytes("refunds.json");

            System.out.println("Read refunds.json (" + refundsBytes.length + " bytes)");

            // Encrypt using RSA with PKCS#1 v1.5 padding, chunked if needed via RsaEncryptor
            byte[] encrypted = RsaEncryptor.rsaEncryptChunked(refundsBytes, publicKey);
            System.out.println("Encrypted length: " + encrypted.length + " bytes");

            // Write encrypted output to project root as refunds.json.rsa
            Path encOutPath = Paths.get(System.getProperty("user.dir"), "refunds.json.rsa");
            Files.write(encOutPath, encrypted);
            System.out.println("Wrote encrypted file to: " + encOutPath.toAbsolutePath());

            // Load signing private key and sign the encrypted bytes (RSA SHA-256)
            PrivateKey signingKey = KeyLoader.loadPrivateKeyFromPem("signing_private_key.pem");
            byte[] signature = RsaSigner.signSha256Rsa(encrypted, signingKey);
            System.out.println("Signature length: " + signature.length + " bytes");

            // Write signature as Base64 to project root as refunds.json.rsa.sig (text)
            Path sigOutPath = Paths.get(System.getProperty("user.dir"), "refunds.json.rsa.sig");
            String b64Sig = Base64.getEncoder().encodeToString(signature);
            Files.writeString(sigOutPath, b64Sig);
            System.out.println("Wrote signature file to: " + sigOutPath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error during encryption/signing: " + e.getMessage());
        }
    }
}