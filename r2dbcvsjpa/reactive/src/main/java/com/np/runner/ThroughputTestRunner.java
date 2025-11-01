package com.np.runner;

import com.np.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@ConditionalOnProperty(value = "throughput.test", havingValue = "true")
public class ThroughputTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ThroughputTestRunner.class);
    private static final int TASKS_COUNT = 100_000;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting...");

        for (int iteration = 1; iteration <= 10; iteration++) {
            // repeat test 10 times, the first test is for warm-up (Java has a cold startup)
            measureTimeTaken(iteration, this::runTest);
        }
    }

    private void measureTimeTaken(int iteration, Runnable runnable) {
        var startTime = System.currentTimeMillis();
        runnable.run();
        var timeTaken = System.currentTimeMillis() - startTime; // in millis
        var throughput = (1.0 * TASKS_COUNT / timeTaken) * 1000; // in seconds
        log.info("Test {}: time taken = {}, throughput = {}/sec", iteration, timeTaken, throughput);
    }

    // Makes TASKS_COUNT calls. Each call is for fetching a single customer.
    private void runTest() {
        Flux.range(1, TASKS_COUNT)
                .flatMap(id -> customerRepository.findById(id))
                .then()
                .block(); // wait for all the tasks to complete
    }
}
