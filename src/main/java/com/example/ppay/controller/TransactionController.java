package com.example.ppay.controller;

import com.example.ppay.dto.TransactionDto;
import com.example.ppay.dto.mapper.TransactionDtoResponse;
import com.example.ppay.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDtoResponse> sendMoney(@RequestBody TransactionDto transaction) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.sendMoney(transaction));
    }
}
