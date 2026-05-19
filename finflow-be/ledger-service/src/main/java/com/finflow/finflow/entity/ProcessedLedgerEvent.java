package com.finflow.finflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_ledger_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedLedgerEvent {

    @Id
    private String transactionId;
}