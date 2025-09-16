package sec03;

import common.Util;
import reactor.core.publisher.Flux;

public class Ex02MultipleSubscriber {

    public static void main(String[] args) {
        var flux = Flux.just(1, 2, 3, 4, 5, 6);

        // all these subscribers will receive all items
        flux.subscribe(Util.subscriber("sub1"));
        flux.subscribe(Util.subscriber("sub2"));
        flux
                .filter(i -> i % 2 == 0)
                .map(i -> i * 2)
                .subscribe(Util.subscriber("sub3"));
    }
}
