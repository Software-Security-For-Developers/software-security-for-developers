package com.example;

import java.security.PrivateKey;
import java.security.Signature;

public final class EccSigner {

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withECDSA");
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }
}
