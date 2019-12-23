package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyStartedService extends Service {

    private static final String TAG= MyStartedService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate Thread name"+Thread.currentThread().getName());
    }


    //THIS IS MY MAIN THREAD OR UI THREAD
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand Thred name "+Thread.currentThread().getName());

        int sleepTime= intent.getIntExtra("sleepTime", 1);

        new MyAsyncTask().execute(sleepTime);  //this execute the CLASS OF ASYNC_TASK below
        return START_STICKY;
    }


    //@androidx.annotation.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind Thread Name"+ Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy Thread name"+Thread.currentThread().getName());
    }


    
    // THIS IS MY ASYNC TASK
    class MyAsyncTask extends AsyncTask <Integer, String, String>{

        private final String TAG=MyAsyncTask.class.getSimpleName();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"onPreExcecute, Thread Name"+Thread.currentThread().getName());
        }

        //This is the only one that does the background thread,the rest work in the main thread
        //Here we perform our LONG RUNNING TASK, e.g downloading file, playing music, load image
        @Override
        protected String doInBackground(Integer... params) {
            Log.i(TAG, "doInBackGround, Thread name"+Thread.currentThread().getName());

         int sleepTime = params[0];

         int a;
         //Performing long running operation
         for ( a=1; a<=sleepTime; a++ ){
             publishProgress("Counter is now"+a);  //This shows the progress update

             try {
                 Thread.sleep(1000);
             }catch (InterruptedException e){
                 e.printStackTrace();
             }
         }

            return ("Counter stop at "+a+" seconds");
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Counter value "+values[0]+"onProgressUpdate, Thread Name"+Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            stopSelf();  // This destroy the service from within the service class itself
            Log.i(TAG, "onPostExcecute, Thread Name"+Thread.currentThread().getName());

            Intent intent=new Intent("action.service.to.activity");
            intent.putExtra("startServiceResult", str);
            sendBroadcast(intent);
        }
    }


}
