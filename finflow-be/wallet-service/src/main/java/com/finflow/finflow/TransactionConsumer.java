package com.finflow.finflow;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionConsumer {
    @KafkaListener(topics = "transaction-topic", groupId = "wallet-group")
    public void consume(String message){
        System.out.println("Wallet received: " + message);
    }
}
