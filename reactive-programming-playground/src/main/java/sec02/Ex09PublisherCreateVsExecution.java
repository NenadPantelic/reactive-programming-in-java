package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
 * Creating publisher is a lightweight operation.
 * Executing time-consuming business logic should be delayed.
 * */
public class Ex09PublisherCreateVsExecution {

    private static final Logger log = LoggerFactory.getLogger(Ex09PublisherCreateVsExecution.class);

    public static void main(String[] args) {
        getName(); // publisher create; will print out `entered the method` immediately

        getName().subscribe(Util.subscriber()); // will print out
        // entered the method
        // generating name
        // after 3 seconds
        // received: Kym
        // received complete!
    }

    private static Mono<String> getName() {
        log.info("entered the method");
        // we are just creating a publisher
        // supplier is delaying execution
        return Mono.fromSupplier(() -> {
            log.info("generating name");
            Util.sleepSeconds(3);
            return Util.faker().name().firstName();
        });
    }
}
