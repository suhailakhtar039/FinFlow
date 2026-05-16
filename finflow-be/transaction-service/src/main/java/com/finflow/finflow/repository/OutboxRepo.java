package com.finflow.finflow.repository;

import com.finflow.finflow.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepo extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findByPublishedFalse();
}
