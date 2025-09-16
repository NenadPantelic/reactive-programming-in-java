package sec03;

import common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Ex09FluxInterval {

    public static void main(String[] args) {
        Flux.interval(Duration.ofMillis(500)) // emits a message every 500ms
                .map(i -> Util.faker().name().firstName())
                .subscribe(Util.subscriber());
        // since it uses a separate thread for this, and the main thread will exit immediately,
        // we have to block the main thread

        // 10 times - 0.5s * 10
        // but the interval thread would never stop
        Util.sleepSeconds(5);
    }
}
