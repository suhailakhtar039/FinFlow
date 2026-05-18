package com.finflow.finflow.service;

import com.finflow.dto.WalletRefundEvent;
import com.finflow.dto.WalletRefundedEvent;
import com.finflow.finflow.entity.Wallet;
import com.finflow.finflow.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletRefundConsumer {
    private final WalletRepo walletRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "wallet-refund-topic", groupId = "wallet-group")
    @Transactional
    public void consume(WalletRefundEvent event){
        Wallet wallet = walletRepo.findById(event.getSenderId()).orElseThrow();
        wallet.setBalance(wallet.getBalance().add(event.getAmount()));
        walletRepo.save(wallet);

        WalletRefundedEvent refundedEvent = WalletRefundedEvent.builder()
                .transactionId(event.getTransactionId())
                .createdAt(event.getCreatedAt())
                .build();

        kafkaTemplate.send("wallet-refunded-topic", refundedEvent);

        System.out.println("==========Wallet Refund=============");
    }
}
