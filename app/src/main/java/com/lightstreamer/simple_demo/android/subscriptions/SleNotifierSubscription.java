package com.lightstreamer.simple_demo.android.subscriptions;


import com.lightstreamer.client.Subscription;
import com.lightstreamer.simple_demo.android.LightstreamerClientProxy;
import com.lightstreamer.simple_demo.android.StockListDemoApplication;

public class SleNotifierSubscription {
    final static String DATA_ADAPTER = "sleNotifier";
    final static String[] items = {"android*Notifier*" + StockListDemoApplication.HASHED_CUSTOMER_ID};
    public final static String[] subscriptionFields = {
            "command",
            "key",
            "publishDateTime",
            "notificationKey",
            "notificationType",
            "notificationTypeString",
            "message",
            "description",
            "title",
            "orderSide",
            "instrumentPersianName",
            "volume",
            "price",
            "messageStatus"
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
