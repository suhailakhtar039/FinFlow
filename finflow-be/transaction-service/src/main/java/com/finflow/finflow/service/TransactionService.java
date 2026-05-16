package com.finflow.finflow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finflow.dto.TransactionCreatedEvent;
import com.finflow.finflow.dto.TransactionRequest;
import com.finflow.finflow.entity.OutboxEvent;
import com.finflow.finflow.entity.Transaction;
import com.finflow.dto.TransactionStatus;
import com.finflow.finflow.repository.OutboxRepo;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;
    private final TransactionRepo transactionRepo;
    private final ObjectMapper objectMapper;
    private final OutboxRepo outboxRepo;

    @Transactional
    public Transaction createTransaction(TransactionRequest request, String key) throws JsonProcessingException {
        Optional<Transaction> existing = transactionRepo.findByIdempotencyKey(key);
        if (existing.isPresent())
            return existing.get();

        Transaction transaction = new Transaction();

        transaction.setId(UUID.randomUUID().toString());
        transaction.setSenderId(request.getSenderId());
        transaction.setReceiverId(request.getReceiverId());
        transaction.setAmount(request.getAmount());

        transaction.setStatus(TransactionStatus.INITIATED);

        transaction.setIdempotencyKey(key);

        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepo.save(transaction);

        // PUBLISH EVENT
        TransactionCreatedEvent event =
                TransactionCreatedEvent.builder()
                        .transactionId(transaction.getId())
                        .senderId(transaction.getSenderId())
                        .receiverId(transaction.getReceiverId())
                        .amount(transaction.getAmount())
                        .createdAt(LocalDateTime.now())
                        .build();

        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outbox = OutboxEvent.builder()
                .id(UUID.randomUUID().toString())
                .aggregateId(transaction.getId())
                .eventType("TransactionCreatedEvent")
                .payload(payload)
                .published(false)
                .createdAt(LocalDateTime.now())
                .build();

        outboxRepo.save(outbox);

        return transaction;

    }

//    public void send(String payload){
//        kafkaTemplate.send("transaction-topic", payload);
//    }
}
