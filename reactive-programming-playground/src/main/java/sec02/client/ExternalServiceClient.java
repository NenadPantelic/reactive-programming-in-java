package sec02.client;

import common.AbstractHttpClient;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {

    public Mono<String> getProductName(int productId) {
        // when the method is invoked, we create a Mono which is capable of sending a request.
        // The actual HTTP request is sent, only when it is subscribed.
        return this.httpClient.get()
                .uri(String.format("/demo01/product/%d", productId))
                .responseContent()
                .asString() // streaming response - all product with that productId (only one ofc)
                .next(); // flux to mono
    }
}
