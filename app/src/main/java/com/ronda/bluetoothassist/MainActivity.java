package com.ronda.bluetoothassist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ronda.bluetoothassist.base.AppConst;
import com.ronda.bluetoothassist.utils.Entry;
import com.ronda.bluetoothassist.utils.MChart;
import com.ronda.bluetoothassist.utils.RToast;
import com.socks.library.KLog;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import database.MydatabaseHelper;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.ronda.bluetoothassist.DealFun.HANQIANG1;
import static com.ronda.bluetoothassist.DealFun.HANQIANG2;
import static com.ronda.bluetoothassist.DealFun.getStatus_hanqiang;
import static com.ronda.bluetoothassist.DealFun.isAll_data;

public class MainActivity extends AppCompatActivity {
    private DataInputStream in;
    private BluetoothChatService mChatService;
    int bufLength = 200;
    byte[] dataBuf = new byte[bufLength]; //为了环形队列设置的。
    static int head = 0;
    static int tail = 0;
    public static boolean connect_state = false;
    //变量区
    private float[] reg1 = new float[2] ;
    private int[] reg11 = new int[3] ;
    private int[] int_reg1 = new int[5] ;
    private float[] reg2 = new float[6];
    private float[] int_reg2 = new float[6];
    private float[] reg3 = new float[6];
    private int[] int_reg3 = new int[3];
    private float[] reg4 = new float[6];
    private String st_time;
    private float linex=0;
    private float liney=0;
    private String data_ip;
    private String data_port;
    private String oldString = "焊口编号：";
    private long time;
    int voltage = 0, current = 0, power = 0;

    private EditText et_send, et_receive;
    private Button btn_connect, btn_clear;
    private CheckBox cb_hex;
    private Button btn2;

    private Button wifiuse;
    private Button lineB;
    private Button lineR;
    private Button hex;
    private Button lookdata;
    private Button changeID;
    private Button main_bu_connect;
private TextView hankouNum_text;
    private TextView songsisudu;
    private TextView baikuan;
    private TextView zuobianting;
    private TextView youbianting;
    private TextView hanjiesudu;
    private TextView hanjiejiaodu;
    private TextView hanjiefangxiang;
    private TextView tv_state;
    private TextView time_s;
    private TextView time_us;
    private TextView redLineData;
    private TextView blueLineData;
    private TextView show_ip;
    private TextView show_port;
    private TextView baojing_text;
    private String string;
    private Spinner myspinner;
    private  Button connect_main;
    private ArrayAdapter myAdapter;
    private List<String> list_handao ;
    private boolean Press_flag = false;
    private boolean Start_flag = false;
    private boolean lookflag = false;
    public static boolean readError = false;
    private static int data_limit = 0;
    public static int LIMIT_GENHAN = 1;
    public static int LIMIT_REHAN = 2;
    public static int LIMIT_TIANCHONG = 3;
    public static int LIMIT_GAIMIAN = 4;
    public static int LIMIT_BUXIANSHI = 0;
    private boolean readVIError = false;
    private boolean lv_line_show_data = false;
    private static int indexV = 0;
    private float[] storeB = new float[400];
    private static int indexI = 0;
    private float[] storeP = new float[180];
    private static int indexP = 0;
    //private MChart myChart=new MChart(this);
    MChart myChart = null;
    private float[] storeV1 = new float[180];
    private float[] storeI1 = new float[180];
    private float[] storeP1 = new float[180];
    private float[] storeR = new float[400];
    private int nnm_of_data;
    private boolean flagP = true;
    private boolean flagR = true;
    private boolean flagB = true;

    private WifiService wifiService;
    private static DealFun dealFun = new DealFun();
    private int datacount=0;


    private float[] setNumRed= new float[6];
    private float[] setNumBlue= new float[6];
    private FloatingActionButton floatingActionButton_graph;

    private MydatabaseHelper mydatabaseHelper;
    private SQLiteDatabase sqldb;
    private int version = 1;


    private static String string_extra;
    private Intent intentdf;
    
    private LinkedList<Integer[]> data_set_main ;

    public static String getString_extra() {
        return string_extra;
    }

    public static void setString_extra(String string_extra) {
        MainActivity.string_extra = string_extra;
    }


    public static DealFun getDealFun() {
        return dealFun;
    }

