package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Main {
    public static void main(String[] args) {
        String resourceName = "refunds.json";
        try {
            String hex = sha3HexOfResource(resourceName);
            System.out.println("SHA3-256(refunds.json) = " + hex);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: SHA3-256 algorithm not available: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("I/O error while reading resource: " + e.getMessage());
            System.exit(3);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    static String sha3HexOfResource(String resourceName) throws IOException, NoSuchAlgorithmException {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IllegalArgumentException("Error: Resource '" + resourceName + "' not found on classpath.");
            }
            byte[] data = is.readAllBytes();
            byte[] digest = MessageDigest.getInstance("SHA3-256").digest(data);
            return HexFormat.of().formatHex(digest);
        }
    }
}