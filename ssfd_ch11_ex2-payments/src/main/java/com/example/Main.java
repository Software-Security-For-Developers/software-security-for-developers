package com.example;

import com.example.util.JwsUtil;
import com.example.util.ResourceReader;
import com.nimbusds.jose.JWSObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        try {
            // 1) Read the JWS from resources
            String jws = ResourceReader.readResourceAsString("refunds.jws");

            // 2) Verify signature
            boolean valid = JwsUtil.verifyJws(jws);
            if (!valid) {
                System.err.println("Invalid JWS signature for refunds.jws");
                System.exit(1);
                return;
            }

            // 3) Parse and extract payload (plain JSON text)
            String payloadJson = JWSObject.parse(jws).getPayload().toString();

            // 4) Write payload to refunds.json in project root (current working directory)
            Path outFile = Path.of("refunds.json").toAbsolutePath();
            Files.writeString(outFile, payloadJson, StandardCharsets.UTF_8);

            System.out.println("refunds.json written to: " + outFile);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to process refunds.jws: " + e.getMessage());
            System.exit(2);
        }
    }
}