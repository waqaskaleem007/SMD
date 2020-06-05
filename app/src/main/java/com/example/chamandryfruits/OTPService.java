package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.Nullable;


public class OTPService extends Service {

    Messenger messenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("HandlerLeak")
    public class IncomingHandler extends Handler {
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    processMessage(msg,msg.replyTo);
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void processMessage(final Message msg, Messenger replyTo){

        Thread thread = new Thread(new Runnable() {
            String line;
            @Override
            public void run() {
               Intent intent = new Intent("CLEAR_CART");
               intent.putExtra("clear","clear");
               sendBroadcast(intent);
            }
        });
        thread.start();
    }

}
