package com.np.playground.sec05.controller;

import com.np.playground.sec05.dto.CustomerDTO;
import com.np.playground.sec05.exception.ApplicationExceptions;
import com.np.playground.sec05.filter.UserCategory;
import com.np.playground.sec05.service.CustomerService;
import com.np.playground.sec05.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Flux<CustomerDTO> listCustomers(@RequestAttribute("category") UserCategory userCategory) {
        log.info("Category received in list customer: {}", userCategory);
        return customerService.getAllCustomers();
    }

    @GetMapping("/paginated")
    public Flux<CustomerDTO> listCustomersPaginated(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return customerService.getCustomersPaginated(page, size);
    }

    @GetMapping("/{id}")
    public Mono<CustomerDTO> getCustomer(@PathVariable("id") Integer id) {
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDTO> saveCustomer(@RequestBody Mono<CustomerDTO> mono) { // when mono is received, its processing
        // can start even before the complete payload is received
        // reactive operators will keep us safe, they will process the input once it is received
        return mono.transform(RequestValidator.validate()) // use transform to apply validation
                .as(customerService::saveCustomer);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDTO> updateCustomer(@PathVariable("id") Integer id, @RequestBody Mono<CustomerDTO> mono) {
        return mono.transform(RequestValidator.validate()) // use transform to apply validation
                .as(transformedMono -> customerService.updateCustomer(id, transformedMono))
                // if it's not found, it will be emitting empty signal
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable("id") Integer id) {
        return customerService.deleteUser(id)
                .filter(b -> b) // keep only true values
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id)) // if true this will not be triggered
                .then();
    }

    private Mono<ResponseEntity<CustomerDTO>> toResponseEntity(Mono<CustomerDTO> mono) {
        return mono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
