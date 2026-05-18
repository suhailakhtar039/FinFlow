package com.finflow.finflow.repository;

import com.finflow.finflow.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepo extends JpaRepository<ProcessedEvent, String> {
}
