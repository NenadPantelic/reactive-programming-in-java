package com.np.playground.tests.sec02;

import com.np.playground.sec02.dto.OrderDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

public class Lec04CustomerOrderRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    public void testGetOrderDetailsByProduct() {
        var query = """
                SELECT
                  co.order_id,
                  c.name AS customer_name,
                  p.description AS product_name,
                  co.amount,
                  co.order_date
                FROM
                  customer c
                INNER JOIN customer_order co ON c.id = co.customer_id
                INNER JOIN product p ON p.id = co.product_id
                WHERE
                  p.description = :description
                ORDER BY co.amount DESC
                """;

        databaseClient.sql(query).bind("description", "iphone 18")
                .mapProperties(OrderDetails.class)
                .all()
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
