package com.example.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;


public class MyIntentService extends IntentService {

    private static final String TAG=MyIntentService.class.getSimpleName();

    public MyIntentService() {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate, ThreadName "+Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Log.i(TAG, "onHandleIntent, ThreadName"+Thread.currentThread().getName());
        int b=intent.getIntExtra("sleepTime", 2);
        ResultReceiver resultReceiver= intent.getParcelableExtra("receiver");

        int a;
        for ( a=1; a<=b; a++)
        {
            Log.i(TAG, "Counter is now" +a);
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e)
            {e.printStackTrace();}
        }

        //Bundle is used for passing data between various activity of android
        //Bundle help to send data back to the Activity
        Bundle bundle=new Bundle();
        bundle.putString("resultIntentService", "Counter stop at "+a+" Seconds");
        resultReceiver.send(10, bundle);  //the result is sent back to MainActivity

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy, ThreadName "+Thread.currentThread().getName());
    }
}
