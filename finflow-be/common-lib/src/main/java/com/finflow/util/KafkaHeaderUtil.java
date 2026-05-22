package com.finflow.util;

import com.finflow.dto.constants.Headers;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;

public class KafkaHeaderUtil {
    private static final String MDC_CORRELATION_ID = "correlationId";

    private KafkaHeaderUtil() {
    }

    public static void addCorrelationIdHeader(ProducerRecord<String, Object> record) {
        String correlationId = MDC.get(MDC_CORRELATION_ID);
        if (correlationId != null) {
            record.headers().add(
                    new RecordHeader(Headers.CORRELATION_ID,
                            correlationId.getBytes(StandardCharsets.UTF_8))
            );
        }
    }

    public static void setCorrelationIdFromHeaders(ConsumerRecord<?, ?> record) {
        Header header = record
                .headers()
                .lastHeader(Headers.CORRELATION_ID);

        if (header != null) {
            String correlationId = new String(header.value(),
                    StandardCharsets.UTF_8);

            MDC.put(MDC_CORRELATION_ID, correlationId);
        }
    }

    public static void clear() {
        MDC.clear();
    }
}
