package com.ronda.bluetoothassist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.widget.Toast;

import com.ronda.bluetoothassist.RecycleL_expended_text.Mach_info_activity;
import com.ronda.bluetoothassist.base.AppConst;
import com.ronda.bluetoothassist.base.MyApplication;
import com.ronda.bluetoothassist.graph_container.JumpActivity;
import com.ronda.bluetoothassist.utils.DInterface;
import com.ronda.bluetoothassist.utils.Modbus;
import com.ronda.bluetoothassist.utils.RToast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import database.MydatabaseHelper;

import static android.app.Notification.DEFAULT_ALL;
import static com.ronda.bluetoothassist.BluetoothChatService.STATE_CONNECTED;
import static com.ronda.bluetoothassist.BluetoothChatService.STATE_NONE;


/**
*修改日期：2019.3.4
*改动：获取数据指令
*
*/

public class DealFun implements Runnable {
    public static final int DEALFUN_FIRST = 1;
    public static final int DEALFUN_SECOND = 2;
    public static final int DEALFUN_THIRD = 3;
    public static final int HANQIANG1 = 1;
    public static final int HANQIANG2 = 2;
    private static boolean all_data = false;
    private static int status_hanqiang;
    private int equipment;
    private Socket socketFun;
    private Handler handler;
    private Handler Jhandler;
    private Handler mach_handler;
    private Socket client ;
    private DataInputStream in;
    private DataOutputStream out;
    private byte[] receive= new byte[200];
    private Timer timer=null;
    private TimerTask timerTask=null;
    private int len = 0;
    private Context context;
    private String limit_strings;
    byte m = (byte) 0x8B;
    int nu = m&0xff;
    //焊枪2数据指令
    //这里的地址0x004E和0x0060是78和96（焊枪2的时间电压电流）


    private byte[] com_V_I_SP_ENTE_HU_SP_TE = {0x00,0x19,0x00,0x00,0x00,0x06,0x01,0x04,0x00, (byte) 0x00,(byte)0x00,(byte)0x0E};//焊接电压、焊接电流、送丝速度、环境温度、环境湿度、焊接速度、焊层温度
    private byte[] com_mach_info30015 = {0x00,0x19,0x00,0x00,0x00,0x06,0x01,0x04,0x00, (byte) 0x0e,0x00,(byte)0x37};
    private byte[] com_mach_info30070 = {0x00,0x19,0x00,0x00,0x00,0x06,0x01,0x04,0x00, (byte) 0x45,0x00,(byte)0x46};
    private byte[] com_mach_info30140 = {0x00,0x19,0x00,0x00,0x00,0x06,0x01,0x04,0x00, (byte) 0x8B,0x00,(byte)0x37};

    private byte[] bt ;
    private boolean stopThread = false;
    private String[] mach_information30015 = new String[5];
    private String[] mach_information30070 = new String[3];
    private String[] mach_information30140 = new String[4];
    private String[] mach_information = new String[12];
//data_set为数据集，从0到14为时间秒、微妙、焊接速度、焊接角度、焊接方向、1送丝速度、1电压、1电流、2送丝速度、2电压、2电流、1摆宽、1左边停、1右边停
//2摆宽、2左边停、2右边停


    private LinkedList<Integer[]> ll = new LinkedList();
    public void setDFHandler(Handler handler) {
        this.handler = handler;
    }//接收主线程中传过来的Handler
    public void setDFContext(Context context){this.context = context;}//接收主线程中传过来的context
    private MydatabaseHelper mydatabaseHelper;
    public void setMach_handler(Handler mach_handler) {
        this.mach_handler = mach_handler;
    }
    public void stopThread(){  stopThread = true;}
    public void startThread(){  stopThread = false;}

