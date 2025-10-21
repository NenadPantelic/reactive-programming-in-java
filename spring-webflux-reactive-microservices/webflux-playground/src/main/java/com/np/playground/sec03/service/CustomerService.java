package com.np.playground.sec03.service;

import com.np.playground.sec03.dto.CustomerDTO;
import com.np.playground.sec03.entity.Customer;
import com.np.playground.sec03.mapper.EntityDTOMapper;
import com.np.playground.sec03.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .map(EntityDTOMapper::toDTO);
    }

    public Flux<CustomerDTO> getCustomersPaginated(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 50);
        return customerRepository.findBy(pageable)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> mono) {
        return mono.map(EntityDTOMapper::toEntity)
                .flatMap(customerRepository::save)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> updateCustomer(Integer id, Mono<CustomerDTO> mono) {
        return customerRepository.findById(id)
                .flatMap(entity -> mono)
                .map(EntityDTOMapper::toEntity)
                .doOnNext(c -> c.setId(id))
                .flatMap(customerRepository::save)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<Boolean> deleteUser(Integer id) {
        return customerRepository.deleteCustomerById(id);
    }
}
