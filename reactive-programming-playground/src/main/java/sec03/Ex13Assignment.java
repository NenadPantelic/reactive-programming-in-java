package sec03;

import common.Util;
import sec03.assignment.StockPriceObserver;
import sec03.client.ExternalServiceClient;

public class Ex13Assignment {

    public static void main(String[] args) {
        var client = new ExternalServiceClient();
        client.getPriceChanges().subscribe(new StockPriceObserver());
        Util.sleepSeconds(20); // time of emitting
    }
}
