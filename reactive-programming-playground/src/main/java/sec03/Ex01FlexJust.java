package sec03;

import common.Util;
import reactor.core.publisher.Flux;

public class Ex01FlexJust {

    public static void main(String[] args) {
        Flux.just(1).subscribe(Util.subscriber());
        Flux.just(1, 2, 3, "Sam", "Mike").subscribe(Util.subscriber()); // all values will be published separately
    }
}
