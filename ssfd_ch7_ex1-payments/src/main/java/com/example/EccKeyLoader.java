package com.example;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class EccKeyLoader {

    /**
     * Loads a PKCS#8 EC private key from a PEM file located on the classpath (resources folder).
     * Example resourcePath: "/private_key.pem"
     */
    public static PrivateKey loadPrivateKeyFromPem(String resourcePath) throws Exception {
        URL url = EccKeyLoader.class.getResource(resourcePath);
        if (url == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);

        Path path = Paths.get(url.toURI());
        String pem = Files.readString(path, StandardCharsets.US_ASCII);
        String base64 = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] der = Base64.getDecoder().decode(base64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePrivate(spec);
    }
}
