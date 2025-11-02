package com.lightstreamer.simple_demo.android.subscriptions;



import com.lightstreamer.client.Subscription;
import com.lightstreamer.simple_demo.android.ClientProxy;
import com.lightstreamer.simple_demo.android.LightstreamerClientProxy;
import com.lightstreamer.simple_demo.android.StockListDemoApplication;

public class PortfolioSubscription {
    final static String DATA_ADAPTER = "PortfolioPusher";
    final static String[] items = {"portfolioItem*" + ClientProxy.HASHED_CUSTOMER_ID};
    final static String item = "dist_portfolioItem_" + ClientProxy.HASHED_CUSTOMER_ID;

    public final static String[] subscriptionFields = {"meta", "pushDateTime"};

    private LightstreamerClientProxy lsClient;

    private final Subscription mainSubscription = new Subscription("DISTINCT",item,subscriptionFields);

    public void subScribe(MainSubscriptionListener mainSubscriptionListener) {
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

    public boolean isSubscribed() {
        return mainSubscription.isSubscribed();
    }
}
