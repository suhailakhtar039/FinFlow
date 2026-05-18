package com.finflow.finflow.service;

import com.finflow.dto.TransactionCreatedEvent;
import com.finflow.dto.WalletDebitedEvent;
import com.finflow.finflow.entity.ProcessedEvent;
import com.finflow.finflow.entity.Wallet;
import com.finflow.finflow.repository.ProcessedEventRepo;
import com.finflow.finflow.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletKafkaConsumer {

    private final WalletRepo walletRepo;

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    private final ProcessedEventRepo processedEventRepo;

    @KafkaListener(
            topics = "transaction-topic",
            groupId = "wallet-group"
    )
    @Transactional
    public void consume(
            TransactionCreatedEvent transaction
    ) {

        boolean alreadyProcessed =
                processedEventRepo.existsById(
                        transaction.getTransactionId()
                );

        if(alreadyProcessed) {

            System.out.println(
                    "Wallet debit already processed"
            );

            return;
        }

        Wallet sender =
                walletRepo.findById(
                        transaction.getSenderId()
                ).orElseThrow();

        if(sender.getBalance().compareTo(
                transaction.getAmount()) < 0) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        sender.setBalance(
                sender.getBalance()
                        .subtract(transaction.getAmount())
        );

        walletRepo.save(sender);

        processedEventRepo.save(
                new ProcessedEvent(
                        transaction.getTransactionId()
                )
        );

        WalletDebitedEvent event =
                WalletDebitedEvent.builder()
                        .transactionId(
                                transaction.getTransactionId()
                        )
                        .senderId(
                                transaction.getSenderId()
                        )
                        .receiverId(
                                transaction.getReceiverId()
                        )
                        .amount(
                                transaction.getAmount()
                        )
                        .createdAt(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                "wallet-debited-topic",
                event
        );

        System.out.println(
                "Wallet debited successfully"
        );
    }
}