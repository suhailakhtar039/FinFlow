package com.finflow.finflow.service;

import com.finflow.dto.LedgerFailedEvent;
import com.finflow.dto.TransactionStatus;
import com.finflow.dto.WalletRefundEvent;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LedgerFailedConsumer {

    private final TransactionRepo transactionRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "ledger-failed-topic", groupId = "transaction-group")
    @Transactional
    public void consume(LedgerFailedEvent event) {
        Transaction transaction = transactionRepo
                .findById(event.getTransactionId())
                .orElseThrow();

        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepo.save(transaction);

        WalletRefundEvent refundEvent = WalletRefundEvent.builder()
                .transactionId(event.getTransactionId())
                .senderId(event.getSenderId())
                .amount(event.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send("wallet-refund-topic", refundEvent);

        System.out.println("=========REFUND TRIGGERED========");
    }
}
