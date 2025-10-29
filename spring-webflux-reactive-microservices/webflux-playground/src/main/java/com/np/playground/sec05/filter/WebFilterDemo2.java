package com.np.playground.sec05.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Service
//@Order(2) // filter order
public class WebFilterDemo2 implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(WebFilterDemo2.class);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("received 2");
        // return Mono.empty();
        return chain.filter(exchange);
    }
}
