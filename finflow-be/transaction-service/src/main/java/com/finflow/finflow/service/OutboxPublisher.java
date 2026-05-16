package com.finflow.finflow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finflow.dto.TransactionCreatedEvent;
import com.finflow.finflow.entity.OutboxEvent;
import com.finflow.finflow.repository.OutboxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepo outboxRepo;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents() throws JsonProcessingException {
        List<OutboxEvent> events = outboxRepo.findByPublishedFalse();

        for (OutboxEvent event: events){
            TransactionCreatedEvent dto = objectMapper.readValue(event.getPayload(),
                    TransactionCreatedEvent.class);

            kafkaTemplate.send("transaction-topic", dto);

            event.setPublished(true);

            outboxRepo.save(event);

            System.out.println("Published outbox event: " + event.getId());
        }

    }

}
