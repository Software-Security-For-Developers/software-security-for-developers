package org.example;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Load refunds.json from classpath resources
            byte[] data = ResourceReader.readResourceFully("refunds.json");

            // 2) Use a plain secret key (hardcoded for example purposes only)
            String secret = "demo-secret-change-me";

            // 3) Compute HMAC using HmacSHA256 (extracted to utility)
            byte[] hmac = HmacUtil.hmacSha256(secret.getBytes(StandardCharsets.UTF_8), data);

            // 4) Print results in HEX and Base64 (use HexFormat for hex)
            String hex = HexFormat.of().formatHex(hmac);

            System.out.println("HMAC-SHA256 over refunds.json");
            System.out.println("HEX:    " + hex);
        } catch (Exception e) {
            System.err.println("Error computing HMAC: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}