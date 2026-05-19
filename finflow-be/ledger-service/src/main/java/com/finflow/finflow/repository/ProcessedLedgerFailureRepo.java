package com.finflow.finflow.repository;

import com.finflow.finflow.entity.ProcessedLedgerFailure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedLedgerFailureRepo extends JpaRepository<ProcessedLedgerFailure, String> {
}
