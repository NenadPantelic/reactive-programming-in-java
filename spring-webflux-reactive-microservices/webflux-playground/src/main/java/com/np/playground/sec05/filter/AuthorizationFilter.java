package com.np.playground.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Service
@Order(2)
public class AuthorizationFilter implements WebFilter {

    private final FilterErrorHandler errorHandler;

    public AuthorizationFilter(FilterErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        UserCategory userCategory = exchange.getAttributeOrDefault("category", UserCategory.STANDARD);
        return userCategory == UserCategory.PRIME ? prime(exchange, chain) : standard(exchange, chain);
    }

    private Mono<Void> prime(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange);
    }


    private Mono<Void> standard(ServerWebExchange exchange, WebFilterChain chain) {
        HttpMethod httpMethod = exchange.getRequest().getMethod();
        if (httpMethod != HttpMethod.GET) {
            return errorHandler.sendProblemDetail(exchange, HttpStatus.FORBIDDEN, "Forbidden");
            // return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
        }

        return chain.filter(exchange);
    }
}
