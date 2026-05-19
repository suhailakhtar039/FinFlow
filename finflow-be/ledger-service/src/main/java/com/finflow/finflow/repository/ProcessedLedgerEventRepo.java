package com.finflow.finflow.repository;

import com.finflow.finflow.entity.ProcessedLedgerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedLedgerEventRepo extends JpaRepository<ProcessedLedgerEvent, String> {
}
