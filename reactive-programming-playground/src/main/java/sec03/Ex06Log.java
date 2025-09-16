package sec03;

import common.Util;
import reactor.core.publisher.Flux;

public class Ex06Log {

    public static void main(String[] args) {
        Flux.range(1, 5)
                .log("logger") // implements both publisher and subscriber
                .subscribe(Util.subscriber());
    }

}
