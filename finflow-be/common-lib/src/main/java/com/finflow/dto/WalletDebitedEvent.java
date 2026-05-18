package com.finflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDebitedEvent {

    private String transactionId;

    private String senderId;

    private String receiverId;

    private BigDecimal amount;

    private LocalDateTime createdAt;
}