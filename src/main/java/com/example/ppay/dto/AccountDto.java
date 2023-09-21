package com.example.ppay.dto;

import java.math.BigDecimal;

public record AccountDto(String id, Integer number, BigDecimal balance, UserDto user) {
}