    public static int getData_limit() {
        return data_limit;
    }

    public static void setData_limit(int data_limit) {
        MainActivity.data_limit = data_limit;
    }

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent_mainAC_GraphAC = getIntent();
        string_extra = intent_mainAC_GraphAC.getStringExtra("amvalue");

        Toast.makeText(this,string_extra,Toast.LENGTH_LONG).show();
        myChart = (MChart) findViewById(R.id.myChart);
        Button lineR = (Button) findViewById(R.id.lineR);
        Button lineB = (Button) findViewById(R.id.lineB);
        connect_main = (Button)findViewById(R.id.connect_main);
        lookdata = (Button) findViewById(R.id.Lookdata);
        tv_state = (TextView) findViewById(R.id.tv_state);
        btn_clear = (Button)findViewById(R.id.btn_clear);
        main_bu_connect = (Button)findViewById(R.id.connect_main);
        redLineData = (TextView)findViewById(R.id.redlinedata);
        blueLineData = (TextView)findViewById(R.id.bluelinedata);
        floatingActionButton_graph = (FloatingActionButton)findViewById(R.id.floatingActionButton_graph);
        myspinner = (Spinner)findViewById(R.id.spinner_handao);
        hankouNum_text = (TextView)findViewById(R.id.hankouNum_text);
        hankouNum_text.setSelected(true);
        TextView redtext = (TextView)findViewById(R.id.redlinestring);
        TextView bluetext = (TextView)findViewById(R.id.bluelinestring);
        //
        dealFun.setDFHandler(mHandler);//将UI线程的Hangdler传入子线程，通过UI线程的Handler子线程可以通过消息队列将数据传入UI线程并进行界面更新
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.remove_layout);
        LinearLayout bluelinearLayout = (LinearLayout)findViewById(R.id.blue_red_view);

        list_handao = new ArrayList<>();
        list_handao.add("根焊");
        list_handao.add("热焊");
        list_handao.add("填充");
        list_handao.add("盖面");
        list_handao.add("不显示");
        myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list_handao);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter( myAdapter);

        if(data_limit == LIMIT_BUXIANSHI)
        myspinner.setSelection(4,true);
        else if(data_limit == LIMIT_GENHAN)
            myspinner.setSelection(0,true);
        else if(data_limit == LIMIT_REHAN)
            myspinner.setSelection(1,true);
        else if(data_limit == LIMIT_TIANCHONG)
            myspinner.setSelection(2,true);
        else if(data_limit == LIMIT_GAIMIAN)
            myspinner.setSelection(3,true);

        myspinner.setVisibility(INVISIBLE);

        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    data_limit = LIMIT_GENHAN;
                }
                else if(position == 1)
                {

                    data_limit = LIMIT_REHAN;
                }
                else if(position == 2)
                {
                    data_limit = LIMIT_TIANCHONG;
                }
                else if(position == 3)
                {
                    data_limit = LIMIT_GAIMIAN;
                }
                else
                {data_limit = LIMIT_BUXIANSHI;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }

      );
