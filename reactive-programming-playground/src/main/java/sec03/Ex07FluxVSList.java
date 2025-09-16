package sec03;

import common.Util;
import sec01.subscriber.SubscriberImpl;
import sec03.helper.NameGenerator;

public class Ex07FluxVSList {

    public static void main(String[] args) {
//        var list = NameGenerator.getNamesList(10); // blocked until it generates all values
//        System.out.println(list);

        var subscriber = new SubscriberImpl();
        NameGenerator.getNamesFlux(10).subscribe(subscriber); // generates them one by one
        subscriber.getSubscription().request(3); // only 3 names will be received
        subscriber.getSubscription().cancel(); // cancel after 3 names as we are satisfied with the result
    }
}

