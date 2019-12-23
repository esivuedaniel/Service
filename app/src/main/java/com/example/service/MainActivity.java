package com.example.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mButton1, mButton2, mButton3;
    TextView mStartedService, mIntentService;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton1=(Button) findViewById(R.id.button);
        mButton2=(Button) findViewById(R.id.button2);
        mButton3=(Button)findViewById(R.id.button3);
        mStartedService=(TextView) findViewById(R.id.textViewStartedService);
        mIntentService=(TextView) findViewById(R.id.textViewIntentService);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, MyStartedService.class);
                intent.putExtra("sleepTime", 10);
                startService(intent);  //instead of using startActivity, we use startService
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MyStartedService.class);
                stopService(intent);

            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResultReceiver myResult=new  MyResultReceiver(null); // this is our handler

                Intent intent=new Intent(MainActivity.this, MyIntentService.class);
                intent.putExtra("sleepTime", 10);
                intent.putExtra("receiver ", myResult);   //Send receiver to MyIntentService.Java
                startService(intent);
            }
        });


    }


    //We want to register our broadcastreceiver dynamically
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(myBroadcastReceiver,intentFilter); //this register our receiver
    }

    //the brodcast receiver is a class itself
    BroadcastReceiver myBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result=intent.getStringExtra("startServiceResult");
            mStartedService.setText(result);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myBroadcastReceiver);  //this unregister our receiver
    }



    //class
    //ResultReceiver is used to send DATA between Activity and Service within the app
    private class MyResultReceiver extends ResultReceiver{
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        //this work on the worker thread(BackGround Thread)
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            Log.i("MyResultReceiver",Thread.currentThread().getName());
            if (resultCode==10 && resultData != null){
              final String result=resultData.getString("resultIntentService");

               handler.post(new Runnable() {  //Handler work on the main thread
                   @Override
                   public void run() {
                       Log.i("Handler", Thread.currentThread().getName());
                       mIntentService.setText(result);
                   }
               });

            }
        }
    }
}
