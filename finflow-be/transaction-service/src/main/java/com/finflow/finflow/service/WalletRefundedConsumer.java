package com.finflow.finflow.service;

import com.finflow.dto.TransactionStatus;
import com.finflow.dto.WalletRefundedEvent;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.metrics.TransactionMetrics;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletRefundedConsumer {
    private final TransactionRepo transactionRepo;
    private final TransactionMetrics transactionMetrics;

    @KafkaListener(topics = "wallet-refunded-topic", groupId = "transaction-group")
    public void consume(WalletRefundedEvent event){
        Transaction transaction = transactionRepo.findById(
                event.getTransactionId()
        ).orElseThrow();

        transaction.setStatus(TransactionStatus.REVERSED);
        transactionRepo.save(transaction);
        transactionMetrics.incrementCompensation();
        log.info("========Trans Reversed===========");
    }
}
