package com.example;

import com.example.util.JwsUtil;
import com.example.util.ResourceReader;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Read the refunds.json payload from the classpath (separate concern)
            String refundsJson = ResourceReader.readResourceAsString("refunds.json");

            // 2) Generate a JWS containing the refunds payload using Nimbus (separate util)
            String jws = JwsUtil.generateJws(refundsJson);

            // 3) Output the compact JWS and verify to prove functionality
            System.out.println("Generated JWS:\n" + jws);
            boolean valid = JwsUtil.verifyJws(jws);
            System.out.println("Signature valid: " + valid);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}