package com.np.runner;

import com.np.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@ConditionalOnProperty(value = "throughput.test", havingValue = "true")
public class ThroughputTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ThroughputTestRunner.class);
    private static final int TASKS_COUNT = 100_000;

    @Autowired
    private CustomerRepository customerRepository;

//    @Value("${useVirtualThreadExecutor:false}")
//    private boolean useVirtualThreadExecutor;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting...");
//        log.info("Will use a virtual thread executor: {}", useVirtualThreadExecutor);

        var executorService = getFixedExecutor();
        for (int iteration = 1; iteration <= 10; iteration++) {
            // repeat test 10 times, the first test is for warm-up (Java has a cold startup)
            measureTimeTaken(iteration, () -> runTest(executorService));
        }
    }

    private void measureTimeTaken(int iteration, Runnable runnable) {
        var startTime = System.currentTimeMillis();
        runnable.run();
        var timeTaken = System.currentTimeMillis() - startTime; // in millis
        var throughput = (1.0 * TASKS_COUNT / timeTaken) * 1000; // in seconds
        log.info("Test {}: time taken = {}, throughput = {}", iteration, timeTaken, throughput);
    }

    // Makes TASKS_COUNT calls. Each call is for fetching a single customer.
    private void runTest(ExecutorService executorService) {
        for (int i = 1; i <= TASKS_COUNT; i++) {
            final var customerId = i;
            executorService.submit(() -> customerRepository.findById(customerId));
        }
    }

    private ExecutorService getFixedExecutor() {
        return Executors.newFixedThreadPool(256); // reactor sends 256 at a time via flatMap, making a
        // comparison fair
    }

//    private ExecutorService getVirtualExecutor() {
//        // This executor does not have any internal queue. All the submitted tasks will be executed concurrently!
//        // You might want to use semaphore to limit the concurrency if it is important!
//        return Executors.newVirtualThreadPerTaskExecutor();
//    }


}
