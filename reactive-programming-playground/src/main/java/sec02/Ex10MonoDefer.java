package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

/*
 * To delay the publisher creation
 * */
public class Ex10MonoDefer {

    private static final Logger log = LoggerFactory.getLogger(Ex10MonoDefer.class);

    public static void main(String[] args) {
        /*
        var list = List.of(1, 2, 3);
        Mono.fromSupplier(() -> sum(list))
                .subscribe(Util.subscriber());

        createPublisher().subscribe();
        createPublisher();
        */

        Mono.defer(Ex10MonoDefer::createPublisher); // nothing will be printed out
        Mono.defer(Ex10MonoDefer::createPublisher).subscribe(Util.subscriber());
    }

    private static Mono<Integer> createPublisher() {
        log.info("Creating publisher 22");
        var list = List.of(1, 2, 3);
        Util.sleepSeconds(1);
        return Mono.fromSupplier(() -> sum(list));
    }

    // time-consuming business logic
    private static Integer sum(List<Integer> list) {
        log.info("finding the sum: {}", list);
        return list.stream().mapToInt(a -> a).sum();
    }
}
