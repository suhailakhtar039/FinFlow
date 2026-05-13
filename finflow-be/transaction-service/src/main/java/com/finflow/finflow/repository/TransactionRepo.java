package com.finflow.finflow.repository;

import com.finflow.finflow.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByIdempotencyKey(String key);
}
