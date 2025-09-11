package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

public class Ex05MonoFromSupplier {

    private static final Logger log = LoggerFactory.getLogger(Ex05MonoFromSupplier.class);

    public static void main(String[] args) {
        var nums = List.of(1, 2, 3);
        Mono.just(sum(nums)).subscribe(Util.subscriber()); // executed

        var mono = Mono.just(sum(nums)); // no subscriber, but it is still executed. If it is a resource exhaustive operation
        // it would be a waste of computing cycles
        // so, we only want to execute when it is required

        // Mono.just is used when we have values in memory, but with that sum that is not case
        // we have to compute it
        System.out.println("Mono from callable");
        // To delay the operation, we can use supplier
        Mono.fromSupplier(() -> sum(nums)); // not executed until it is needed
        Mono.fromSupplier(() -> sum(nums)).subscribe(Util.subscriber()); // executed because we have a subscriber
    }

    private static int sum(List<Integer> values) {
        log.info("Finding the sum of {}", values);
        return values.stream().mapToInt(i -> i).sum();
    }
}
