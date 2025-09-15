package common;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

public abstract class AbstractHttpClient {

    private static final String BASE_URL = "http://localhost:7070";
    protected final HttpClient httpClient;

    public AbstractHttpClient() {
        // by default, it creates 1 thread per CPU
        var loopResources = LoopResources.create(
                "np", // prefix
                1, // number of worker threads
                true // daemon
        );
        this.httpClient = HttpClient.create()
                .runOn(loopResources) // event loop
                .baseUrl(BASE_URL);
    }


}
