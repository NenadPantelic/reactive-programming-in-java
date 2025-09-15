package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sec02.client.ExternalServiceClient;

/*
 * External service has to be up & running
 * */
public class Ex11NonBlockingIO {

    private static final Logger log = LoggerFactory.getLogger(Ex11NonBlockingIO.class);

    public static void main(String[] args) {
        var client = new ExternalServiceClient();
        log.info("Starting...");
        client.getProductName(1)
                .subscribe(Util.subscriber());

        for (int i = 0; i <= 50; i++) {
            // it's like sending all at once (not one by one)
            // so, the order of receiving could be different,
            // but it will be almost like receiving all of them at once
            // only one thread serves all these requests
            client.getProductName(i)
                    .subscribe(Util.subscriber());
        }
        // non-blocking code, it sends the request and exits
        // to get and see the response, we will have to sleep or block the main thread for 2s
        Util.sleepSeconds(2);

        log.info("Starting the blocking code...");
        // DO NOT DO THIS
        for (int i = 0; i <= 50; i++) {
            System.out.println(client.getProductName(i).block()); // makes the code blocking the current thread, like sending one by one
        }
    }
}