//改变坐标文字
        if("vol_cur".equals(string_extra))
        {
            myChart.setString("Current","Voltage");
            for(int o= 0;o<6;o++)
            {
                setNumBlue[o] = 100*o;
                setNumRed[o] =20*o;
                redtext.setText("电流数值：");
                bluetext.setText("电压数值：");
                myspinner.setVisibility(VISIBLE);

            }
        }
        else if("sudu_songsi".equals(string_extra))
        {
            myChart.setString("Speed_tran","speed_weld");
            for(int o= 0;o<6;o++)
            {
                setNumBlue[o] = (float)(24*o);
                setNumRed[o] = 8*o;
                redtext.setText("送丝速度数值：");
                bluetext.setText("焊接速度值: ");
                myspinner.setVisibility(VISIBLE);



            }
        }
        else if("envir_temp_humi".equals(string_extra))
        {
            myChart.setString("envir_humi","envir_temp");
            for(int o= 0;o<6;o++)
            {
                setNumBlue[o] = 16*o;
                setNumRed[o] = 20*o;
                redtext.setText("环境湿度数值：");
                bluetext.setText("环境温度数值：");
            }
        }
        else
        {
            myChart.setString("temp_weld","");
            for(int o= 0;o<6;o++)
            {
                setNumBlue[o] = 40*o;
                setNumRed[o] = 44*o;
                redtext.setText("焊层温度数值：");
                bluetext.setText("");
                bluelinearLayout.removeView(linearLayout);
                bluelinearLayout.removeView((View)findViewById(R.id.Bline4));
            }

        }
        ArrayList<Float> mlist = new ArrayList<>();
        ArrayList<Float> tlist = new ArrayList<>();
        for(int i = 0 ;i<6 ;i++) {
            tlist.add(setNumRed[i]);
            mlist.add(setNumBlue[i]);

        }
        myChart.setMileageList(mlist);
        myChart.setTimeList(tlist);

        ArrayList<String> dlist = new ArrayList<>();
        String[] dates = new String[]{"1", "2", "3", "4", "5", "6", "7"};
        dlist.addAll(Arrays.asList(dates));
        setChartChange(datacount,myChart, 9, dlist);



        /**
         *
         *修改：
         *作用：database 数据库处理
         *参数：
         *重要笔记：
         *
         */

        mydatabaseHelper = new MydatabaseHelper(this,"databasemach.db",null,version);
        sqldb = mydatabaseHelper.getWritableDatabase();
        //TODO:添加数据



        main_bu_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (main_bu_connect.getText().toString().equals("连接")) {
                    if (!connect_state) {

                        dealFun.startThread();
                        dealFun.setDFHandler(mHandler);//将UI线程的Hangdler传入子线程，通过UI线程的Handler子线程可以通过消息队列将数据传入UI线程并进行界面更新
                        dealFun.setDFContext(MainActivity.this);//将MainActivity的context传入子线程，方便子线程中设置显示控件
                        new Thread(dealFun).start();//开启新的线程

                   }

                    //DataService.setDF(dealFun);


                    RToast.showToast(MainActivity.this, "正在连接...", Toast.LENGTH_LONG);
                } else if (main_bu_connect.getText().toString().equalsIgnoreCase("断开")) {

                        dealFun.stopThread();
                        connect_state = false;
                        Toast.makeText(MainActivity.this, "连接已断开", Toast.LENGTH_LONG).show();
                        mHandler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, BluetoothChatService.STATE_NONE, -1, "wuyong").sendToTarget();//将消息传入主线程的消息队列等待处理

                }

            }
        });
//        btn2.setOnClickListener(new View.OnClickListener() {    //wifi
//            @Override
//            public void onClick(View view) {
//                setChange(datacount,1, myChart);
//            }
//        });
//        wifiuse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {//按钮“连接”的监听
//
//                if (wifiuse.getText().toString().equals("连接")) {
//                    wifiService = new WifiService();//创建一个类的对象
//                    wifiService.setHandler(mHandler);//将UI线程的Hangdler传入子线程，通过UI线程的Handler子线程可以通过消息队列将数据传入UI线程并进行界面更新
//                    wifiService.setContext(MainActivity.this);//将MainActivity的context传入子线程，方便子线程中设置显示控件
//                    new Thread(wifiService).start();//开启新的线程
//                    RToast.showToast(MainActivity.this, "正在连接...", Toast.LENGTH_LONG);
//                } else if (wifiuse.getText().toString().equalsIgnoreCase("断开")) {
//                    try {
//                        wifiService.getIn().close();
//                        wifiService.getSocket().close();//关闭输入流和套接字（socket）
//                        Toast.makeText(MainActivity.this, "连接已断开", Toast.LENGTH_LONG).show();
//                        mHandler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, WifiService.STATE_WIFI_NOTCONNECTED, -1, "wuyong").sendToTarget();//将消息传入主线程的消息队列等待处理
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//
//            }
//        });
        lineR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按钮“I曲线”的监听
                if (flagR)
                    flagR = false;
                else
                    flagR = true;

                myChart.setLineIstatus(flagR);//将I曲线的显示状态传入MChart类中
                setChange(datacount,1, myChart);
            }
        });

        lineB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按钮“V曲线”的监听

                if (flagB)
                    flagB = false;
                else
                    flagB = true;
                myChart.setLineVstatus(flagB);
                setChange(datacount,1, myChart);
            }
        });
