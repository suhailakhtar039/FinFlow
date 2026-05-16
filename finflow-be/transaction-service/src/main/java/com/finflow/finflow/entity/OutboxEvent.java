package com.finflow.finflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    private String id;

    private String aggregateId;

    private String eventType;

    @Lob
    private String payload;

    private boolean published;

    private LocalDateTime createdAt;
}
