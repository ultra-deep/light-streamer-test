package com.lightstreamer.simple_demo.android.subscriptions;


import com.lightstreamer.client.Subscription;
import com.lightstreamer.simple_demo.android.LightstreamerClientProxy;
import com.lightstreamer.simple_demo.android.StockListDemoApplication;

public class OrderManagementTradedSubscription {
    final static String DATA_ADAPTER = "OrderManagementTraded";
    final static String[] items = {"android*tradeOrder*" + StockListDemoApplication.HASHED_CUSTOMER_ID};
    public final static String[] subscriptionFields = {
            "key",
            "command",
            "instrumentIsinCode",
            "side",
            "tradesDateTime",
            "tradesShamsiDate",
            "tradedPriceAverageAmount",
            "tradedPriceAverageCurrency",
            "tradedVolume",
    };

    private LightstreamerClientProxy lsClient;

    private final Subscription mainSubscription = new Subscription("COMMAND",items,subscriptionFields);
    private final MainSubscriptionListener mainSubscriptionListener = new MainSubscriptionListener();

    public void subScribe() {
        lsClient = StockListDemoApplication.client;
        mainSubscription.setDataAdapter(DATA_ADAPTER);
        mainSubscription.setRequestedMaxFrequency("1");
        mainSubscription.setRequestedSnapshot("yes");
        mainSubscription.addListener(mainSubscriptionListener);
        lsClient.addSubscription(mainSubscription);
    }
    public void unSubscribe() {
        lsClient.removeSubscription(mainSubscription);
    }
}
