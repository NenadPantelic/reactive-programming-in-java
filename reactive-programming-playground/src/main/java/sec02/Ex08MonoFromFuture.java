package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Ex08MonoFromFuture {

    private static final Logger log = LoggerFactory.getLogger(Ex08MonoFromFuture.class);

    public static void main(String[] args) {
        Mono.fromFuture(getName()).subscribe(Util.subscriber());
        // CompletableFuture usually uses a common forkjoin pool for any async operation
        // uses a separate thread
        // we need some sleep to help the subscriber get the value
        // we block the main thread to give enough time for a thread to complete
        Util.sleepSeconds(1);

        // flaw, even without a subscriber, the code gets executed
        Mono.fromFuture(getName()); // completable future is not lazy by default
        Util.sleepSeconds(1);

        Mono.fromFuture(Ex08MonoFromFuture::getName); // supplier make the completable future lazy
        Util.sleepSeconds(1);
    }

    private static CompletableFuture<String> getName() { // not lazy by default
        return CompletableFuture.supplyAsync(() -> {
            log.info("generating name");
            return Util.faker().name().firstName();
        });
    }
}
