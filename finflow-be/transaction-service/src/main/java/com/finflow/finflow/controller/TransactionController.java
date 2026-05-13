package com.finflow.finflow.controller;

import com.finflow.finflow.service.TransactionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionProducer transactionProducer;
    @PostMapping
    public String create(@RequestBody String message){
        transactionProducer.send(message);
        return "Transaction event sent";
    }
}
