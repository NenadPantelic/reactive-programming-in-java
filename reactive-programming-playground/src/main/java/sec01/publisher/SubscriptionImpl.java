package sec01.publisher;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionImpl implements Subscription {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionImpl.class);
    private static final int MAX_ITEMS = 10; // max number of items per request

    private final Faker faker;
    private final Subscriber<? super String> subscriber;
    private boolean isCancelled;
    private int count = 0;

    public SubscriptionImpl(Subscriber<? super String> subscriber) {
        this.subscriber = subscriber;
        this.faker = Faker.instance();
    }

    @Override
    public void request(long requested) {
        // on this occasion, the publisher produces a message
        if (isCancelled) {
            // the pubsub is cancelled, just get out
            return;
        }

        // to invoke the error
        if (requested > MAX_ITEMS) {
            this.subscriber.onError(new RuntimeException("Validation failed"));
            this.isCancelled = true; // no more data to be produced after this
            return;
        }

        log.info("Subscriber has requested {} items", requested);
        for (int i = 0; i < requested && count < MAX_ITEMS; i++) {
            count++;
            this.subscriber.onNext(faker.internet().emailAddress());
        }

        if (count == MAX_ITEMS) {
            log.info("No more data to produce");
            subscriber.onComplete();
            isCancelled = true;
        }
    }

    @Override
    public void cancel() {
        log.info("Subscriber has cancelled");
        this.isCancelled = true;
    }
}
