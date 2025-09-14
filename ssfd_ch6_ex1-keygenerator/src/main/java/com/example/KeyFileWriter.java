package com.example;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.Base64;

public class KeyFileWriter {

    public void writePemFiles(KeyPair keyPair, Path privateKeyPath, Path publicKeyPath) throws Exception {
        String publicPem = toPem("PUBLIC KEY", keyPair.getPublic().getEncoded());
        String privatePem = toPem("PRIVATE KEY", keyPair.getPrivate().getEncoded());

        writeString(privateKeyPath, privatePem);
        writeString(publicKeyPath, publicPem);
    }

    private static String toPem(String type, byte[] derBytes) {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII))
                .encodeToString(derBytes);
        return "-----BEGIN " + type + "-----\n" + base64 + "\n-----END " + type + "-----";
    }

    private static void writeString(Path path, String content) throws Exception {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, content, StandardCharsets.US_ASCII);
    }
}
