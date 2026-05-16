package com.finflow.finflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Wallet {

    @Id
    private String userId;

    private BigDecimal balance;

    @Version
    private Long version;
}
