package com.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.security.Security;

public final class EccEncryptor {
    private EccEncryptor() {}

    public static byte[] eccEncrypt(byte[] data, PublicKey recipientPublic) throws Exception {
        // Ensure Bouncy Castle provider is available
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        // Use ECIES implementation from Bouncy Castle
        Cipher ecies = Cipher.getInstance("ECIES", "BC");
        ecies.init(Cipher.ENCRYPT_MODE, recipientPublic);
        return ecies.doFinal(data);
    }
}
