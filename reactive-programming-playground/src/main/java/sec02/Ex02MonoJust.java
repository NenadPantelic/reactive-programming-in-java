package sec02;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import sec01.subscriber.SubscriberImpl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ex02MonoJust {

    private static final Logger log = LoggerFactory.getLogger(Ex02MonoJust.class);

    public static void main(String[] args) {
        Mono<String> mono = Mono.just("Nenad");
        log.info("Mono: {}", mono); // will not print anything useful
        // we have to subscribe to get the data, only then the publisher will emit
        var subscriber = new SubscriberImpl();
        mono.subscribe(subscriber);
        subscriber.getSubscription().request(1);
        // it only emits one value, so this is useless
        subscriber.getSubscription().request(10);
        // cancellation is pointless after it already emitted the value
        subscriber.getSubscription().cancel();

        var subscriber2 = new SubscriberImpl();
        mono.subscribe(subscriber2);
        // will print just one value
        subscriber2.getSubscription().request(2);

        var subscriber3 = new SubscriberImpl();
        mono.subscribe(subscriber3);
        subscriber3.getSubscription().cancel();
        // useless because it is already cancelled
        subscriber3.getSubscription().request(3);

        save(Mono.just("Nenad"));
    }

    public static void save(Publisher<String> publisher) {
        // do whatever
    }
}
