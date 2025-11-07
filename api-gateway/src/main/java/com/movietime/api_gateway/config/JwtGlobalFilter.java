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
        String path = exchange.getRequest().getPath().value();

        // Allow auth endpoints to pass through (signup/signin handled by user-service)
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(JwtConstant.JWT_HEADER);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Missing or malformed Authorization header for path: {}", path);
            return unauthorized(exchange);
        }

        try {
            String token = authHeader.substring(7).trim();
            SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
            // will throw JwtException on invalid/expired
            var jwt = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            var claims = jwt.getBody();
            logger.debug("JWT validated for path={} ; claims={}", path, claims);

            // propagate a trusted header with the user's email (if present)
            String email = claims.get("email", String.class);
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Email", email == null ? "" : email)
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return chain.filter(mutatedExchange);
        } catch (JwtException e) {
            logger.debug("JWT validation failed: {}", e.getMessage());
            return unauthorized(exchange);
        } catch (Exception e) {
            logger.error("Unexpected error while validating JWT", e);
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
        // run early
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
