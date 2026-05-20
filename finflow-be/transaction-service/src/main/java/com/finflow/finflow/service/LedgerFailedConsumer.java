package com.finflow.finflow.service;

import com.finflow.dto.LedgerFailedEvent;
import com.finflow.dto.TransactionStatus;
import com.finflow.dto.WalletRefundEvent;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.metrics.TransactionMetrics;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerFailedConsumer {

    private final TransactionRepo transactionRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TransactionMetrics transactionMetrics;

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
        transactionMetrics.incrementFailure();
        log.info(
                "Refund triggered for transactionId={}",
                event.getTransactionId()
        );
    }
}
