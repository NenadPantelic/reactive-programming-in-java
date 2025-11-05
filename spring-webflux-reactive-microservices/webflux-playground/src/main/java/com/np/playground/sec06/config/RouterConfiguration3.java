package com.np.playground.sec06.config;

import com.np.playground.sec06.exception.CustomerNotFoundException;
import com.np.playground.sec06.exception.InvalidInputException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration3 {

    private final CustomerRequestHandler customerRequestHandler;
    private final ApplicationExceptionHandler exceptionHandler;

    public RouterConfiguration3(CustomerRequestHandler customerRequestHandler,
                                ApplicationExceptionHandler exceptionHandler) {
        this.customerRequestHandler = customerRequestHandler;
        this.exceptionHandler = exceptionHandler;
    }

    private RouterFunction<ServerResponse> customerRoutes1() {
        return RouterFunctions.route()
                // chain of predicates, the first one which predicate is true will handle the request
                .GET(req -> false, this.customerRequestHandler::paginatedCustomers)
                // the id should start with one (1x)
                .GET("/api/v1/customers/{id}", RequestPredicates.path("*/1?"), customerRequestHandler::getCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }
}
