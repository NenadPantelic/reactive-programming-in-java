package sec03;

import common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class Ex04FluxFromStream {

    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4);
        var stream = list.stream();

        var flux = Flux.fromStream(stream);
        flux.subscribe(Util.subscriber("sub1"));
        // will receive an error (caused by stream)
        // flux.subscribe(Util.subscriber("sub2"));

        var streamTwo = list.stream();
        streamTwo.forEach(System.out::println);
        // stream.forEach(System.out::println); // this one will result in an error. The complete stream was consumed in
        // the previous line, so stream can be used only once.
        // e.g.
        // streamTwo.forEach(System.out::println);
        // stream.forEach(System.out::println); // this one will result in an error. The complete stream was consumed in
        // the previous line, so stream can be used only once.

        // for multiple subscriber, we can create a custom supplier that would create stream for each and every subscriber
        flux = Flux.fromStream(list::stream);
        flux.subscribe(Util.subscriber("sub11"));
        flux.subscribe(Util.subscriber("sub12"));
    }
}
