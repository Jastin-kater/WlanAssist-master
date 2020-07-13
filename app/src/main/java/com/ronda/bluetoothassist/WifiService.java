package com.ronda.bluetoothassist;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ronda.bluetoothassist.base.AppConst;
import com.ronda.bluetoothassist.service.DataService;

public class WifiService implements Runnable{//这个类实现了Runnable接口，需要重载线程的run（）

    private Socket client ;
    private SocketAddress socketAddress;
    private Handler handler ;
    private Context context;


    private DealFun df ;
    private String st1;
    private Socket socket;
    public static final int STATE_WIFI_CONNECTED = 3;
    public static final int STATE_WIFI_NOTCONNECTED = 4;
    public static final int WIFI_READ=6;
    private DataInputStream in;
//    public void setHandler(Handler handler) {
//        this.handler = handler;
//    }//接收主线程中传过来的Handler
//    public void setContext(Context context){this.context = context;}//接收主线程中传过来的context
    @Override
    public void run() {

        try {

            client = new Socket(InetAddress.getByName(com.ronda.bluetoothassist.IPChangeActivity.getdata_ip()),Integer.parseInt(com.ronda.bluetoothassist.IPChangeActivity.getdata_port()));//连接对应IP地址和端口的设备

            df = new DealFun();
            df.setSocket(client);
            df.setHandler(handler);
            df.setDFContext(context);//上面三句向DealFun对象中传入了Socket、handler、context
            new Thread(df).start();//启动了新线程

            handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, STATE_WIFI_CONNECTED, -1,"wuyong").sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(this.context,"无法正常连接!",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


    }

  public DataInputStream getIn()
{in = df.getDataIn();
       return in;}

    public void setstring(String st)
    {
        st1 = st;
        df.send(st1);

    }

    public Socket getSocket() {
        return client;
    }



}

