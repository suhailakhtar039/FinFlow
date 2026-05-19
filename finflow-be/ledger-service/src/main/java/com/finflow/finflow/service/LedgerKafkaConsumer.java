package com.finflow.finflow.service;

import com.finflow.dto.LedgerCreatedEvent;
import com.finflow.dto.LedgerFailedEvent;
import com.finflow.dto.WalletDebitedEvent;
import com.finflow.finflow.repository.LedgerRepo;
import com.finflow.finflow.repository.ProcessedLedgerEventRepo;
import com.finflow.finflow.repository.ProcessedLedgerFailureRepo;
import com.finflow.finflow.entity.LedgerEntry;
import com.finflow.finflow.entity.ProcessedLedgerEvent;
import com.finflow.finflow.entity.ProcessedLedgerFailure;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LedgerKafkaConsumer {

    private final LedgerRepo ledgerRepo;

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    private final ProcessedLedgerEventRepo
            processedLedgerEventRepo;

    private final ProcessedLedgerFailureRepo
            processedLedgerFailureRepo;

    @KafkaListener(
            topics = "wallet-debited-topic",
            groupId = "ledger-group"
    )
    @Transactional
    public void consume(
            WalletDebitedEvent transaction
    ) {

        // ==========================================
        // IDEMPOTENCY CHECK
        // ==========================================

        boolean alreadyProcessed =
                processedLedgerEventRepo.existsById(
                        transaction.getTransactionId()
                );

        if(alreadyProcessed) {

            System.out.println(
                    "Ledger already processed"
            );

            return;
        }

        try {

            // ==========================================
            // FAILURE SIMULATION
            // ==========================================

            if(transaction.getAmount()
                    .compareTo(BigDecimal.valueOf(5000)) > 0) {

                throw new RuntimeException(
                        "Ledger processing failed"
                );
            }

            // ==========================================
            // DEBIT ENTRY
            // ==========================================

            LedgerEntry debit =
                    new LedgerEntry();

            debit.setTransactionId(
                    transaction.getTransactionId()
            );

            debit.setAccountId(
                    transaction.getSenderId()
            );

            debit.setEntryType("DEBIT");

            debit.setAmount(
                    transaction.getAmount()
            );

            debit.setCreatedAt(
                    LocalDateTime.now()
            );

            // ==========================================
            // CREDIT ENTRY
            // ==========================================

            LedgerEntry credit =
                    new LedgerEntry();

            credit.setTransactionId(
                    transaction.getTransactionId()
            );

            credit.setAccountId(
                    transaction.getReceiverId()
            );

            credit.setEntryType("CREDIT");

            credit.setAmount(
                    transaction.getAmount()
            );

            credit.setCreatedAt(
                    LocalDateTime.now()
            );

            ledgerRepo.save(debit);

            ledgerRepo.save(credit);

            // ==========================================
            // MARK EVENT PROCESSED
            // ==========================================

            processedLedgerEventRepo.save(
                    new ProcessedLedgerEvent(
                            transaction.getTransactionId()
                    )
            );

            // ==========================================
            // SUCCESS EVENT
            // ==========================================

            LedgerCreatedEvent event =
                    LedgerCreatedEvent.builder()
                            .transactionId(
                                    transaction.getTransactionId()
                            )
                            .createdAt(
                                    LocalDateTime.now()
                            )
                            .build();

            kafkaTemplate.send(
                    "ledger-created-topic",
                    event
            );

            System.out.println(
                    "Ledger entries created"
            );

        } catch (Exception e) {

            // ==========================================
            // FAILURE IDEMPOTENCY
            // ==========================================

            boolean failureAlreadyProcessed =
                    processedLedgerFailureRepo
                            .existsById(
                                    transaction.getTransactionId()
                            );

            if(failureAlreadyProcessed) {

                System.out.println(
                        "Ledger failure already processed"
                );

                return;
            }

            // ==========================================
            // MARK FAILURE PROCESSED
            // ==========================================

            processedLedgerFailureRepo.save(
                    new ProcessedLedgerFailure(
                            transaction.getTransactionId()
                    )
            );

            // ==========================================
            // FAILURE EVENT
            // ==========================================

            LedgerFailedEvent failedEvent =
                    LedgerFailedEvent.builder()
                            .transactionId(
                                    transaction.getTransactionId()
                            )
                            .senderId(
                                    transaction.getSenderId()
                            )
                            .amount(
                                    transaction.getAmount()
                            )
                            .reason(
                                    e.getMessage()
                            )
                            .createdAt(
                                    LocalDateTime.now()
                            )
                            .build();

            kafkaTemplate.send(
                    "ledger-failed-topic",
                    failedEvent
            );

            System.out.println(
                    "Ledger failed event published"
            );

            // IMPORTANT:
            // DO NOT THROW AGAIN
        }
    }
}