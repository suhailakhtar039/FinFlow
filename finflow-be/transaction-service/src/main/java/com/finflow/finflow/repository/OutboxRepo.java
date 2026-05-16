package com.finflow.finflow.repository;

import com.finflow.finflow.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepo extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findByPublishedFalse();
}
