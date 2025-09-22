package com.example;

import com.example.util.JwsUtil;
import com.example.util.ResourceReader;
import com.example.util.OutputWriter;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Read the refunds.json payload from the classpath (separate concern)
            String refundsJson = ResourceReader.readResourceAsString("refunds.json");

            // 2) Generate a JWS containing the refunds payload using Nimbus (separate util)
            String jws = JwsUtil.generateJws(refundsJson);

            // 3) Persist the JWS to project root as refunds.jws (separate concern handled by OutputWriter)
            Path written = OutputWriter.writeStringToProjectRoot("refunds.jws", jws);
            System.out.println("Generated JWS written to: " + written.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}