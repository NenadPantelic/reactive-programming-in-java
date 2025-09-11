package sec02;

import common.Util;
import reactor.core.publisher.Mono;

public class Ex04MonoEmptyError {

    public static void main(String[] args) {
        // ok
        getUsername(1).subscribe(Util.subscriber("sub1"));

        // empty
        getUsername(2).subscribe(Util.subscriber("sub2"));

        // error
        getUsername(3).subscribe(Util.subscriber("sub3"));

        // error handling
//        getUsername(3).subscribe(System.out::println); // error is dropped by default if there is no an error handler
        // will stop the program

        // swallowing error
        getUsername(3).subscribe(
                System.out::println,
                err -> {
                }
        ); // error is dropped by default if there is no an error handler

        // previous error has been swallowed
        getUsername(2).subscribe(Util.subscriber("sub2"));
    }

    private static Mono<String> getUsername(int userId) {
        return switch (userId) {
            case 1 -> Mono.just("sam");
            case 2 -> Mono.empty(); // null
            default -> Mono.error(new RuntimeException("invalid input"));
        };
    }
}
