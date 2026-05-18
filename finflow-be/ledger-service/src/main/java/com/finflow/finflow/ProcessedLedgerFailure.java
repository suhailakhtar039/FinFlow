package com.finflow.finflow;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_ledger_failures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedLedgerFailure {

    @Id
    private String transactionId;
}