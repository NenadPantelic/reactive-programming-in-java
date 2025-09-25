package sec03.assignment;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * - The stock price service will emit price changes every 500ms for ~20 seconds.
 * - The price change might change between 80-120
 * Task:
 * - create a subscriber with $1000 balance
 * - whenever the price drops below 90, you buy a stock
 * - when the price goes above 110
 *   - you sell all the quantities
 *   - cancel the subscription
 *   - print the profit you made
 *
 *
 * */

public class StockPriceObserver implements Subscriber<Integer> {

    private static final Logger log = LoggerFactory.getLogger(StockPriceObserver.class);
    private int balance = 1000; // balance to purchase stocks
    private int quantity = 0; // no of stocks

    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("Subscriber registering...");
        subscription.request(Long.MAX_VALUE);
        this.subscription = subscription;
    }

    @Override
    public void onNext(Integer price) {
        if (price < 90 && balance >= price) {
            quantity++;
            balance -= price;
            log.info("Stock purchased. Current balance = {}, quantity = {}", balance, quantity);
        } else if (price > 110 && quantity > 0) {
            log.info("Selling all stocks ({}) at {}", quantity, price);
            balance += quantity * price;
            quantity = 0;
            this.subscription.cancel();
            log.info("New balance = {}, profit = {}", balance, balance - 1000);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error: {}", throwable.getMessage(), throwable);
    }

    @Override
    public void onComplete() {
        log.info("Completed!");
    }
}
