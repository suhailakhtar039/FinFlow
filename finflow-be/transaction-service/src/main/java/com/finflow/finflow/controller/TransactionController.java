package com.finflow.finflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finflow.finflow.dto.TransactionRequest;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody TransactionRequest request,
            @RequestHeader("Idempotency-Key") String key
    ) throws JsonProcessingException {
        return ResponseEntity.ok(
                transactionService.createTransaction(request, key)
        );
    }
}
