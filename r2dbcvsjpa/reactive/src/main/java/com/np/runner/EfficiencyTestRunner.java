package com.np.runner;

import com.np.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@ConditionalOnProperty(value = "efficiency.test", havingValue = "true")
public class EfficiencyTestRunner implements CommandLineRunner {

    private static final int MILLION = 1_000_000;
    private static final Logger log = LoggerFactory.getLogger(EfficiencyTestRunner.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        var atomicInteger = new AtomicInteger(0);
        log.info("Starting...");
        // works even with just 100 MB of memory
        // flux is a pipe through which data (bytes) flows
        // actually, internally it has a queue holding up to 256 items
        // consumers drain the items from this queue
        // if the consumer is slow, that queue will become full very quickly
        // to slow down the data transfer, the receiver will send a message as part
        // of the TCP acknowledgment to the database saying that the consumer is slow
        // and the receiver buffer is full, so the message will be sent which most of the databases
        // understand, and they react by not sending any more data
        // that mechanism is called `TCP backpressure`
        customerRepository.findAll()
                .doOnNext(c -> {
                    // for each customer increment the counter
                    // print every million
                    var count = atomicInteger.incrementAndGet();
                    if (count % MILLION == 0) {
                        log.info("Counter: {}", count);
                    }
                }).then()
                .block(); // for demo
        log.info("Done");
    }
}