//        btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {  //按钮蓝牙“连接“的监听
//                if (btn_connect.getText().toString().equals("连接")) {
//                    showDialog();//弹出蓝牙列表
//                } else {
//                    mChatService.stop();
//                }
//            }
//        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按钮”清空数据“的监听
                for (int i = 0; i < 180; i++) {
                    storeR[i] = 0;
                    storeB[i] = 0;
                }
                indexV = 0;

                indexI = 0;


                datacount = 0;//以上所有设置“0”的操作都是为了清空操作后进行变量归零
                setChange(datacount,1, myChart);
            }
        });

        lookdata.setOnClickListener(new View.OnClickListener() {//按钮“查看数据”监听
            @Override
            public void onClick(View v) {
                if (lookflag) {
                    lookflag = false;
                    lv_line_show_data = false;
                }
                else {
                    lookflag = true;
                    lv_line_show_data = true;
                }
                myChart.setLookFlag(lookflag);
                setChange(datacount,1, myChart);

            }
        });
//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {//按钮“发送”监听
//
//                string = et_send.getText().toString();//从界面的输入框中获取user输入的内容
//                if (wifiuse.getText().toString().equalsIgnoreCase("断开")) {
//                    wifiService.setstring(string);//将获得的字符串传入wifiService中
//                    Toast.makeText(MainActivity.this, "已发送!", Toast.LENGTH_SHORT).show();
//
//                } else
//                    Toast.makeText(MainActivity.this, "请连接WIFI!", Toast.LENGTH_SHORT).show();
//            }
//        });
        myChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//显示框的监听
                switch (event.getAction()) {//当对屏幕进行“按下、按住滑动、抬起手指”动作时，event能获取到不同的动作值
                    case MotionEvent.ACTION_DOWN://按下


                        linex = event.getX();
                        liney = event.getY();//获取按下的位置
                        if((Math.abs(linex-myChart.line_x))<30)//计算按下的位置与绿色线的X值相差是否小于30，若是则认为user要滑动绿线
                        { Press_flag = true;
                        }
                        break;
                     case MotionEvent.ACTION_MOVE://按住移动

                         if (Press_flag)
                         {
                            myChart.line_x = event.getX();
                            if(Start_flag) {
                                for (int i = 0; i < datacount; i++) {//找到绿线位置的值是多少
                                    if (((myChart.getX(myChart.mMileageDatas.get(i).x)) > myChart.line_x) && (myChart.line_x >= myChart.getX(myChart.mMileageDatas.get(1).x)) && (myChart.getX(myChart.mMileageDatas.get(i - 1).x) < myChart.line_x)) {
                                        redLineData.setText(String.valueOf(myChart.mMileageDatas.get(i - 1).y));
                                        blueLineData.setText(String.valueOf(myChart.mTimesDatas.get(i - 1).y));
                                    }
                                }
                            }


                         }
                        break;
                    case MotionEvent.ACTION_UP://手指抬起
                        Press_flag = false;

                }
                setChange(datacount,1,myChart);
                return true;
            }
        });


        mChatService = new BluetoothChatService(mHandler);
//
//
//        // 连接蓝牙时，会有对话框提示，所以不能在onCreate()中，只能是所有View绘制完之后才可以
        //   String addr = SPUtils.getMainBluetoothAddr();
//
//        if (addr.isEmpty()) {
//            showDialog();
//        } else {//自动连接蓝牙
//            mChatService.connect(addr);
//        }

    floatingActionButton_graph.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        onBackPressed();
        }
    });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.connect_state)
        {   connect_main.setText("断开");

            tv_state.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.colorLV));
        }
        else if(!MainActivity.connect_state)
        {
            connect_main.setText("连接");

            tv_state.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.colorHONG));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar,menu);
    return true;
    }




