package com.example.ssfd_ch5_ex4payments.controller;

import com.example.ssfd_ch5_ex4payments.model.Refund;
import com.example.ssfd_ch5_ex4payments.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Refund> uploadRefunds(@RequestBody byte[] encryptedBody) {
        return refundService.decryptAndReturnRefunds(encryptedBody);
    }
}
