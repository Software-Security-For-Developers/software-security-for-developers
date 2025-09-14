package org.example.ssfd_ch4_ex3payments.controller;

import lombok.AllArgsConstructor;
import org.example.ssfd_ch4_ex3payments.model.Refund;
import org.example.ssfd_ch4_ex3payments.service.RefundService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@AllArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Refund> uploadRefunds(@RequestHeader("X-Content-SHA3") String sha3,
                                      @RequestBody byte[] body) {
        return refundService.verifyAndReturnRefunds(body, sha3);
    }
}