    @Override
    public void run() {
            try {
                Log.i("tag","run1");
                //client = new Socket(InetAddress.getByName(com.ronda.bluetoothassist.IPChangeActivity.getdata_ip()),Integer.parseInt(com.ronda.bluetoothassist.IPChangeActivity.getdata_port()));//连接对应IP地址和端口的设备
                client = new Socket();
                SocketAddress socAddress = new InetSocketAddress(com.ronda.bluetoothassist.IPChangeActivity.getdata_ip(),Integer.parseInt(com.ronda.bluetoothassist.IPChangeActivity.getdata_port()));
                Log.i("tag","client"+com.ronda.bluetoothassist.IPChangeActivity.getdata_ip()+Integer.parseInt(com.ronda.bluetoothassist.IPChangeActivity.getdata_port()));
                client.connect(socAddress, 5000);
                Log.i("tag","run");
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
                handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, STATE_CONNECTED, -1,"wuyong").sendToTarget();
                MainActivity.connect_state = true;
                mydatabaseHelper = new MydatabaseHelper(MyApplication.getContext(),"machDataBase.db",null,1);
                SQLiteDatabase sqLiteDatabase = mydatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues valuesA = new ContentValues();


    while (!stopThread) {
        Integer[] regInt;
        Integer[] regIteger = new Integer[13];

//设备数据
        sendcommand(com_V_I_SP_ENTE_HU_SP_TE);
        if ((len = in.read(receive)) != -1)//从输入流读取数据
            bt = new byte[len];
        bt = receive;

        regInt = DecodeT(bt);
        //将数据加入节点
//设备信息
        sendcommand(com_mach_info30015);
        if ((len = in.read(receive)) != -1)//从输入流读取数据
            bt = new byte[len];
        bt = receive;
        mach_information30015 = infoCharToString(bt, 5, 30, 30, 10, 20, 20);


        sendcommand(com_mach_info30070);
        if ((len = in.read(receive)) != -1)//从输入流读取数据
            bt = new byte[len];
        bt = receive;
        mach_information30070 = infoCharToString(bt, 3, 20, 60, 60);


        sendcommand(com_mach_info30140);
        if ((len = in.read(receive)) != -1)//从输入流读取数据
            bt = new byte[len];
        bt = receive;
        mach_information30140 = infoCharToString(bt, 4, 10, 10, 60, 30);


        for (int i = 0; i < 5; i++)
            mach_information[i] = mach_information30015[i];

        for (int i = 0; i < 3; i++)
            mach_information[i + 5] = mach_information30070[i];

        for (int i = 0; i < 4; i++)
            mach_information[i + 8] = mach_information30140[i];

        if (Mach_info_activity.activity_Start)
            mach_handler.obtainMessage(Mach_info_activity.MACH_INFORMATION, 12, 0, mach_information).sendToTarget();
//焊缝信息发送
           handler.obtainMessage(WifiService.WIFI_READ, DEALFUN_THIRD, 1, mach_information[7]).sendToTarget();
//加入时间后发送
        for (int i = 0; i < 7; i++)
            regIteger[i] = regInt[i];
       // String time = "19-10-24 20:15:59";
        String time = mach_information[11].trim();
        String time1[] = time.split("\\s", 2);
        String year_month_day[] = time1[0].split("-", 3);
        String hour_minute_second[] = time1[1].split(":", 3);
        String new_time = (Integer.parseInt(year_month_day[0]) + 2000) + "-" + year_month_day[1] + "-" + year_month_day[2] + " " + hour_minute_second[0] + ":" + hour_minute_second[1] + ":" + hour_minute_second[2];

//下面分别是年月日时分秒
        regIteger[7] = new Integer(Integer.parseInt(year_month_day[0]));
        regIteger[8] = new Integer(Integer.parseInt(year_month_day[1]));
        regIteger[9] = new Integer(Integer.parseInt(year_month_day[2]));
        regIteger[10] = new Integer(Integer.parseInt(hour_minute_second[0]));
        regIteger[11] = new Integer(Integer.parseInt(hour_minute_second[1]));
        regIteger[12] = new Integer(Integer.parseInt(hour_minute_second[2]));

        if (ll.size() < 180) {
            ll.addLast(regIteger);
        } else {
            ll.addLast(regIteger);
            ll.removeFirst();
        }
        handler.obtainMessage(WifiService.WIFI_READ, DEALFUN_FIRST, ll.size(), ll).sendToTarget();

        //放入数据库
//                    values.put("weld_vol",Float.intBitsToFloat(regIteger[0]));
//                    values.put("weld_cur",Float.intBitsToFloat(regIteger[1]));
//                    values.put("speed_songsi",Float.intBitsToFloat(regIteger[2]));
//                    values.put("envir_temp",Float.intBitsToFloat(regIteger[3]));
//                    values.put("envir_humi",Float.intBitsToFloat(regIteger[4]));
//                    values.put("speed_weld",Float.intBitsToFloat(regIteger[5]));
//                    values.put("welding_layer_temp",Float.intBitsToFloat(regIteger[6]));
////                    //放入数据库
//                    values.put("info_mach",mach_information[0]);
//                    values.put("ID_proj",mach_information[1]);
//                    values.put("num_unit",mach_information[2]);
//                    values.put("procedure_tech",mach_information[3]);
//                    values.put("num_mach",mach_information[4]);
//                    values.put("tech_weld",mach_information[5]);
//                    values.put("num_person",mach_information[6]);
//                    values.put("num_weld_point",mach_information[7]);
//                    values.put("place_weld",mach_information[8]);
//                    values.put("name_welding_layer",mach_information[9]);
//                    values.put("alert_working",mach_information[10]);
//                    values.put("weld_type",MainActivity.getData_limit());
////                    //时间需要转化为秒
//                    values.put("time", DateDeal.stringToLong(new_time,"yyyy-MM-dd HH:mm:ss"));
//                    sqLiteDatabase.insert("mach",null,values);


        if (MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN) {
            if (limit_detector(34, 24, Float.intBitsToFloat(regInt[0]))) {
                limit_strings = "电压超限";
                valuesA.put("weld_vol", Float.intBitsToFloat(regInt[0]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GENHAN");
                sqLiteDatabase.insert("voltage1", null, valuesA);
                valuesA.clear();
                Log.i("limit", "电压超限");
            }
            if (limit_detector(100, 60, Float.intBitsToFloat(regInt[1]))) {
                limit_strings = limit_strings + "  " + "电流超限";
                valuesA.put("weld_cur", Float.intBitsToFloat(regInt[1]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GENHAN");
                sqLiteDatabase.insert("current", null, valuesA);
                valuesA.clear();
                Log.i("limit", "电流超限");
            }
            if (limit_detector(12, 6, Float.intBitsToFloat(regInt[5]))) {
                limit_strings = limit_strings + "  " + "焊接速度超限";
                ;
                valuesA.put("speed_hanjie", Float.intBitsToFloat(regInt[5]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GENHAN");
                sqLiteDatabase.insert("hanjie", null, valuesA);
                valuesA.clear();
                Log.i("limit", "焊接速度超限");
            }

            notification_pop(limit_strings);

        } else if (MainActivity.getData_limit() == MainActivity.LIMIT_REHAN) {
            if (limit_detector(22, 18, Float.intBitsToFloat(regInt[0]))) {
                limit_strings = "电压超限";
                valuesA.put("weld_vol", Float.intBitsToFloat(regInt[0]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_REHAN");
                sqLiteDatabase.insert("voltage1", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(240, 170, Float.intBitsToFloat(regInt[1]))) {
                limit_strings = limit_strings + "  " + "电流超限";

                valuesA.put("weld_cur", Float.intBitsToFloat(regInt[1]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_REHAN");
                sqLiteDatabase.insert("current", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(110, 80, Float.intBitsToFloat(regInt[2]))) {
                limit_strings = limit_strings + "  " + "送丝速度超限";
                valuesA.put("speed_songsi", Float.intBitsToFloat(regInt[2]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_REHAN");
                sqLiteDatabase.insert("songsi", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(22, 16, Float.intBitsToFloat(regInt[5]))) {
                limit_strings = limit_strings + "  " + "焊接速度超限";
                valuesA.put("speed_hanjie", Float.intBitsToFloat(regInt[5]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_REHAN");
                sqLiteDatabase.insert("hanjie", null, valuesA);
                valuesA.clear();
            }

            notification_pop(limit_strings);

        } else if (MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG) {
            if (limit_detector(22, 18, Float.intBitsToFloat(regInt[0]))) {
                limit_strings = "电压超限";
                valuesA.put("weld_vol", Float.intBitsToFloat(regInt[0]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_TIANCHONG");
                sqLiteDatabase.insert("voltage1", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(270, 180, Float.intBitsToFloat(regInt[1]))) {
                limit_strings = limit_strings + "  " + "电流超限";
                valuesA.put("weld_cur", Float.intBitsToFloat(regInt[1]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_TIANCHONG");
                sqLiteDatabase.insert("current", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(110, 90, Float.intBitsToFloat(regInt[2]))) {
                limit_strings = limit_strings + "  " + "送丝速度超限";
                valuesA.put("speed_songsi", Float.intBitsToFloat(regInt[2]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_TIANCHONG");
                sqLiteDatabase.insert("songsi", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(22, 16, Float.intBitsToFloat(regInt[5]))) {
                limit_strings = limit_strings + "  " + "焊接速度超限";

                valuesA.put("speed_hanjie", Float.intBitsToFloat(regInt[5]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_TIANCHONG");
                sqLiteDatabase.insert("hanjie", null, valuesA);
                valuesA.clear();
            }

            notification_pop(limit_strings);

        } else if (MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN) {
            if (limit_detector(22, 18, Float.intBitsToFloat(regInt[0]))) {
                limit_strings = "电压超限";
                valuesA.put("weld_vol", Float.intBitsToFloat(regInt[0]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GAIMIAN");
                sqLiteDatabase.insert("voltage1", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(230, 160, Float.intBitsToFloat(regInt[1]))) {
                limit_strings = limit_strings + "  " + "电流超限";
                valuesA.put("weld_cur", Float.intBitsToFloat(regInt[1]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GAIMIAN");
                sqLiteDatabase.insert("current", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(100, 80, Float.intBitsToFloat(regInt[2]))) {
                limit_strings = limit_strings + "  " + "送丝速度超限";
                valuesA.put("speed_songsi", Float.intBitsToFloat(regInt[2]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GAIMIAN");
                sqLiteDatabase.insert("songsi", null, valuesA);
                valuesA.clear();
            }
            if (limit_detector(20, 14, Float.intBitsToFloat(regInt[5]))) {
                limit_strings = limit_strings + "  " + "焊接速度超限";
                valuesA.put("speed_hanjie", Float.intBitsToFloat(regInt[5]));
                valuesA.put("time", time);
                valuesA.put("name", "LIMIT_GAIMIAN");
                sqLiteDatabase.insert("hanjie", null, valuesA);
                valuesA.clear();
            }

            notification_pop(limit_strings);

        }
    }




            } catch (IOException e2) {
                e2.printStackTrace();
                Log.e("error111","连接不正常");
                MainActivity.connect_state = false;
                handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, STATE_NONE, -1,"wuyong").sendToTarget();
                try {
                    if(client != null)
                        client.close();
                    if(in != null)
                        in.close();
                    if(out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.prepare();
                Looper.loop();
            }finally {
                try {
                    //Toast.makeText(this.context,"连接停止!",Toast.LENGTH_SHORT).show();
                    MainActivity.connect_state = false;
                    if(client != null)
                    client.close();
                    if(in != null)
                    in.close();
                    if(out != null)
                    out.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }


            }
/*
函数功能：解析字节形成字符串，可以解析不定长短数据

第一个参数为获取到的数据字节，后面为信息种类数，各个类别所占字节数
 */



    private String[] infoCharToString(byte[] bytes, int... x)
    {
        String[] strings = new String[x[0]];
        int sum = 0;
        int shiwu = (0x000000ff & ((int) bytes[0])) << 8 | (0x000000ff & (int) bytes[1]);
        int xieyi = (0x000000ff & ((int) bytes[2])) << 8 | (0x000000ff & (int) bytes[3]);
        int length1 = (0x000000ff & ((int) bytes[4])) << 8 | (0x000000ff & (int) bytes[5]);
        int danyuanbz = (0x000000ff & (int) bytes[6]);
        int gongnengma = (0x000000ff & (int) bytes[7]);
        //异常码处理，出现异常会弹窗提醒
        if(gongnengma == 132) {
            String ErCode = Integer.toHexString(bytes[8]);
            DInterface.ShowDialog(context,"读取数据错误","错误代码"+ErCode, context.getResources().getDrawable(R.mipmap.cuowu),this);
            RToast.showToast(context, "连接已断开", Toast.LENGTH_LONG);
            handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, WifiService.STATE_WIFI_NOTCONNECTED, -1, "wuyong").sendToTarget();

            String errorStrings[] =new String[x[0]];
            Mach_info_activity.readerror = 1;
            return strings;
        }
        else {





            for(int i = 0 ; i < x[0] ; i++)
            {

                byte[] data = new byte[x[i+1]];
                for(int j = 0 ; j < x[i+1] ; j++)
                {

                data[j] = bytes[9+j+sum];

                }
                sum = sum + x[i+1];
                strings[i] = new String(data);

                System.out.println("strings"+i+":"+strings[i]);

            }
            return  strings;

        }



    }




    private float[] DecodeFloat(byte[] bytes) {//电流电压数据处理
        int shiwu = (0x000000ff & ((int) bytes[0])) << 8 | (0x000000ff & (int) bytes[1]);
        int xieyi = (0x000000ff & ((int) bytes[2])) << 8 | (0x000000ff & (int) bytes[3]);
        int length1 = (0x000000ff & ((int) bytes[4])) << 8 | (0x000000ff & (int) bytes[5]);
        int danyuanbz = (0x000000ff & (int) bytes[6]);
        int gongnengma = (0x000000ff & (int) bytes[7]);
        //异常码处理，出现异常会弹窗提醒
        if(gongnengma == 132) {
            String ErCode = Integer.toHexString(bytes[8]);
            DInterface.ShowDialog(context,"读取数据错误","错误代码"+ErCode, context.getResources().getDrawable(R.mipmap.cuowu),this);
            RToast.showToast(context, "连接已断开", Toast.LENGTH_LONG);
            handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, WifiService.STATE_WIFI_NOTCONNECTED, -1, "wuyong").sendToTarget();

            float a[] =new float[2];
            MainActivity.readError = true;
            return a;
        }
        else {
            //对收到的十六进制数据进行解包处理
            int length2 = Integer.parseInt(Integer.toHexString(bytes[8]), 16);//shujuchangdu
            float data[] = new float[length2 / 2];
            for (int i = 0; i < (length2 / 2); i++) {
                int firstb = 0;
                int secondb = 0;
                int thirdb = 0;
                int fourthb = 0;
                firstb = (0x000000FF) & ((int) bytes[9 + 4 * i]);
                secondb = (0x000000FF) & ((int) bytes[10 + 4 * i]);
                thirdb = (0x000000FF) & ((int) bytes[11 + 4 * i]);
                fourthb = (0x000000FF) & ((int) bytes[12 + 4 * i]);
                int v = (thirdb << 24) | (fourthb << 16) | (firstb << 8) | secondb;//一共四个字节，两个字，前面为低位后面为高位。所以第三个字节往前放，第二个字节往
                // System.out.println(v);
                float w = Float.intBitsToFloat(Integer.valueOf(Integer.toHexString(v), 16));
                data[i] = w;

            }

            MainActivity.readError = false;
            return data;
        }




    }

    private Integer[] DecodeT(byte[] bytes) {//时间/速度角度方向数据解包处理
        int shiwu = (0x000000ff & ((int) bytes[0])) << 8 | (0x000000ff & (int) bytes[1]);
        int xieyi = (0x000000ff & ((int) bytes[2])) << 8 | (0x000000ff & (int) bytes[3]);
        int length1 = (0x000000ff & ((int) bytes[4])) << 8 | (0x000000ff & (int) bytes[5]);
        int danyuanbz = (0x000000ff & (int) bytes[6]);
        int gongnengma = (0x000000ff & (int) bytes[7]);
        //异常码处理
        if(gongnengma == 132) {
            String ErCode = Integer.toHexString(bytes[8]);


            DInterface.ShowDialog(context,"读取数据错误","错误代码"+ErCode, context.getResources().getDrawable(R.mipmap.cuowu),this);//这是一个Dialog弹窗，进行出错提醒
            RToast.showToast(context, "连接已断开", Toast.LENGTH_LONG);
            handler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, WifiService.STATE_WIFI_NOTCONNECTED, -1, "wuyong").sendToTarget();
            Integer b[] = new Integer[2];//这个数组没有用到，只是为了让函数有个返回值
            MainActivity.readError = true;//true表示读取时发生了错误
            return  b;
        }
        else {

            int length2 = bytes[8];

            for (int u =0 ; u<length2 ;u++)
            {
                Log.i("databyte"+u,Integer.toHexString(bytes[u]));

            }

                    //Integer.parseInt(Integer.toHexString(bytes[8]), 16);//shujuchangdu
            Integer data[] = new Integer[length2 / 4];
//            System.out.println("length"+length2);
            for (int i = 0; i < (length2 / 4); i++) {


//                String s="3E1E9E9F";
//                Float value = Float.intBitsToFloat(Integer.valueOf(s.trim(), 16));
//                System.out.println("浮点数："+value);


                //System.out.println(i);
                int firstb = 0;
                int secondb = 0;
                int thirdb = 0;
                int fourthb = 0;
                firstb = (0x000000FF) & ((int) bytes[9 + 4 * i]);
                secondb = (0x000000FF) & ((int) bytes[10 + 4 * i]);
                thirdb = (0x000000FF) & ((int) bytes[11 + 4 * i]);
                fourthb = (0x000000FF) & ((int) bytes[12 + 4 * i]);


//System.out.println("第1个"+Integer.toHexString((int)bytes[9 + 4 * i]));
//System.out.println("第2个"+Integer.toHexString((int)bytes[10 + 4 * i]));
//System.out.println("第3个"+Integer.toHexString((int)bytes[11 + 4 * i]));
//System.out.println("第4个"+Integer.toHexString((int)bytes[12 + 4 * i]));



                Integer v = Integer.valueOf(Integer.toHexString((int)((thirdb<< 24) | (fourthb<< 16) | ( firstb<< 8) |secondb)), 16);
                //System.out.println("HEX:"+Integer.toHexString((thirdb << 24) | (fourthb << 16) | (firstb << 8) | secondb));
//                int v = (firstb << 24) | (secondb << 16) | (thirdb << 8) | fourthb;
                //  System.out.println(v);
                //int w = Integer.parseInt(Integer.toHexString(v), 16);
                //System.out.println(w);
                data[i] = v;
            }
            MainActivity.readError = false;
            return data;
        }

    }

    private boolean limit_detector(float limit_max,float limit_min,float num) {


        return (num < limit_min)||(num > limit_max);

    }


    private  void notification_pop(String st)
    {

        Log.i("pop","notification_pop");
if(SettingActivity.isNotion_state()) {
    //获取状态通知栏管理
    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder builder;
    //判断是否是8.0Android.O
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        manager.createNotificationChannel(new NotificationChannel("static", "Primary Channel", NotificationManager.IMPORTANCE_HIGH));

        builder = new NotificationCompat.Builder(context, "static");
    } else {
        builder = new NotificationCompat.Builder(context);
    }
    //对builder进行配置
    builder.setContentTitle("超限警报") //设置通知栏标题
            .setContentText(st) //设置通知栏显示内容
            .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
            .setSmallIcon(R.mipmap.showdata)
            .setDefaults(DEFAULT_ALL)
            .setAutoCancel(true); //设置这个标志当用户单击面板就可以将通知取消
    //绑定intent，点击图标能够进入某activity
    Intent mIntent = new Intent(context, JumpActivity.class);
    //mIntent.putExtras(bundle);
    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setContentIntent(mPendingIntent);
    //绑定Notification，发送通知请求
    manager.notify(0, builder.build());
}
    }
    public DataInputStream getDFIn() { return this.in;}
    public Socket getDFSocket() {
        return client;
    }
    public static int getStatus_hanqiang() {
        return status_hanqiang;
    }

    public void setStatus_hanqiang(int status_hanqiang) {
        this.status_hanqiang = status_hanqiang;
    }

    public static boolean isAll_data() {
        return all_data;
    }

    public void setAll_data(boolean all_data) {
        this.all_data = all_data;
    }

    public void sendcommand(byte[] a) {
        try {
        for(int i = 0; i< a.length; i++)
        {

                out.write(a[i]);

        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }










    public void sendHexGetFirstPart()
{


    byte[] command = Modbus.modbus_body(Modbus.MODBUS_FIRST, 78, 10);
    try {
        for(int i = 0; i< command.length; i++)
        {
            out.write(command[i]);
        }
       // out.flush();


    } catch (IOException e) {
        e.printStackTrace();
    }


}

    public void sendHexGetSecondPart()
    {
        byte[] command;
        if(equipment == HANQIANG1) {
            command = Modbus.modbus_body(Modbus.MODBUS_FIRST, 88, 6);
        }
        else
        {
            command = Modbus.modbus_body(Modbus.MODBUS_FIRST, 94, 6);
        }
        try {
            for(int i=0;i<command.length;i++)
            {
                out.write(command[i]);
            }
           // out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void sendHexGetThirdPart()
    {
        byte[] command;
        if(equipment == HANQIANG1) {
            command = Modbus.modbus_body(Modbus.MODBUS_FIRST,100,6);
        }
        else
        {
            command = Modbus.modbus_body(Modbus.MODBUS_FIRST, 106, 6);
        }

        try {
            for(int i=0;i<command.length;i++)
            {
                out.write(command[i]);
            }
            // out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setEquipment(int equipment) {
        this.equipment = equipment;
    }



    public void send(String st)
    {
        try {

            out.write(st.getBytes());
            out.flush();
            //out.close();

        } catch (IOException e) {
            e.printStackTrace();
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

    public void timer() {
        if(timer==null)
            timer = new Timer();
        if(timerTask==null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        in.close();
                        out.close();
                        socketFun.close();
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
