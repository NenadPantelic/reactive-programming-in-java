package com.np.playground.tests.sec02;

import com.np.playground.sec02.entity.Customer;
import com.np.playground.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec01CustomerRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindAll() {
        customerRepository.findAll() // 10 records
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindById() {
        customerRepository.findById(2)
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertEquals("mike", customer.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindByName() {
        customerRepository.findByName("jake")
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertEquals("jake", customer.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindByEmailEndsWith() {
        customerRepository.findByEmailEndsWith("gmail.com")
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    public void testInsertAndDelete() {
        var customer = new Customer();
        customer.setName("john");
        customer.setEmail("john@example.com");

        customerRepository.save(customer)
                .doOnNext(c -> log.info("Customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> {
                    Assertions.assertNotNull(c.getId());
                    Assertions.assertEquals(c.getName(), "john");
                    Assertions.assertEquals(c.getEmail(), "john@example.com");
                })
                .expectComplete()
                .verify();

        customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        customerRepository.deleteById(11)
                .then(customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testUpdateCustomer() {
        customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("noel"))
                .flatMap(customer -> customerRepository.save(customer))
                .doOnNext(c -> log.info("Customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotEquals(c.getName(), "ethan"))
                .expectComplete()
                .verify();
    }
}
