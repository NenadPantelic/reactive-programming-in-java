package sec03;

import common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class Ex11FluxDefer {

    public static void main(String[] args) {
        // to delay an execution
        Flux.defer(
                () -> Flux.fromIterable(List.of(1, 2, 3)) // accepts a supplier
        );

        Flux.defer(() -> Flux.fromIterable(List.of(1, 2, 3)))
                .subscribe(Util.subscriber());
    }
}
