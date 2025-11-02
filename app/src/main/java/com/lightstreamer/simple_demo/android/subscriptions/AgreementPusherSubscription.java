package com.lightstreamer.simple_demo.android.subscriptions;


import com.lightstreamer.client.Subscription;
import com.lightstreamer.simple_demo.android.ClientProxy;
import com.lightstreamer.simple_demo.android.LightstreamerClientProxy;
import com.lightstreamer.simple_demo.android.StockListDemoApplication;

public class AgreementPusherSubscription {
    final static String DATA_ADAPTER = "AgreementPusher";
    final static String[] items = {"agreementPusher*" + ClientProxy.HASHED_CUSTOMER_ID};
    public final static String[] subscriptionFields = {"rcCodes"};

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
