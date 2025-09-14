package org.example;

import com.google.crypto.tink.subtle.EngineFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;

public class Main {
    public static void main(String[] args) {
        try {
            String hash = hashRefundsJsonWithSha3("refunds.json");
            System.out.println("SHA3-256(refunds.json) = " + hash);
        } catch (IOException e) {
            System.err.println("Failed to read refunds.json: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            System.err.println("Security error computing hash: " + e.getMessage());
        }
    }

    /**
     * SHA3-256 of refunds.json (from resources) using Tink.
     */
    public static String hashRefundsJsonWithSha3(String resourceName) throws IOException, GeneralSecurityException {
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(resourceName)) {
            MessageDigest md = EngineFactory.MESSAGE_DIGEST.getInstance("SHA3-256");
            byte [] fileContent = in.readAllBytes();
            byte [] digested =  md.digest(fileContent);
            return HexFormat.of().formatHex(digested);
        }
    }

}