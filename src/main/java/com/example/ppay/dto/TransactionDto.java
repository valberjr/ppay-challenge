package com.example.ppay.dto;

import java.math.BigDecimal;

public record TransactionDto(String senderId, String receiverId, BigDecimal amount) {
}
