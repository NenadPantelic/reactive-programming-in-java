package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

public class Ex06MonoFromCallable {

    private static final Logger log = LoggerFactory.getLogger(Ex06MonoFromCallable.class);

    public static void main(String[] args) {
        // Supplier vs Callable

        // Supplier
        // a functional interface  that showed up in Java 8
        // it does not throw any checked exception (it can throw runtime exceptions)

        // Callable
        // a functional interface that got introduced 20+ years ago
        // it can throw an exception, part of its signature
        var nums = List.of(1, 2, 3);


        Mono.fromCallable(() -> sum(nums));
//        Mono.fromSupplier(() -> sum(nums)).subscribe(Util.subscriber()); // cannot use the supplier with a function
        // that can throw an exception
    }

    private static int sum(List<Integer> values) throws Exception {
        log.info("Finding the sum of {}", values);
        return values.stream().mapToInt(i -> i).sum();
    }
}
