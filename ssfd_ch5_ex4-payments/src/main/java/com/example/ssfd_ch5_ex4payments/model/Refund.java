package com.example.ssfd_ch5_ex4payments.model;

import java.math.BigDecimal;

public record Refund(String orderId, BigDecimal amount) {
}
