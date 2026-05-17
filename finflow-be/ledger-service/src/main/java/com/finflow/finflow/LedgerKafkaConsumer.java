package com.finflow.finflow;

import com.finflow.dto.LedgerCreatedEvent;
import com.finflow.dto.LedgerFailedEvent;
import com.finflow.dto.TransactionCreatedEvent;
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

    @KafkaListener(
            topics = "transaction-topic",
            groupId = "ledger-group"
    )
    @Transactional
    public void consume(
            TransactionCreatedEvent transaction
    ) {

        try {

            // simulate failure
            if(transaction.getAmount()
                    .compareTo(BigDecimal.valueOf(5000)) > 0) {

                throw new RuntimeException(
                        "Ledger processing failed"
                );
            }

            // existing ledger logic

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

        } catch (Exception e) {

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
                            .reason(e.getMessage())
                            .createdAt(LocalDateTime.now())
                            .build();

            kafkaTemplate.send(
                    "ledger-failed-topic",
                    failedEvent
            );

            throw e;
        }
    }
}