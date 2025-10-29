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


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.simple_demo.android.subscriptions.MainSubscriptionListener;
import com.lightstreamer.simple_demo.android.subscriptions.PortfolioSubscription;
import com.lightstreamer.simple_demo.android.subscriptions.SleNotifierSubscription;


public class StockListActivity extends AppCompatActivity {


    private static final String TAG = "StockListDemo";

    private Handler handler;

    private ClientListener currentListener = new LSClientListener();
//    private boolean pnEnabled = false;
    private boolean isConnectionExpected = false;
    

    LightstreamerClientProxy client;

    private final SleNotifierSubscription sleNotifierSubscription = new SleNotifierSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = StockListDemoApplication.client;

        this.handler = new Handler();


        setContentView(R.layout.stocks);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AgreementPusherSubscription().subScribe();
//                new PortfolioSubscription().subScribe();
                sleNotifierSubscription.subScribe();
//                sleNotifierSubscription.subScribe();
            }
        });
//        new AgreementPusherSubscription().subScribe();
//        new CustomerBuyingPowerSubscription().subScribe();
//        new BrokerageNotificationSubscription().subScribe();
//        new CustomerBuyingPowerSubscription().subScribe();
//        new NotificationCenterSubscription().subScribe();

        MainSubscriptionListener mainSubscriptionListener = new MainSubscriptionListener() {
            @Override
            public void onItemUpdate(ItemUpdate update) {
                super.onItemUpdate(update);
                StringBuilder fieldsvalues = new StringBuilder();
                for (String k : update.getFields().keySet()) {
                    fieldsvalues.append(k);
                    fieldsvalues.append(" = ");
                    fieldsvalues.append(update.getFields().get(k));
                    fieldsvalues.append("\r\n");
                }
                Log.i(TAG, "ON ITEM UPDATE " + fieldsvalues.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv =  (TextView) findViewById(R.id.textView);
                        tv.setText(fieldsvalues.toString());
                    }
                });
            }
        };

        new PortfolioSubscription().subScribe(mainSubscriptionListener);
//        sleNotifierSubscription.subScribe();
    }


    @Override
    public void onPause() {
        super.onPause();
        client.removeListener(currentListener);
        client.stop(false);
    }
    
    @Override 
    public void onResume() {
        super.onResume();

        client.addListener(this.currentListener);
        isConnectionExpected = client.start(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.v(TAG,"Switch button: " + isConnectionExpected);
        menu.findItem(R.id.start).setVisible(!isConnectionExpected);
        menu.findItem(R.id.stop).setVisible(isConnectionExpected);
        
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.stop) {
            Log.i(TAG,"Stop");
            supportInvalidateOptionsMenu();
            client.stop(true);
            isConnectionExpected = false;
            return true;
        } else if (itemId == R.id.start) {
            Log.i(TAG,"Start");
            supportInvalidateOptionsMenu();
            client.start(true);
            isConnectionExpected = true;
            return true;
        } else if (itemId == R.id.about) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sleNotifierSubscription.unSubscribe();
    }

    public class LSClientListener implements ClientListener {


        @Override
        public void onPropertyChange(String arg0) {
        }

        @Override
        public void onListenEnd(LightstreamerClient client) {

        }

        @Override
        public void onListenStart(LightstreamerClient client) {

        }

        @Override
        public void onServerError(int code, String message) {
            Log.e(TAG,"Error "+code+": "+message);
        }

        @Override
        public void onStatusChange(String status) {
            Log.e("LLLSSS", "onStatusChange: " + status );
            handler.post(new StatusChange(status));
        }
        
    }
    
    
    private class StatusChange implements Runnable {

        private String status;

        public StatusChange(String status) {
            this.status = status;
        }
        
        private void applyStatus(int textId , int color , String status) {
//            ImageView statusIcon = (ImageView) findViewById(R.id.status_image);
//            TextView textStatus = (TextView) findViewById(R.id.text_status);
            TextView textStatus = (TextView) findViewById(R.id.textView);

            textStatus.setBackgroundColor(color);
            textStatus.setText(status);

        }

        @Override
        public void run() {
            
            Log.i(TAG, "Client status: " + status);
            switch(status) {
            
                case "CONNECTING":
                    applyStatus(R.drawable.status_disconnected, Color.YELLOW , status);
                    break;

                case "CONNECTED:STREAM-SENSING":
                    applyStatus(R.drawable.status_connected_polling,Color.GREEN , status);
                    break;

                case "DISCONNECTED":
                    applyStatus(R.drawable.status_disconnected,Color.RED , status);
                    break;
                case "DISCONNECTED:WILL-RETRY":
                    applyStatus(R.drawable.status_disconnected,Color.RED , status);
                    break;
                
                case "CONNECTED:HTTP-STREAMING":
                    applyStatus(R.drawable.status_connected_streaming,Color.GREEN , status);
                    break;
                case "CONNECTED:WS-STREAMING":
                    applyStatus(R.drawable.status_connected_streaming,Color.GREEN , status);
                    break;
                
                case "CONNECTED:HTTP-POLLING":
                    applyStatus(R.drawable.status_connected_polling,Color.CYAN , status);
                    break;
                case "CONNECTED:WS-POLLING":
                    applyStatus(R.drawable.status_connected_polling,Color.CYAN , status);
                    break;
                    
                case "DISCONNECTED:TRYING-RECOVERY":
                    applyStatus(R.drawable.status_stalled,Color.RED , status);
                    break;
                case "STALLED":
                    applyStatus(R.drawable.status_stalled,Color.argb(255,128,255,0) , status);
                    break;
                
                default: 
                    Log.wtf(TAG, "Received unexpected connection status: " + status);
                    return;
                
            }
        }
        
    }
    
//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        this.mDetector.onTouchEvent(event);
//        // Be sure to call the superclass implementation
//        return super.onTouchEvent(event);
//    }
    
//    public static class AboutDialog extends DialogFragment {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            LayoutInflater inflater = getActivity().getLayoutInflater();
//            builder.setView(inflater.inflate(R.layout.dialog_about, null)).setPositiveButton("OK", null);
//            return builder.create();
//        }
//
//    }

 //we simply use this class to listen for double taps in which case we reveal/hide
    //a textual version of the connection status
    private class GestureControls extends 
        GestureDetector.SimpleOnGestureListener implements  
        GestureDetector.OnDoubleTapListener  {
            
        @Override
        public boolean onDown(MotionEvent event) { 
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            
            //toggleContainer.setVisibility(show ? View.VISIBLE : View.GONE);
            TextView textStatus = (TextView) findViewById(R.id.text_status);
            textStatus.setVisibility(textStatus.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            
            return true;
        }
    
      
    }
    
}
