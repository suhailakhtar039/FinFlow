package com.finflow.finflow;

import com.finflow.dto.LedgerCreatedEvent;
import com.finflow.dto.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LedgerKafkaConsumer {

    private final LedgerRepo ledgerRepo;

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    @KafkaListener(
            topics = "transaction-topic",
            groupId = "ledger-group"
    )
    @Transactional
    public void consume(
            TransactionCreatedEvent transaction
    ) {

        boolean exists =
                ledgerRepo
                        .existsByTransactionIdAndEntryType(
                                transaction.getTransactionId(),
                                "DEBIT"
                        );

        if(exists) {
            return;
        }

        LedgerEntry debit = new LedgerEntry();

        debit.setTransactionId(
                transaction.getTransactionId()
        );

        debit.setAccountId(
                transaction.getSenderId()
        );

        debit.setEntryType("DEBIT");

        debit.setAmount(transaction.getAmount());

        debit.setCreatedAt(LocalDateTime.now());

        LedgerEntry credit = new LedgerEntry();

        credit.setTransactionId(
                transaction.getTransactionId()
        );

        credit.setAccountId(
                transaction.getReceiverId()
        );

        credit.setEntryType("CREDIT");

        credit.setAmount(transaction.getAmount());

        credit.setCreatedAt(LocalDateTime.now());

        ledgerRepo.save(debit);

        ledgerRepo.save(credit);

        // STEP 6 HERE
        LedgerCreatedEvent event =
                LedgerCreatedEvent.builder()
                        .transactionId(
                                transaction.getTransactionId()
                        )
                        .createdAt(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                "ledger-created-topic",
                event
        );

        System.out.println(
                "Ledger entries created"
        );
    }
}