package com.np.playground.sec06.config;

import com.np.playground.sec06.service.CustomerService;
import com.np.playground.sec06.exception.ApplicationExceptions;
import com.np.playground.sec06.validator.RequestValidator;
import com.np.playground.sec06.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    private final CustomerService customerService;


    public CustomerRequestHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Mono<ServerResponse> listCustomers(ServerRequest serverRequest) {
        //  serverRequest.pathVariable();
        //  serverRequest.headers();
        //  serverRequest.queryParam();
        return customerService.getAllCustomers()
                .as(flux -> ServerResponse.ok().body(flux, CustomerDTO.class));
    }


    public Mono<ServerResponse> paginatedCustomers(ServerRequest serverRequest) {
        var page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        var size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(5);
        return this.customerService.getCustomersPaginated(page, size)
                .as(flux -> ServerResponse.ok().body(flux, CustomerDTO.class));
    }

    public Mono<ServerResponse> getCustomer(ServerRequest serverRequest) {
        var id = Integer.parseInt(serverRequest.pathVariable("id"));
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CustomerDTO.class)
                .transform(RequestValidator.validate())
                .as(customerService::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        var id = Integer.parseInt(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(CustomerDTO.class)
                .transform(RequestValidator.validate())
                .as(validatedReq -> customerService.updateCustomer(id, validatedReq))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest serverRequest) {
        var id = Integer.parseInt(serverRequest.pathVariable("id"));
        return customerService.deleteUser(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then(ServerResponse.ok().build());
    }
}
