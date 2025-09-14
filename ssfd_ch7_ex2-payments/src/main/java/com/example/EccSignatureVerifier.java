package com.example;

import java.security.PublicKey;
import java.security.Signature;

public class EccSignatureVerifier {

    public static boolean verifyResourceSignature(byte[] data,
                                                  byte[] sigBytes,
                                                  PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withECDSA");
        verifier.initVerify(publicKey);
        verifier.update(data);
        return verifier.verify(sigBytes);
    }
}
