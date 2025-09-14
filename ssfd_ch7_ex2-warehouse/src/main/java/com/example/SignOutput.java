package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;

public final class SignOutput {
    private SignOutput() {}

    public static Path signAndWriteToAppRoot(byte[] data, String fileName) throws Exception {
        PrivateKey privateKey = KeyLoader.loadEcPrivateKeyFromPem("signing_private_key.pem");
        byte[] signature = EccSigner.sign(data, privateKey);
        Path out = Paths.get(System.getProperty("user.dir"), fileName);
        Files.write(out, signature);
        return out;
    }
}
