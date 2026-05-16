package com.finflow.finflow.service;

import com.finflow.dto.TransactionCreatedEvent;
import com.finflow.finflow.entity.Wallet;
import com.finflow.finflow.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletKafkaConsumer {

    private final WalletRepo walletRepo;

    @KafkaListener(topics = "transaction-topic", groupId = "wallet-group")
    @Transactional
    public void consume(TransactionCreatedEvent event) {
        Wallet sender = walletRepo
                .findById(event.getSenderId())
                .orElseThrow();
        if (sender.getBalance().compareTo(event.getAmount()) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }
        sender.setBalance(sender.getBalance().subtract(event.getAmount()));

        walletRepo.save(sender);

        System.out.println("Wallet Debited Successfully");
    }
}
