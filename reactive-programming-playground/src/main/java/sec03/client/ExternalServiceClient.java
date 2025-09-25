package sec03.client;

import common.AbstractHttpClient;
import reactor.core.publisher.Flux;

public class ExternalServiceClient extends AbstractHttpClient {

    public Flux<String> streamNames() {
        // when the method is invoked, we create a Mono which is capable of sending a request.
        // The actual HTTP request is sent, only when it is subscribed.
        return this.httpClient.get()
                .uri("/demo02/name/stream")
                .responseContent()
                .asString(); // streaming response - all product with that productId (only one ofc)
    }

    public Flux<Integer> getPriceChanges() {
        return this.httpClient.get()
                .uri("/demo02/stock/stream")
                .responseContent()
                .asString()
                .map(Integer::parseInt);
    }
}
