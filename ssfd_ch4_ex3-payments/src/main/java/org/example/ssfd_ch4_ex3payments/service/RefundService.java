package org.example.ssfd_ch4_ex3payments.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.ssfd_ch4_ex3payments.exception.InvalidHashException;
import org.example.ssfd_ch4_ex3payments.manager.HashManager;
import org.example.ssfd_ch4_ex3payments.model.Refund;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class RefundService {

    private final ObjectMapper objectMapper;
    private final HashManager hashManager;

    public List<Refund> verifyAndReturnRefunds(byte[] bodyBytes, String providedHashHex) {
        if (providedHashHex == null || providedHashHex.isBlank()) {
            throw new InvalidHashException("Missing X-Content-SHA3 header");
        }
        String computed = hashManager.computeSha3Hex(bodyBytes);
        if (!computed.equalsIgnoreCase(providedHashHex.trim())) {
            throw new InvalidHashException("Invalid SHA3 hash for provided refunds file");
        }
        try {
            return objectMapper.readValue(bodyBytes, new TypeReference<List<Refund>>(){});
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid refunds JSON format", e);
        }
    }
}
