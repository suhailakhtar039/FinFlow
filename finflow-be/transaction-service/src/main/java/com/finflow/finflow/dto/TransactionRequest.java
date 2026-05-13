package com.finflow.finflow.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {

    private String senderId;
    private String receiverId;
    private BigDecimal amount;

}
