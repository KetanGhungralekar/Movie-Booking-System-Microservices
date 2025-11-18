package com.movietime.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(JwtGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("#### JWT FILTER LOADED NEW VERSION ####");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. ALWAYS allow OPTIONS preflight requests
        if (request.getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }

        // 2. Allow auth endpoints without JWT
        if (path.startsWith("/auth/") || path.startsWith("/movies/")) {
            return chain.filter(exchange);
        }

        // 3. JWT required for all other endpoints
        String authHeader = request.getHeaders().getFirst(JwtConstant.JWT_HEADER);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Missing or malformed Authorization header for path: {}", path);
            return unauthorized(exchange);
        }

        try {
            String token = authHeader.substring(7).trim();
            SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
            var jwt = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

            String email = jwt.getBody().get("email", String.class);

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Email", email != null ? email : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            logger.debug("JWT validation failed: {}", e.getMessage());
            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
             // *** THE FIX IS HERE ***
        // Set a low precedence value (e.g., 50). This ensures this filter runs AFTER 
        // Spring Cloud Gateway's internal filters, including the CORS filter.
        return 1000;
    }
}
