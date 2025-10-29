package com.np.playground.sec05.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
@Order(1)
public class AuthenticationFilter implements WebFilter {

    private static final Map<String, UserCategory> CREDENTIAL_USER_CATEGORY_MAP = Map.of(
            "secret123", UserCategory.STANDARD,
            "secret456", UserCategory.PRIME
    );
    private static final String AUTH_TOKEN_HEADER = "auth-token";

    private final FilterErrorHandler errorHandler;

    public AuthenticationFilter(FilterErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authToken = exchange.getRequest().getHeaders().getFirst(AUTH_TOKEN_HEADER);

        if (Objects.nonNull(authToken) && CREDENTIAL_USER_CATEGORY_MAP.get(authToken) != null) {
            exchange.getAttributes().put("category", CREDENTIAL_USER_CATEGORY_MAP.get(authToken));
            return chain.filter(exchange);
        }

        // Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
        return errorHandler.sendProblemDetail(exchange, HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
}
