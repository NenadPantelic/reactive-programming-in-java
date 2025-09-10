package sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * If we do not have the terminal operator, then stream operators will not execute
 * */
public class Ex01LazyStream {

    private static final Logger log = LoggerFactory.getLogger(Ex01LazyStream.class);

    public static void main(String[] args) {
        // Java Stream by default is lazy
        Stream.of(1)
                .peek(i -> log.info("Received {}", i)) //  by default, this ☝ will not be executed
                // we need the terminal operator for stream to be executed, e.g. .collect()
                // practically, we are subscribing to it
                .collect(Collectors.toList());
    }
}
