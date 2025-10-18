package com.np.playground.tests.sec02;

import com.np.playground.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import reactor.test.StepVerifier;

public class Lec02ProductRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByPriceBetween() {
        /*
        ('iphone 20', 1000),
        ('iphone 18', 750),
        ('ipad', 800),
        ('mac pro', 3000),
        ('apple watch', 400),
        ('macbook air', 1200),
        ('airpods pro', 250),
        ('imac', 2000),
        ('apple tv', 200),
        ('homepod', 300);
        */
        productRepository.findByPriceBetween(500, 2000)
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals("iphone 20", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("iphone 18", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("ipad", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("macbook air", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("imac", p.getDescription()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindBy() {
        productRepository.findBy(PageRequest.of(1, 3))
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals("mac pro", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("apple watch", p.getDescription()))
                .assertNext(p -> Assertions.assertEquals("macbook air", p.getDescription()))
                .expectComplete()
                .verify();
    }
}
