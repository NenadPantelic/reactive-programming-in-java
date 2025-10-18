package com.np.playground.tests.sec02;

import com.np.playground.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec03CustomerOrderRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    public void testProductsOrderedByCustomer() {
        customerOrderRepository.getProductsOrderedByCustomer("sam")
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals("iphone 20", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("iphone 18", p.getDescription()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetOrderDetailsByProduct() {
        customerOrderRepository.getOrderDetailsByProduct("iphone 18")
                .doOnNext(od -> log.info("Order details: {}", od))
                .as(StepVerifier::create)
                .assertNext(od -> {
                    Assertions.assertEquals("iphone 18", od.productName());
                    Assertions.assertEquals("sam", od.customerName());
                    Assertions.assertEquals(850, od.amount());
                })
                .assertNext(od -> {
                    Assertions.assertEquals("iphone 18", od.productName());
                    Assertions.assertEquals("jake", od.customerName());
                    Assertions.assertEquals(775, od.amount());
                })
                .assertNext(od -> {
                    Assertions.assertEquals("iphone 18", od.productName());
                    Assertions.assertEquals("jake", od.customerName());
                    Assertions.assertEquals(750, od.amount());
                })
                .expectComplete()
                .verify();
    }
}
