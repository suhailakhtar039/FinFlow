package com.finflow.finflow;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedLedgerFailureRepo extends JpaRepository<ProcessedLedgerFailure, String> {
}
