package com.ronda.bluetoothassist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ronda.bluetoothassist.about.About;
import com.ronda.bluetoothassist.graph_container.JumpActivity;
import com.ronda.bluetoothassist.history.HistoryActivity;
import com.ronda.bluetoothassist.linesets.LineChatActivity;
import com.ronda.bluetoothassist.linesets.MyChatActivity;

import androidx.appcompat.app.AppCompatActivity;
import ren.define.circlepictext.CirclePicText;



public class NavPageActivity extends AppCompatActivity {
    private CirclePicText[] circlePic =new CirclePicText[6];
    private int screenWidth;
    private int screenHight;
    private String st[] = new String[1000];
    //导航页的数据
    private int[] circlepicID={R.id.circlepic_lefttop,R.id.circlepic_leftmid,R.id.circlepic_leftdown,
    R.id.circlepic_righttop,R.id.circlepic_rightmid,R.id.circlepic_rightdown};
    private int[] imageDrawable={R.mipmap.ipw,R.mipmap.mach_nav,R.mipmap.about_nav,
            R.mipmap.light_nav,R.mipmap.history_nav,R.mipmap.setting_nav};
    private String[] name={"网络参数","设备","关于","安全警报","历史数据","应用设置"};
//          下面color中可以填入下面的字符串，例如“red”、“gray”
//        * 'red', 'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta',
//        * 'yellow', 'lightgray', 'darkgray', 'grey', 'lightgrey', 'darkgrey',
//        * 'aqua', 'fuchsia', 'lime', 'maroon', 'navy', 'olive', 'purple',
//        * 'silver', 'teal'.
    private String[] color ={"#66CCCC","#FF9999","#CCCCFF","#EE0000","#CC99CC","#FFCC99"};
    //导航页的6个按钮，从左上到右下
//    private MydatabaseHelper mh = new MydatabaseHelper(MyApplication.getContext(),"machDataBase.db",null,1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_nav_main);
        //测量屏幕的尺寸(dp)
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = (int)(displayMetrics.widthPixels/displayMetrics.density);
        screenHight = (int)(displayMetrics.heightPixels/displayMetrics.density);
        Log.i("circle","width:"+displayMetrics.widthPixels+"hight:"+displayMetrics.heightPixels+"density"+displayMetrics.density);
        //设置图片、文字和颜色
        for(int i=0;i<6;i++) {
            circlePic[i] = (CirclePicText) findViewById(circlepicID[i]);
            circlePic[i].setText_Image(imageDrawable[i],name[i],color[i]);
        }

        for(int i = 0 ;i<1000;i++)
            st[i] ="ceshi"+i;
        //利用setPadding设置RelativeLayout中控件到其边缘的距离
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.nav_relative);
        relativeLayout.setPadding((int)(0.4*displayMetrics.density*(screenWidth-300)),(int)(0.5*displayMetrics.density*(screenHight-510)),(int)(0.4*displayMetrics.density*(screenWidth-300)),(int)(0.01*displayMetrics.density*(screenHight-510)));
//setPadding参数的单位是像素
    //设置IP按钮的监听器
    circlePic[0].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(NavPageActivity.this,IPChangeActivity.class);
            startActivity(intent);
        }
    });
//设备按钮的监听器
    circlePic[1].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(NavPageActivity.this, JumpActivity.class);
            startActivity(intent);
        }
    });
    //关于按钮的监听器
    circlePic[2].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NavPageActivity.this, About.class);
            startActivity(intent);

        }
    });
    //安全警报按钮的监听器
    circlePic[3].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentalert = new Intent(NavPageActivity.this, JingBaoActivity.class);
            startActivity(intentalert);
        }
    });
    //历史数据按钮的监听器
    circlePic[4].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intenthistory = new Intent(NavPageActivity.this, HistoryActivity.class);
            startActivity(intenthistory);

        }
    });
    //设置按钮的监听器
    circlePic[5].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentsetting = new Intent(NavPageActivity.this,SettingActivity.class);
            startActivity(intentsetting);

        }
    });




//        String stri = "2019-10-21 21:20:30";
//        String strin = "yyyy-MM-dd hh:mm:ss";
//        SQLiteDatabase SQli = mh.getWritableDatabase();
//        ContentValues values =  new ContentValues();
//        values.put("time",DateDeal.stringToLong(stri,strin));
//        SQli.insert("mach",null,values);
//
////        Log.i("testtime","long:"+DateDeal.stringToLong(stri,strin));
////        Log.i("testtime","int:"+(int)DateDeal.stringToLong(stri,strin));
////        Log.i("testtime","String:"+DateDeal.longToString((int)DateDeal.stringToLong(stri,strin),strin));
//
//        SQLiteDatabase sqlsb = mh.getReadableDatabase();
//        Cursor cursor = sqlsb.query("mach",null,null,null,null,null,null);
//        if(cursor.moveToFirst()){
//            do{
//                Log.i("DATABASE", "weld_vol = "+cursor.getFloat(cursor.getColumnIndex("weld_vol")));
//                Log.i("DATABASE", "speed_weld = "+cursor.getFloat(cursor.getColumnIndex("speed_weld")));
//                Log.i("DATABASE", "info_mach = "+cursor.getString(cursor.getColumnIndex("info_mach")));
//                Log.i("DATABASE", "time = "+ DateDeal.longToString(cursor.getLong(cursor.getColumnIndex("time")),"yyyy-MM-dd HH:mm:ss"));
//
//            }while(cursor.moveToNext());
//
//        }
//        cursor.close();




        //System.out.println("浮点数3："+ FourCharsToFloat.getDoubleByHexstr(s1));

    }







}
