package com.ronda.bluetoothassist.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ronda.bluetoothassist.WifiService;
import com.ronda.bluetoothassist.base.AppConst;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class DealFun implements Runnable {
  private Socket socketFun;
  private Handler handler;
  private DataInputStream in;
  private Socket socket2 = new Socket();
  private byte[] receive= new byte[6];
  private char flag =0;
  private Timer timer=null;
  private TimerTask timerTask=null;
  private int len = 0;
    private Context context;

  @Override
    public void run() {
      byte c=0,d=0,e=0,f=0;
      int g=0;
    try {

        in = new DataInputStream(socketFun.getInputStream());
        while(true) {

            if((len = in.read(receive))!= -1) {
                stopTimer();
                //Log.e("REND",new String(receive));
                c = (byte) (receive[2] - 48);
                d = (byte) (receive[3] - 48);
                e = (byte) (receive[4] - 48);
                f = (byte) (receive[5] - 48);
                g = f + e * 10 + d * 100 + c * 1000;
                receive[5] = (byte) (g & 0xff);
                receive[4] = (byte) (g >> 8 & 0xff);
                receive[3] = (byte) (g >> 16 & 0xff);
                receive[2] = (byte) (g >> 24 & 0xff);
                Log.e("REND", new String(receive));
                //in.close();
                handler.obtainMessage(AppConst.MESSAGE_READ, -1, -1, receive).sendToTarget();
                startTimer();
            }

        }
    } catch (IOException e1) {
        e1.printStackTrace();
    }


    }

    public DataInputStream getDataIn()
    {
        return in ;


    }
    public void setSocket(Socket socket)
    {
        socketFun = socket;


    }

    public  void setHandler(Handler handler1)
    {
      handler = handler1;

    }
    public void setContext(Context context){this.context = context;}
  public void timer() {
      if(timer==null)
      timer = new Timer();
      if(timerTask==null) {
          timerTask = new TimerTask() {
              @Override
              public void run() {
                      try {
                          in.close();
                            Looper.prepare();//必须要有消息队列才能
                          Toast.makeText(context,"连接已断开",Toast.LENGTH_SHORT).show();
                          handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, WifiService.STATE_WIFI_NOTCONNECTED, -1,"wuyong").sendToTarget();

                          Looper.loop();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
              }

          };
      }
      timer.schedule(timerTask,120000);

  }
public void startTimer()
{
    timer();


}
public void stopTimer()
{
if(timer!=null) {
    timer.cancel();
    timer = null;
}
if (timerTask!=null) {
    timerTask.cancel();
    timerTask = null;
}

}



}
