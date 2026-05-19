package com.finflow.finflow.repository;

import com.finflow.finflow.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepo extends JpaRepository<LedgerEntry, Long> {
    boolean existsByTransactionIdAndEntryType(
            String transactionId,
            String entryType
    );
}
