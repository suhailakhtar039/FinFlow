package com.finflow.observability;

public final class LogFields {

    private LogFields() {}

    public static final String CORRELATION_ID = "correlationId";
    public static final String TRANSACTION_ID = "transactionId";
    public static final String WALLET_ID = "walletId";
    public static final String USER_ID = "userId";
    public static final String EVENT_TYPE = "eventType";

    public static final String KAFKA_TOPIC = "kafkaTopic";
    public static final String KAFKA_PARTITION = "partition";
    public static final String KAFKA_OFFSET = "offset";

    public static final String RETRY_COUNT = "retryCount";

    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";
}
