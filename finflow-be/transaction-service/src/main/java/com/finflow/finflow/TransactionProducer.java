package com.finflow.finflow;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String payload){
        kafkaTemplate.send("transaction-topic", payload);
    }
}
