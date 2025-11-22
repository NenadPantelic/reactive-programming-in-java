package com.np.playground.sec06.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.function.BiFunction;

@Configuration
public class CalculatorAssignment {

    @Bean
    public RouterFunction<ServerResponse> calculator() {
        return RouterFunctions.route()
                .path("calculator", this::calculatorRoutes)
                .build();
    }

    private RouterFunction<ServerResponse> calculatorRoutes() {
        return RouterFunctions.route()
                .GET("/{a}/0", badRequest("b cannot be 0"))
                .GET("/{a}/{b}", isOperation("+"), handle((a, b) -> a + b))
                .GET("/{a}/{b}", isOperation("-"), handle((a, b) -> a - b))
                .GET("/{a}/{b}", isOperation("*"), handle((a, b) -> a * b))
                .GET("/{a}/{b}", isOperation("/"), handle((a, b) -> a / b))
                .GET("/{a}/{b}", badRequest("the operation header must be sent in request: +-*/"))
                .build();
    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(h -> operation.equals(h.firstHeader("operation")));
    }

    private HandlerFunction<ServerResponse> handle(BiFunction<Integer, Integer, Integer> function) {
        return request -> {
            var a = Integer.parseInt(request.pathVariable("a"));
            var b = Integer.parseInt(request.pathVariable("b"));
            var result = function.apply(a, b);
            return ServerResponse.ok().bodyValue(result);
        };
    }

    private HandlerFunction<ServerResponse> badRequest(String message) {
        return request -> ServerResponse.badRequest().bodyValue(message);
    }
}
