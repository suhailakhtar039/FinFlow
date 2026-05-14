package com.finflow.finflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JwtConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {

        return NimbusReactiveJwtDecoder
                .withJwkSetUri(
                        "http://localhost:8443/realms/finflow/protocol/openid-connect/certs"
                )
                .build();
    }
}