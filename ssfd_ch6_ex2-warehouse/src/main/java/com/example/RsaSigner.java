package com.example;

import java.security.PrivateKey;
import java.security.Signature;


public final class RsaSigner {
    private RsaSigner() {}

    public static byte[] signSha256Rsa(byte[] data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }
}
