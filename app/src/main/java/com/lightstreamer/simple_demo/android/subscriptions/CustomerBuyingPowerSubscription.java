package com.lightstreamer.simple_demo.android.subscriptions;


import com.lightstreamer.client.Subscription;
import com.lightstreamer.simple_demo.android.LightstreamerClientProxy;
import com.lightstreamer.simple_demo.android.StockListDemoApplication;

public  class CustomerBuyingPowerSubscription {
    final static String DATA_ADAPTER = "CustomerBuyingPower";
    final static String[] items = {"android*buyingPower*4B48EFB9C47D7A05523E544F1A061151FBE93E936C0318F8E72FEE9774EABE078CC250EEB4C9AFC42F2E22464C6A4AF162ED89910A81BB72F7CB93E2D6A26DEA"};
    public final static String[] subscriptionFields = {
            "customerId",
            "buyingPower",
            "buyingPowerCurrency",
            "bp_t0",
            "bp_t1",
            "bp_t2",
    };

    private LightstreamerClientProxy lsClient;

    private final Subscription mainSubscription = new Subscription("MERGE",items,subscriptionFields);
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
