package com.finflow.finflow;

import com.finflow.dto.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerKafkaConsumer {

    private final LedgerRepo ledgerRepo;

    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(
                    delay = 2000,
                    multiplier = 2.0
            ),
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(
            topics = "transaction-topic",
            groupId = "ledger-group"
    )
    @Transactional
    public void consume(TransactionCreatedEvent event) {

        System.out.println("Processing transaction: "
                + event.getTransactionId());
        System.out.println("EVENT in CONSUMER: " + event);
        // TEMPORARY FAILURE SIMULATION
        if(event.getAmount().compareTo(
                BigDecimal.valueOf(500)) > 0) {

            throw new RuntimeException(
                    "Simulated processing failure");
        }

        boolean exists =
                ledgerRepo.existsByTransactionIdAndEntryType(
                        event.getTransactionId(),
                        "DEBIT"
                );

        if(exists) {
            return;
        }

        LedgerEntry debit = new LedgerEntry();

        debit.setTransactionId(event.getTransactionId());
        debit.setAccountId(event.getSenderId());
        debit.setEntryType("DEBIT");
        debit.setAmount(event.getAmount());
        debit.setCreatedAt(LocalDateTime.now());

        LedgerEntry credit = new LedgerEntry();

        credit.setTransactionId(event.getTransactionId());
        credit.setAccountId(event.getReceiverId());
        credit.setEntryType("CREDIT");
        credit.setAmount(event.getAmount());
        credit.setCreatedAt(LocalDateTime.now());

        ledgerRepo.save(debit);
        ledgerRepo.save(credit);

        System.out.println("Ledger entries created");
    }

    @DltHandler
    public void dlt(TransactionCreatedEvent event){
        System.out.println("Message Sent To DLT: " + event.getTransactionId());
    }

}