package com.finflow.finflow.service;

import com.finflow.dto.LedgerCreatedEvent;
import com.finflow.dto.TransactionStatus;
import com.finflow.finflow.entity.Transaction;
import com.finflow.finflow.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LedgerCreatedConsumer {

    private final TransactionRepo transactionRepo;

    @KafkaListener(
            topics = "ledger-created-topic",
            groupId = "transaction-group"
    )
    @Transactional
    public void consume(
            LedgerCreatedEvent event
    ) {

        Transaction transaction =
                transactionRepo.findById(
                        event.getTransactionId()
                ).orElseThrow();

        transaction.setStatus(
                TransactionStatus.COMPLETED
        );

        transactionRepo.save(transaction);

        System.out.println(
                "Transaction COMPLETED"
        );
    }
}
