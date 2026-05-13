package com.finflow.finflow;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerKafkaConsumer {
    @Autowired
    private final LedgerRepo ledgerRepo;

    @KafkaListener(topics = "transaction-topic", groupId = "ledger-group")
    public void consume(String message) {
        System.out.println("Ledger processing: " + message);

        // Example transactionId
        String transactionId = UUID.randomUUID().toString();

        //DEBIT ENTRY
        LedgerEntry debit = new LedgerEntry();
        debit.setTransactionId(transactionId);
        debit.setAccountId("USER_A");
        debit.setEntryType("DEBIT");
        debit.setAmount(BigDecimal.valueOf(100));
        debit.setCreatedAt(LocalDateTime.now());

        // CREDIT ENTRY
        LedgerEntry credit = new LedgerEntry();
        credit.setTransactionId(transactionId);
        credit.setAccountId("USER_B");
        credit.setEntryType("CREDIT");
        credit.setAmount(BigDecimal.valueOf(100));
        credit.setCreatedAt(LocalDateTime.now());

        ledgerRepo.save(debit);
        ledgerRepo.save(credit);

        System.out.println("Ledger entries created");

    }
}