//    @Override
//    protected void onResume() {
//        super.onResume();
//        data_ip = com.ronda.bluetoothassist.IPChangeActivity.getdata_ip();
//        data_port = com.ronda.bluetoothassist.IPChangeActivity.getdata_port();
//
//    }






    private void setChange(int datacount, int type, MChart myChart) {//类似一种数据更新，感觉分成三个case没啥用，一个就够了
        Start_flag = true;
        switch (type) {
            case 0:
                ArrayList<String> dlist1 = new ArrayList<>();
                String[] dates = new String[]{"1", "21", "42", "63", "84", "90"};
                dlist1.addAll(Arrays.asList(dates));
                setChartChange(datacount,myChart, 180, dlist1);
                // Toast.makeText(MainActivity.this, "蓝牙", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                ArrayList<String> dlist2 = new ArrayList<>();
                String[] dates2 = new String[]{"2", "42", "84", "126", "168", "180"};
                dlist2.addAll(Arrays.asList(dates2));
                setChartChange(datacount,myChart, 180, dlist2);
                // Toast.makeText(MainActivity.this, "WiFi", Toast.LENGTH_SHORT).show();
                break;
            default:
                ArrayList<String> dlist3 = new ArrayList<>();
                String[] dates3 = new String[]{"2", "42", "84", "126", "168", "180"};
                dlist3.addAll(Arrays.asList(dates3));
                setChartChange(datacount,myChart, 180, dlist3);
                // Toast.makeText(MainActivity.this, "GPRS", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDataChange(int datacount, int type, MChart myChart,String[] st)
    {Start_flag = true;
        ArrayList<String> dlist3 = new ArrayList<>();
        String[] dates3 = st;
        dlist3.addAll(Arrays.asList(dates3));
        setChartChange(datacount,myChart, 180, dlist3);

    }


    private void setChartChange(int datanum,MChart myChart, int count, ArrayList<String> list) {//数据写到对应的列表里，以便后续的显示
        myChart.setDatesList(list, count);

        ArrayList<Entry> mdlist = new ArrayList<>();

        ArrayList<Entry> tdlist = new ArrayList<>();
       // ArrayList<Entry> powerlist = new ArrayList<>();

//           mdlist.add(new Entry(i, (float) (Math.random()*80)));
//           tdlist.add(new Entry(i, (float) (Math.random()*160)));

        for(int j =0;j<datanum;j++) {
            tdlist.add(new Entry(j, storeR[j]));//HONG
            mdlist.add(new Entry(j, storeB[j]));//LAN
        }
        myChart.setMileageDataList(mdlist);  //voltage
        myChart.setTimeDataList(tdlist);     //current
        myChart.setDatanum(datanum);
        myChart.invalidate();
    }


//
//    private int[] Decode(byte[] bytes) {//数据解包处理
//        int affair = (0x000000ff & ((int) bytes[0])) << 8 | (0x000000ff & (int) bytes[1]);
//        int protocol = (0x000000ff & ((int) bytes[2])) << 8 | (0x000000ff & (int) bytes[3]);
//        int length1 = (0x000000ff & ((int) bytes[4])) << 8 | (0x000000ff & (int) bytes[5]);
//        int unit = (0x000000ff & (int) bytes[6]);
//        int code_founction = (0x000000ff & (int) bytes[7]);
//        //异常码处理
//        if(code_founction == 132) {
//            String ErCode = Integer.toHexString(bytes[8]);
//            DInterface.ShowDialog(this,"读取数据错误","错误代码"+ErCode, getResources().getDrawable(R.mipmap.cuowu),dealFun);//这是一个Dialog弹窗，进行出错提醒
//            RToast.showToast(MainActivity.this, "连接已断开", Toast.LENGTH_LONG);
//            mHandler.obtainMessage(AppConst.MESSAGE_STATE_CHANGE, WifiService.STATE_WIFI_NOTCONNECTED, -1, "wuyong").sendToTarget();
//            readError = true;//true表示读取时发生了错误
//            return  new int[0];
//        }
//        else {
//
//
//            int length2 = Integer.parseInt(Integer.toHexString(bytes[8]), 16);//shujuchangdu
//            int data[] = new int[length2 / 2];
//            for (int i = 0; i < (length2 / 2); i++) {
//                int firstb = 0;
//                int secondb = 0;
//                int thirdb = 0;
//                int fourthb = 0;
//                firstb = (0x000000FF) & ((int) bytes[9 + 4 * i]);
//                secondb = (0x000000FF) & ((int) bytes[10 + 4 * i]);
//                thirdb = (0x000000FF) & ((int) bytes[11 + 4 * i]);
//                fourthb = (0x000000FF) & ((int) bytes[12 + 4 * i]);
//                int v = (thirdb << 24) | (fourthb << 16) | (firstb << 8) | secondb;
////                int v = (firstb << 24) | (secondb << 16) | (thirdb << 8) | fourthb;
//                //  System.out.println(v);
//
//                data[i] = Integer.valueOf(Integer.toHexString(v), 16);
//
//
//            }
//            readError = false;
//            return data;
//        }
//
//    }



    private void showDialog() {//蓝牙连接的弹窗
        DeviceListDialogFragment dialog = new DeviceListDialogFragment();
        dialog.setCallback(new DeviceListDialogFragment.Callback() {
            @Override
            public void onSelectedItem(String address) {
                // 连接远程蓝牙
                mChatService.connect(address);
            }
        });
        dialog.show(MainActivity.this.getFragmentManager(), "deviceListDialogFragment");
    }




    private final Handler mHandler = new Handler() {//Handler消息的处理函数，wifi数据的处理在最底部
        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message msg) {
            AppCompatActivity activity = MainActivity.this;

            switch (msg.what) {
                case AppConst.MESSAGE_STATE_CHANGE:

                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            String deviceName = msg.obj.toString();// 此时 obj 的值是 remoteDeviceName, 不为 null
                            //tv_label.setText("connected to " + deviceName);
                            Toast.makeText(activity, "connected to " + deviceName, Toast.LENGTH_SHORT).show();
                            connect_main.setText("断开");

                            tv_state.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.colorLV));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //tv_label.setText("connecting...");
                            Toast.makeText(activity, "connecting...", Toast.LENGTH_SHORT).show();
                            //btn_connect.setText("断开");

                            tv_state.setBackgroundColor(activity.getResources().getColor(R.color.colorTUHUANG));
                            break;
                        case BluetoothChatService.STATE_NONE:
                            //tv_label.setText("not connected");
                            connect_main.setText("连接");

                            tv_state.setBackgroundColor(activity.getResources().getColor(R.color.colorHONG));
                            Toast.makeText(activity, "未连接成功！", Toast.LENGTH_SHORT).show();
                            break;
                        case WifiService.STATE_WIFI_CONNECTED:
                            String wuyong = msg.obj.toString();

                            //wifiuse.setText("断开");

                            tv_state.setBackgroundColor(activity.getResources().getColor(R.color.colorLV));

                            break;
                        case WifiService.STATE_WIFI_NOTCONNECTED:

                            //wifiuse.setText("连接");

                            tv_state.setBackgroundColor(activity.getResources().getColor(R.color.colorHONG));
                            //Toast.makeText(activity, "not connected", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case AppConst.MESSAGE_WRITE:
                    String writeMessage = (String) msg.obj;
                    //mViewData.setText(writeMessage);
                    KLog.e("write: " + writeMessage);
                    break;
                case AppConst.MESSAGE_READ:
                    //String readMessage = (String) msg.obj;
                    byte[] buf = (byte[]) msg.obj;
                    int headTmp;
                    int changeState = 0;
                    for (int i = 0; i < buf.length; i++) {
                        dataBuf[tail] = buf[i];
                        tail = tail + 1;
                    }
                    headTmp = head;
                    while ((tail - headTmp) > 5) {
                        changeState = 1;
                        if (dataBuf[headTmp] == 'V') {
                            voltage = 0;
                            dataBuf[headTmp] = 0;
                            dataBuf[headTmp + 1] = 0;
                            for (int i = 0; i < 4; i++) {
                                voltage = voltage << 8 | dataBuf[headTmp + 2 + i];
                                dataBuf[headTmp + 2 + i] = 0;
                            }
                            if (indexV < 30) {
                                storeB[indexV] = voltage;
                                indexV++;
                                Log.e("store", "ok2");
                                System.out.println(voltage);
                            } else {
                                int j;
                                for (j = 0; j < 29; j++) {
                                    storeB[j] = storeB[j + 1];
                                }
                                storeB[j] = voltage;
                            }
                            //setChange(0,myChart);
                            headTmp = headTmp + 6;
//                            et_receive.append(String.valueOf(voltage));
                        } else if (dataBuf[headTmp] == 'I') {
                            current = 0;
                            dataBuf[headTmp] = 0;
                            dataBuf[headTmp + 1] = 0;
                            for (int i = 0; i < 4; i++) {
                                current = current << 8 | dataBuf[headTmp + 2 + i];
                                dataBuf[headTmp + 2 + i] = 0;
                            }
                            if (indexI < 30) {
                                storeB[indexI] = current;
                                indexI++;
                            } else {
                                int j;
                                for (j = 0; j < 29; j++) {
                                    storeB[j] = storeB[j + 1];
                                }
                                storeB[j] = current;
                            }
                            headTmp = headTmp + 6;
//                            et_receive.append(String.valueOf(current));
                        } else if (dataBuf[headTmp] == 'P') {
                            Log.e("Pcon", "ok");
                            power = 0;
                            dataBuf[headTmp] = 0;
                            dataBuf[headTmp + 1] = 0;
                            for (int i = 0; i < 4; i++) {
                                power = power << 8 | dataBuf[headTmp + 2 + i];
                                dataBuf[headTmp + 2 + i] = 0;
                            }
                            if (indexP < 30) {
                                storeP[indexP] = power;
                                indexP++;
                            } else {
                                int j;
                                for (j = 0; j < 29; j++) {
                                    storeP[j] = storeP[j + 1];
                                }
                                storeP[j] = power;
                            }
                            headTmp = headTmp + 6;
//                            et_receive.append(String.valueOf(power));
                        } else
                            headTmp += 1;

                    }
                    if (changeState == 1) {
                        head = headTmp;
                        changeState = 0;
                        int i;
                        for (i = 0; i < tail - head; i++) {
                            dataBuf[i] = dataBuf[head + i];
                            dataBuf[head + i] = 0;
                        }
                        tail = i;
                        head = 0;
                    }

//                    String readTxt = new String(buf, 0, buf.length);
//                    if (cb_hex.isChecked()) {
//                        readTxt = HexUtils.bytesToHexStringWithSpace(buf);
//                    }
//
//                    et_receive.append(readTxt);

//                    scrollToBottom(mScrollView, et_receive);

                    // KLog.e("read: " + readTxt);
                    setChange(datacount,1, myChart);
                    break;
                case AppConst.MESSAGE_TOAST: // 接收连接时失败 和 已连接后又中断 的情况
                    Toast.makeText(activity, msg.getData().getString(AppConst.TOAST), Toast.LENGTH_SHORT).show();
                    break;
              case WifiService.WIFI_READ://WIFI采进来的数据在下面进行处理
                    switch (msg.arg1) {
                        case DealFun.DEALFUN_FIRST:
                            data_set_main =(LinkedList<Integer[]>) msg.obj;
                            nnm_of_data = msg.arg2;
                            String time[] = {"::","::","::","::"};

                        if ("vol_cur".equals(string_extra)) {

                            for (int i = 0; i < nnm_of_data; i++) {
                                storeB[i] = Float.intBitsToFloat(data_set_main.get(i)[0]);//电压
                               storeR[i] = Float.intBitsToFloat(data_set_main.get(i)[1]);//电流
                                if(i == 0)
                                {
                                    time[0] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 59)
                                {
                                    time[1] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 119)
                                {
                                    time[2] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 179)
                                {
                                    time[3] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                            }
                            if(!lv_line_show_data) {
                                redLineData.setText(String.valueOf(storeR[nnm_of_data - 1]));
                                blueLineData.setText(String.valueOf(storeB[nnm_of_data - 1]));
                            }

                        } else if ("sudu_songsi".equals(string_extra)) {
                            for (int i = 0; i < nnm_of_data; i++) {
                                storeB[i] = Float.intBitsToFloat(data_set_main.get(i)[5]);//焊接速度
                                storeR[i] = Float.intBitsToFloat(data_set_main.get(i)[2]);//送丝速度

                                if(i == 0)
                                {
                                    time[0] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 59)
                                {
                                    time[1] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 119)
                                {
                                    time[2] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 179)
                                {
                                    time[3] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }

                            }
                            if(!lv_line_show_data) {
                                redLineData.setText(String.valueOf(storeR[nnm_of_data - 1]));
                                blueLineData.setText(String.valueOf(storeB[nnm_of_data - 1]));
                            }



                        } else if ("envir_temp_humi".equals(string_extra)) {
                            for (int i = 0; i < nnm_of_data; i++) {
                                storeB[i] = Float.intBitsToFloat(data_set_main.get(i)[3]);//环境温度
                                storeR[i] = Float.intBitsToFloat(data_set_main.get(i)[4]);//环境湿度

                                if(i == 0)
                                {
                                    time[0] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 59)
                                {
                                    time[1] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 119)
                                {
                                    time[2] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 179)
                                {
                                    time[3] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }


                            }
                            if(!lv_line_show_data) {
                                redLineData.setText(String.valueOf(storeR[nnm_of_data - 1]));
                                blueLineData.setText(String.valueOf(storeB[nnm_of_data - 1]));
                            }

                        } else {
                            for (int i = 0; i < nnm_of_data; i++) {
                                storeB[i] = 0;//焊接速度
                                storeR[i] = Float.intBitsToFloat(data_set_main.get(i)[6]);//焊层温度

                                if(i == 0)
                                {
                                    time[0] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 59)
                                {
                                    time[1] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 119)
                                {
                                    time[2] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }
                                else if(i == 179)
                                {
                                    time[3] = data_set_main.get(i)[10]+":"+data_set_main.get(i)[11]+":"+data_set_main.get(i)[12];
                                }

                            }
                            if(!lv_line_show_data) {
                                redLineData.setText(String.valueOf(storeR[nnm_of_data - 1]));
                                blueLineData.setText(String.valueOf(storeB[nnm_of_data - 1]));
                            }


                        }
                            setDataChange(nnm_of_data, 1, myChart,time);
                   // setChange(nnm_of_data, 1, myChart);

                            break;
                        case DealFun.DEALFUN_SECOND://时间值的处理
//                            int_reg1 = Decode(bt);
//                            if (!readError) {
//                            //时间  秒  微妙
//                            reg11[0] = int_reg1[0];
//                            reg11[1] = int_reg1[1];
//                            //焊接方向
//                            reg11[2] = int_reg1[4];
//                            //焊接速度  焊接角度
//                            reg1[0] = Float.intBitsToFloat(int_reg1[2]);
//                            reg1[1] = Float.intBitsToFloat(int_reg1[3]);
//
//                                String times = String.valueOf(reg11[0]);
//                                String timeus = String.valueOf(reg11[1]);
//                                time = reg11[0]*1000+reg11[1];
//                                Date date = new Date(time);
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                                st_time = sdf.format(date);
//                                //TODO:时间显示  待处理
//
//                                hanjiefangxiang.setText(reg11[2]+"");
//                                hanjiejiaodu.setText(reg1[1]+"");
//                                hanjiesudu.setText(reg1[0]+"");
//                            }
                            break;
                        case DealFun.DEALFUN_THIRD:

                            //焊缝编号显示
                            if(oldString.equals((String)msg.obj))
                                ;
                            else {
                                oldString = (String) msg.obj;
                                hankouNum_text.setText(oldString);
                            }
                            break;

                    }
                    if(isAll_data())//判断数据是否完整
                    {

                        //判断是哪一号设备的数据  然后入库
                        if (getStatus_hanqiang() == HANQIANG1) {
                            sqldb.execSQL("INSERT INTO mach1 (time,speed ,angel ,direction  ,speed_songsi1  ,v_hanqiang1 ,i_hanqiang1  ,baikuan_hanqiang1 ,time_zuobianting1 ,time_yubianting1) VALUES(?,?,?,?,?,?,?,?,?,?)",
                                    new String[]{st_time, String.valueOf(reg1[0]), String.valueOf(reg1[1]), String.valueOf(reg11[2]), String.valueOf(reg2[0]), String.valueOf(reg2[1]), String.valueOf(reg2[2]), String.valueOf(reg3[0]), String.valueOf(reg3[1]), String.valueOf(reg3[2])});

                        }
                        else if(getStatus_hanqiang() == HANQIANG2)
                        {
                            sqldb.execSQL("INSERT INTO mach2 (time,speed ,angel ,direction  ,speed_songsi1  ,v_hanqiang1 ,i_hanqiang1  ,baikuan_hanqiang1 ,time_zuobianting1 ,time_yubianting1) VALUES(?,?,?,?,?,?,?,?,?,?)",
                                    new String[]{st_time, String.valueOf(reg1[0]), String.valueOf(reg1[1]), String.valueOf(reg11[2]), String.valueOf(reg2[0]), String.valueOf(reg2[1]), String.valueOf(reg2[2]), String.valueOf(reg3[0]), String.valueOf(reg3[1]), String.valueOf(reg3[2])});

                        }
                    }






            }
        }



    };




}
