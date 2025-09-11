package sec02;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

// if we want to emit after some method invocation
public class Ex07MonoFromRunnable {

    private static final Logger log = LoggerFactory.getLogger(Ex07MonoFromRunnable.class);

    public static void main(String[] args) {
        getProductName(1).subscribe(Util.subscriber());
        getProductName(25).subscribe(Util.subscriber());
    }

    private static Mono<String> getProductName(int productId) {
        if (productId == 1) {
            return Mono.fromSupplier(() -> Util.faker().commerce().productName());
        }

        // we will first invoke notifyBusiness, then return an empty Mono
        return Mono.fromRunnable(() -> notifyBusiness(productId));
    }

    private static void notifyBusiness(int productId) {
        log.info("Notifying business about the available product: {}", productId);
    }
}
