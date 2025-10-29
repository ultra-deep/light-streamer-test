/*
 * Copyright (c) Lightstreamer Srl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lightstreamer.simple_demo.android;


import android.app.Application;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;

public class StockListDemoApplication extends Application {

    //dev
    private static final String ADDRESS = "https://stream-dev.charisma.digital";
    public static final String HASHED_CUSTOMER_ID = "4B48EFB9C47D7A05523E544F1A061151FBE93E936C0318F8E72FEE9774EABE078CC250EEB4C9AFC42F2E22464C6A4AF162ED89910A81BB72F7CB93E2D6A26DEA";
    public static final String PASSWORD = "766760BEB7ECC7A345462CA0CF64AD4B6E5898294DE4DCA6B518FB026D3B01A3-1";

    //stage
//    private static final String ADDRESS = "https://stream-stage.charisma.digital";
//    public static final String HASHED_CUSTOMER_ID = "D9FBB615A994F426B4A114AB897F77C0FC296FB146C083EB02D5161C6412A8ED16C1C876B21794A9243C785C8A07C45D6CBD9333477005564CEC07FECAF34B30";
//    public static final String PASSWORD = "3A6458BE2DDF5B75648C77309AFE40611C753EE18A6AAF31C792C64209605232-1";

    //production
//    private static final String ADDRESS = "https://stream-trader.charisma.ir";
//    public static final String HASHED_CUSTOMER_ID = "5F615323DF621B03C7C0E21C6FA6DE92F91DACE8300A7676CFD046A34B102DB7116BAA57B369C801A0B172444DDA5447D7E8129518CB4E385875A1459459A463";
//    public static final String PASSWORD = "1ACE557F1D343774DC2BCD71B686811947E82E7BE72BD27F648BB9450E0BA0C6-1";



    public static LightstreamerClientProxy client = null;

    @Override
    public void onCreate() {
        super.onCreate();

//        ConsoleLoggerProvider prov = new ConsoleLoggerProvider(ConsoleLogLevel.WARN);
//        LightstreamerClient.setLoggerProvider(prov);

        client = new ClientProxy(); //expose the instance
    }


    private class ClientProxy implements LightstreamerClientProxy {
        private boolean connectionWish = false;
        private boolean userWantsConnection = true;
        private LightstreamerClient lsClient = new LightstreamerClient(ADDRESS, "Trader");

        public ClientProxy() {
            lsClient.connectionDetails.setUser(HASHED_CUSTOMER_ID);
            lsClient.connectionDetails.setPassword(PASSWORD);
            lsClient.connect();
        }

        @Override
        public boolean start(boolean userCall) {
            synchronized (lsClient) {
                if (!userCall) {
                    if (!userWantsConnection) {
                        return false;
                    }
                } else {
                    userWantsConnection = true;
                }

                connectionWish = true;
//                lsClient.connect();
                return true;
            }
        }

        @Override
        public void stop(boolean userCall) {
            synchronized (lsClient) {
                connectionWish = false;

                if (userCall) {
                    userWantsConnection = false;
                    lsClient.disconnect();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }
                            synchronized (lsClient) {
                                if (!connectionWish) {
                                    lsClient.disconnect();
                                }
                            }
                        }
                    }.start();
                }
            }
        }

        @Override
        public void addSubscription(Subscription sub) {
            lsClient.subscribe(sub);
        }

        @Override
        public void removeSubscription(Subscription sub) {
            lsClient.unsubscribe(sub);
        }


        @Override
        public void addListener(ClientListener listener) {
            lsClient.addListener(listener);
        }

        @Override
        public void removeListener(ClientListener listener) {
            lsClient.removeListener(listener);
        }

        public String getStatus() {
            return lsClient.getStatus();
        }
    }
}
