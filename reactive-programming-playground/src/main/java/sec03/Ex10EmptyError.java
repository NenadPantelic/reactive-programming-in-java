package sec03;

import common.Util;
import reactor.core.publisher.Flux;

public class Ex10EmptyError {

    public static void main(String[] args) {
        Flux.empty() // only complete will be emitted
                .subscribe(Util.subscriber());

        Flux.error(new RuntimeException("Error"))
                .subscribe(Util.subscriber()); // only on error will be emitted
    }
}
