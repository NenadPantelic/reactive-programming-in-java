package sec03;

import common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Ex12FluxMono {

    public static void main(String[] args) {
        var mono = getUsername(1);

        var flux = mono.flux();
        save(flux);

        var flux2 = Flux.from(mono);
        save(flux2);

        var mono2 = flux.next();
        var mono3 = Mono.from(flux);
    }

    private static Mono<String> getUsername(int userId) {
        return switch (userId) {
            case 1 -> Mono.just("sam");
            case 2 -> Mono.empty(); // null
            default -> Mono.error(new RuntimeException("invalid input"));
        };
    }

    public static void save(Flux<String> flux) {
        flux.subscribe(Util.subscriber());
    }
}


