package org.example.ssfd_ch4_ex5payments.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.ssfd_ch4_ex5payments.exception.InvalidHashException;
import org.example.ssfd_ch4_ex5payments.manager.HmacManager;
import org.example.ssfd_ch4_ex5payments.model.Refund;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class RefundService {

    private final ObjectMapper objectMapper;
    private final HmacManager hmacManager;

    public List<Refund> verifyAndReturnRefunds(byte[] bodyBytes, String providedHmacHex) {
        if (providedHmacHex == null || providedHmacHex.isBlank()) {
            throw new InvalidHashException("Missing X-Content-HMAC header");
        }
        String computed = hmacManager.computeHmacHex(bodyBytes);
        if (!computed.equalsIgnoreCase(providedHmacHex.trim())) {
            throw new InvalidHashException("Invalid HMAC for provided refunds file");
        }
        try {
            return objectMapper.readValue(bodyBytes, new TypeReference<List<Refund>>(){});
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid refunds JSON format", e);
        }
    }
}
