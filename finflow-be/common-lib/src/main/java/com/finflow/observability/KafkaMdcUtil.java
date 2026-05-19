package com.finflow.observability;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public final class KafkaMdcUtil {

    private KafkaMdcUtil() {}

    public static void populate(ConsumerRecord<?, ?> record) {

        MdcUtil.put(
                LogFields.KAFKA_TOPIC,
                record.topic()
        );

        MdcUtil.put(
                LogFields.KAFKA_PARTITION,
                String.valueOf(record.partition())
        );

        MdcUtil.put(
                LogFields.KAFKA_OFFSET,
                String.valueOf(record.offset())
        );
    }
}