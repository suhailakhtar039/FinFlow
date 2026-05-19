package com.finflow.finflow.filter;

import com.finflow.dto.constants.Headers;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId =
                exchange
                        .getRequest()
                        .getHeaders()
                        .getFirst(Headers.CORRELATION_ID);

        if(correlationId == null || correlationId.isBlank()){
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put("correlationId", correlationId);

        ServerHttpRequest mutatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header(Headers.CORRELATION_ID, correlationId)
                        .build();

        exchange.getResponse()
                .getHeaders()
                .add(Headers.CORRELATION_ID, correlationId);

        return chain.filter(
                exchange.mutate()
                        .request(mutatedRequest)
                        .build()
        ).doFinally(signal -> MDC.clear());

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
