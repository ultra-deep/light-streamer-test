package com.lightstreamer.simple_demo.android.subscriptions;


import com.lightstreamer.client.Subscription;
import com.lightstreamer.simple_demo.android.LightstreamerClientProxy;
import com.lightstreamer.simple_demo.android.StockListDemoApplication;

public class PortfolioSubscription {
    final static String DATA_ADAPTER = "PortfolioPusher";
    final static String[] items = {"portfolioItem*" + StockListDemoApplication.HASHED_CUSTOMER_ID};
    final static String item = "portfolioItem*" + StockListDemoApplication.HASHED_CUSTOMER_ID;

    public final static String[] subscriptionFields = {"key","command" , "isinCode", "quantity","valuePerLastTrade","valuePerClosingPrice","weightPerLastTrade","weightPerClosingPrice","costOfPerClosingPrice","costOfPerLastTrade","referencePricePerClosingPrice","referencePricePerLastTrade","referencePriceChangePercentagePerClosingPrice","referencePriceChangePercentagePerLastTrade","incomePerClosingPrice","incomePerLastTrade","incomePercentagePerClosingPrice","incomePercentagePerLastTrade","todayIncomePerClosingPrice","todayIncomePerLastTrade","todayIncomePercentagePerClosingPrice","todayIncomePercentagePerLastTrade","creationDateTime"};

    private LightstreamerClientProxy lsClient;

    private final Subscription mainSubscription = new Subscription("COMMAND",item,subscriptionFields);

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
}
