package sec03;

import common.Util;
import reactor.core.publisher.Flux;

public class Ex05FluxRange {

    public static void main(String[] args) {
        Flux.range(1, 10) // acts like a for loop
                // start, how many items
                .subscribe(Util.subscriber());

        Flux.range(1, 10)
                .map(i -> Util.faker().random().hex())
                .subscribe(Util.subscriber());

    }
}