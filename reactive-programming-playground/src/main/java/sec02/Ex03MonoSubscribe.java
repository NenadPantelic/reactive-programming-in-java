package sec02;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Ex03MonoSubscribe {

    private static final Logger log = LoggerFactory.getLogger(Ex03MonoSubscribe.class);

    public static void main(String[] args) {
//        demo1();
        demo2();
    }

    private static void demo1() {
        var mono = Mono.just(1);
        mono.subscribe(
                i -> log.info("Received: {}", i),
                err -> log.error("Error", err), // error handler
                () -> log.info("Completed") // completion handler
                // Subscription::cancel // subscription code
        );
        // questions:
        // 1. where is complete?
        // 2. subscriber only subscribed itself to the publisher, but didn't request for any item
    }

    private static void demo2() {
        var mono = Mono.just(1).map(i -> i / 0);
        mono.subscribe(
                i -> log.info("Received: {}", i),
                err -> log.error("Error", err), // error handler
                () -> log.info("Completed") // completion handler
                // Subscription::cancel // subscription code
        );
    }
}
