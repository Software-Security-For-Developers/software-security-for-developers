package org.example.ssfd_ch4_ex5payments.model;

import java.math.BigDecimal;

public record Refund(String orderId, BigDecimal amount) {
}
