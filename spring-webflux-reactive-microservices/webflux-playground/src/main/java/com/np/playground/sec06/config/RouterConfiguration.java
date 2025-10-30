package com.np.playground.sec06.config;

import com.np.playground.sec06.exception.CustomerNotFoundException;
import com.np.playground.sec06.exception.InvalidInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    private final CustomerRequestHandler customerRequestHandler;
    private final ApplicationExceptionHandler exceptionHandler;

    public RouterConfiguration(CustomerRequestHandler customerRequestHandler,
                               ApplicationExceptionHandler exceptionHandler) {
        this.customerRequestHandler = customerRequestHandler;
        this.exceptionHandler = exceptionHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> customerReadRoutes() {
        return RouterFunctions.route()
                .GET("/customers", customerRequestHandler::listCustomers)
                // pay attention
                // /customers/paginated
                // /customers/{id}
                // if we hit /customers/paginated, we will get the response from /customers/paginated

                // /customers/{id}
                // /customers/paginated
                // if we hit /customers/paginated, we will get the response from /customers/{id}
                // it will think that the id value is `paginated`
                .GET("/api/v1/customers/paginated", this.customerRequestHandler::paginatedCustomers)
                .GET("/api/v1/customers/{id}", customerRequestHandler::getCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customerWriteRoutes() {
        return RouterFunctions.route()
                .POST("/api/v1/customers", customerRequestHandler::saveCustomer)
                .PUT("/api/v1/customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("/api/v1/customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }
}
