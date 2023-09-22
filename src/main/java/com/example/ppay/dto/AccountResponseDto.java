package com.example.ppay.dto;

import java.math.BigDecimal;

public record AccountResponseDto(Integer number, BigDecimal balance, String fullName) {
}
