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
//@Order(1) // filter order
public class WebFilterDemo1 implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(WebFilterDemo1.class);

    // with this we will get an empty response/200 no matter which endpoint is hit and if the request would cause a
    // failure in regular conditions
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("received 1");
        // return Mono.empty(); // it will return the result won't pass down the request to the next filter in the chain
        return chain.filter(exchange);
    }
}
