package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class KeyLoader {
    private KeyLoader() {}

    public static PublicKey loadPublicKeyFromPem(String resourceName) throws Exception {
        byte[] pemBytes = loadResourceBytes(resourceName);

        String pem = new String(pemBytes, StandardCharsets.US_ASCII);
        pem = pem.replace("\r", "");
        StringBuilder sb = new StringBuilder();
        for (String line : pem.split("\n")) {
            if (line.startsWith("-----")) continue;
            if (line.isBlank()) continue;
            sb.append(line.trim());
        }
        byte[] der = Base64.getDecoder().decode(sb.toString());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    public static byte[] loadResourceBytes(String resourceName) throws IOException {
        ClassLoader cl = KeyLoader.class.getClassLoader();
        try (InputStream is = cl.getResourceAsStream(resourceName)) {
            return is.readAllBytes();
        }
    }
}
