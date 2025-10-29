package com.np.playground.sec05.mapper;

import com.np.playground.sec05.dto.CustomerDTO;
import com.np.playground.sec05.entity.Customer;

public class EntityDTOMapper {

    public static Customer toEntity(CustomerDTO customerDTO) {
        var customer = new Customer();
        customer.setEmail(customerDTO.email());
        customer.setId(customerDTO.id());
        customer.setName(customerDTO.name());
        return customer;
    }

    public static CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail());
    }
}