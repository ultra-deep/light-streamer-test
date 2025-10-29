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
package com.lightstreamer.simple_demo.android.subscriptions;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.client.SubscriptionListener;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;


public class MainSubscriptionListener implements SubscriptionListener {

    private static final String TAG = "LLLSSS";

    private Context context = new Context();


    public class Context {
        public Handler handler;
        public ListView listView;
    }


    @Override
    public void onClearSnapshot(String arg0, int arg1) {
        Log.d(TAG,"clear snapshot call"); //the default stocklist demo adapter does not send this event
    }

    @Override
    public void onCommandSecondLevelItemLostUpdates(int arg0, String arg1) {
        Log.wtf(TAG,"Not expecting 2nd level events");
    }

    @Override
    public void onCommandSecondLevelSubscriptionError(int arg0, String arg1,
                                                      String arg2) {
        Log.wtf(TAG,"Not expecting 2nd level events");
    }

    @Override
    public void onEndOfSnapshot(String itemName, int arg1) {
        Log.v(TAG,"Snapshot end for " + itemName);
    }

    @Override
    public void onItemLostUpdates(String arg0, int arg1, int arg2) {
        Log.wtf(TAG,"Not expecting lost updates");
    }

    @Override
    public void onItemUpdate(ItemUpdate update) {
        Log.v(TAG,"Update for " + update.getItemName());
    }

    @Override
    public void onListenEnd(Subscription subscription) {
        Log.d(TAG,"Stop listening");
    }

    @Override
    public void onListenStart(Subscription subscription) {
        Log.d(TAG,"Start listening");
    }

    @Override
    public void onSubscription() {
        Log.i(TAG,"************************* *** *** *  Subscribed");
    }

    @Override
    public void onSubscriptionError(int code, String message) {
        Log.e(TAG,"Subscription error " + code + ": " + message);
    }

    @Override
    public void onUnsubscription() {
        Log.v(TAG,"onUnsubscription");
    }

    @Override
    public void onRealMaxFrequency(String frequency) {

    }

}