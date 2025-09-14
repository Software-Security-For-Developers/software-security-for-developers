package org.example.ssfd_ch4_ex3payments.model;

import java.math.BigDecimal;

public record Refund(String orderId, BigDecimal amount) {
}
