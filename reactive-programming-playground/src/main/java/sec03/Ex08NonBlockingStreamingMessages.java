package sec03;

import common.Util;
import sec03.client.ExternalServiceClient;

/*
 * Ensure that the external service is up and running
 *
 * */
public class Ex08NonBlockingStreamingMessages {

    public static void main(String[] args) {
        var client = new ExternalServiceClient();
        client.streamNames()
                .subscribe(Util.subscriber("sub1"));

        client.streamNames()
                .subscribe(Util.subscriber("sub2"));

        // as the non-blocking call is async, the main thread will exit immediately,
        // so we will block it for 5 seconds
        Util.sleepSeconds(5);
    }
}
