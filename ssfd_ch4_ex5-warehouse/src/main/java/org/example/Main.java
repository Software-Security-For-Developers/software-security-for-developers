package org.example;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Load refunds.json from classpath resources
            byte[] data = ResourceReader.readResourceFully("refunds.json");
            if (data == null) {
                throw new IllegalStateException("Resource 'refunds.json' not found on classpath");
            }

            // 2) Use a plain secret key (hardcoded for example purposes only)
            String secret = "demo-secret-change-me";

            // 3) Compute HMAC using HmacSHA256 (extracted to utility)
            byte[] hmac = HmacUtil.hmacSha256(secret.getBytes(StandardCharsets.UTF_8), data);

            // 4) Convert to HEX (use HexFormat for hex)
            String hex = HexFormat.of().formatHex(hmac);

            // 5) Print and persist the HMAC to the project root as refunds.json.hs256
            System.out.println("HMAC-SHA256 over refunds.json");
            System.out.println("HEX:    " + hex);

            Path outPath = Path.of("").toAbsolutePath().resolve("refunds.json.hs256");
            Files.writeString(outPath, hex, StandardCharsets.UTF_8);
            System.out.println("Wrote HMAC to: " + outPath);
        } catch (Exception e) {
            System.err.println("Error computing HMAC: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}