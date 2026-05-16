package com.finflow.dto;

public enum TransactionStatus {
    INITIATED,
    PROCESSING,
    WALLET_DEBITED,
    LEDGER_CREATED,
    COMPLETED,
    FAILED,
    REVERSED
}
