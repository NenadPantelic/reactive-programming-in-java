package common;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;

public class Util {

    private static final Faker faker = Faker.instance();

    public static <T> Subscriber<T> subscriber(String name) {
        return new DefaultSubscriber<>(name);
    }

    public static <T> Subscriber<T> subscriber() {
        return subscriber("");
    }

    public static void main(String[] args) {
        var mono = Mono.just(1);
        mono.subscribe(subscriber("sub1")); // subscriber subscriber to a mono single-value emitter, so
        // request will only yield one value regardless of the Long.MAX_VALUE set. After that one value is published
        // the complete handler is triggered
        mono.subscribe(subscriber("sub2"));

        Util.faker().name().firstName();
    }

    public static Faker faker() {
        return faker;
    }

    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
