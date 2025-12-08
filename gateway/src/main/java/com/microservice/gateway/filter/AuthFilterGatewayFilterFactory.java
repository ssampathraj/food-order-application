package com.microservice.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.gateway.util.JwtUtil;

@Component
public class AuthFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthFilterGatewayFilterFactory.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
            }

            // OPTIONAL → pass user ID / roles to downstream service
            String username = jwtUtil.extractUsername(token);

            exchange = exchange.mutate()
                    .request(req -> req.headers(headers -> {
                        headers.add("X-User-Id", username);
                        // add more headers if needed
                    }))
                    .build();

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // No configuration needed
    }
}
