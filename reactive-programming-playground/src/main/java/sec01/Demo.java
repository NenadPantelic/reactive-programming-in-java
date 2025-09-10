package sec01;

import sec01.publisher.PublisherImpl;
import sec01.subscriber.SubscriberImpl;


/*
 * 1. publisher does not produce data unless subscriber requests for it
 * 2. publisher will produce only <= subscriber requested items. Publisher can also produce 0 items!
 * 3. subscriber can cancel the subscription, producer should also stop at that moment as subscriber is no longer
 * interested in consuming the data
 * 4. producer can send the error signal to indicate something is wrong
 *
 * */
public class Demo {

    public static void main(String[] args) throws InterruptedException {
        //  demo1();
        // demo2();
        // demo3();
        demo4();
    }

    private static void demo1() {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber); // subscriber is subscribed to the publisher
    }

    private static void demo2() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber); // subscriber is subscribed to the publisher

        subscriber.getSubscription().request(3);
        Thread.sleep(3000); // 3 seconds
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
    }

    private static void demo3() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber); // subscriber is subscribed to the publisher

        subscriber.getSubscription().request(3);
        Thread.sleep(3000); // 3 seconds
        subscriber.getSubscription().cancel();
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
    }

    private static void demo4() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber); // subscriber is subscribed to the publisher

        subscriber.getSubscription().request(3);
        Thread.sleep(3000); // 3 seconds
        subscriber.getSubscription().request(15);
        Thread.sleep(3000);
        subscriber.getSubscription().request(3);
        Thread.sleep(3000);
    }
}
