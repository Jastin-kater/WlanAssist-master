package com.ronda.bluetoothassist.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ronda.bluetoothassist.DealFun;

public class DataService extends Service {
    private static DealFun dealFun;
    public DataService() {

    }

    public static void setDF(DealFun df)
    {
        dealFun = df;

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("oncreat");
         new Thread(dealFun).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
//        DealFun df = new DealFun();
//        new Thread(df).start();


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }
}
