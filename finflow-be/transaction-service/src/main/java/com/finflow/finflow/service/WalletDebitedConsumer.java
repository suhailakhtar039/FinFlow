package com.finflow.finflow.service;

import com.finflow.dto.TransactionStatus;
import com.finflow.dto.WalletDebitedEvent;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletDebitedConsumer {

    private final TransactionRepo transactionRepo;

    @KafkaListener(
            topics = "wallet-debited-topic",
            groupId = "transaction-group"
    )
    @Transactional
    public void consume(
            WalletDebitedEvent event
    ) {

        Transaction transaction =
                transactionRepo.findById(
                        event.getTransactionId()
                ).orElseThrow();

        transaction.setStatus(
                TransactionStatus.WALLET_DEBITED
        );

        transactionRepo.save(transaction);

        System.out.println(
                "Transaction updated to WALLET_DEBITED"
        );
    }
}