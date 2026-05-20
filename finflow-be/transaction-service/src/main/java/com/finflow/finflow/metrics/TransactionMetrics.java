package com.finflow.finflow.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TransactionMetrics {

    private final Counter transactionSuccessCounter;
    private final Counter transactionFailureCounter;
    private final Counter transactionCompensationCounter;
    private final Counter transactionInitiatedCounter;

    public TransactionMetrics(MeterRegistry meterRegistry) {
        this.transactionSuccessCounter =
                Counter.builder("transactions_completed_total")
                        .description("Total completed transactions")
                        .register(meterRegistry);

        this.transactionFailureCounter =
                Counter.builder("transactions_failed_total")
                        .description("Total failed transactions")
                        .register(meterRegistry);

        this.transactionCompensationCounter =
                Counter.builder("transactions_compensated_total")
                        .description("Total compensated transactions")
                        .register(meterRegistry);

        this.transactionInitiatedCounter =
                Counter.builder("transactions_initiated_total")
                        .description("Total initiated transactions")
                        .register(meterRegistry);
    }

    public void incrementSuccess() {
        transactionSuccessCounter.increment();
    }

    public void incrementFailure() {
        transactionFailureCounter.increment();
    }

    public void incrementCompensation() {
        transactionCompensationCounter.increment();
    }

    public void incrementInitiated(){
        transactionInitiatedCounter.increment();
    }

}
