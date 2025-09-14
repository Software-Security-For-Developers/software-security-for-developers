package org.example;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        String resourceName = "refunds.json";
        try {
            // Locate resource on classpath and read bytes using helper class
            URL resourceUrl = ResourceReader.getResourceUrl(resourceName);
            byte[] data = ResourceReader.readAllBytes(resourceUrl);

            // Compute SHA3-256 and hex-encode using dedicated hasher
            String hex = Sha3Hasher.hashHex(data);

            // Write result to the project root directory using dedicated writer
            Path outputPath = ResultWriter.writeToProjectRoot(resourceName + ".sha3", hex);

            System.out.println("[OK] SHA3-256 hash written to: " + outputPath);
            System.out.println("[HASH] " + hex);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            System.exit(2);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA3-256 algorithm not available: " + e.getMessage());
            System.exit(3);
        }
    }
}