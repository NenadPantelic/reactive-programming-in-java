package com.np.playground.sec03.controller;

import com.np.playground.sec03.dto.CustomerDTO;
import com.np.playground.sec03.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Flux<CustomerDTO> listCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/paginated")
    public Flux<CustomerDTO> listCustomersPaginated(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return customerService.getCustomersPaginated(page, size);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomer(@PathVariable("id") Integer id) {
        return toResponseEntity(customerService.getCustomerById(id));
    }

    @PostMapping
    public Mono<CustomerDTO> saveCustomer(@RequestBody Mono<CustomerDTO> mono) { // when mono is received, its processing
        // can start even before the complete payload is received
        // reactive operators will keep us safe, they will process the input once it is received
        return customerService.saveCustomer(mono);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomer(@PathVariable("id") Integer id, @RequestBody Mono<CustomerDTO> mono) {
        return toResponseEntity(customerService.updateCustomer(id, mono));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable("id") Integer id) {
        return customerService.deleteUser(id)
                .filter(b -> b) // keep only true values
                .map(b -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Mono<ResponseEntity<CustomerDTO>> toResponseEntity(Mono<CustomerDTO> mono) {
        return mono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
