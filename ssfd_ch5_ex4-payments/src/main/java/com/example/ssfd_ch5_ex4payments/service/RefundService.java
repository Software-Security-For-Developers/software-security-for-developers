package com.example.ssfd_ch5_ex4payments.service;

import com.example.ssfd_ch5_ex4payments.manager.AesGcmManager;
import com.example.ssfd_ch5_ex4payments.model.Refund;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final ObjectMapper objectMapper;
    private final AesGcmManager aesGcmManager;

    public List<Refund> decryptAndReturnRefunds(byte[] encryptedBodyBytes) {
        byte[] plainBytes = aesGcmManager.decrypt(encryptedBodyBytes);
        try {
            return objectMapper.readValue(plainBytes, new TypeReference<List<Refund>>(){});
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid refunds JSON format", e);
        }
    }
}
