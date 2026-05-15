package com.finflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreatedEvent {

    private String transactionId;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
