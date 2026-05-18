package com.finflow.finflow.service;

import com.finflow.dto.TransactionStatus;
import com.finflow.dto.WalletRefundedEvent;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletRefundedConsumer {
    private final TransactionRepo transactionRepo;

    @KafkaListener(topics = "wallet-refunded-topic", groupId = "transaction-group")
    public void consume(WalletRefundedEvent event){
        Transaction transaction = transactionRepo.findById(
                event.getTransactionId()
        ).orElseThrow();

        transaction.setStatus(TransactionStatus.REVERSED);
        transactionRepo.save(transaction);

        System.out.println("============== TRANS REVERSED===========");
    }
}
