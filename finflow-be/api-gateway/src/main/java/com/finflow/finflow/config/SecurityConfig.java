package com.finflow.finflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http){
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(ex -> ex
                                .pathMatchers("/actuator/**").permitAll()
                                .anyExchange().authenticated()
                        )
                .oauth2ResourceServer(oauth -> oauth.jwt())
                .build();
    }

}
