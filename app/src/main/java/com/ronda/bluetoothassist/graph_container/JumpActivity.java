package com.ronda.bluetoothassist.graph_container;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ronda.bluetoothassist.MainActivity;
import com.ronda.bluetoothassist.R;
import com.ronda.bluetoothassist.RecycleL_expended_text.Mach_info_activity;
import com.ronda.bluetoothassist.utils.MChart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.widget.Toast.LENGTH_LONG;


public class JumpActivity extends AppCompatActivity {


    private static final int JPTEXTCHANGE = 0;
    MChart myChart = null;
    private TextView tv_state;
    public static final int MACH_INFO = 1;
    public static final int MACH_CHOOSE1 = 1;
    public static final int MACH_CHOOSE2 = 2;
    private static int mach = MACH_CHOOSE1;


    //DealFun中数据显示判断标志
    public static char SHOW_DATA_DEV1=1;
    public static char SHOW_DATA_DEV2=2;
    public static char data_show_flag = SHOW_DATA_DEV1;

    private FloatingActionButton floatingActionButton_mach;
    private DrawerLayout mdrawerlayout;
    private NavigationView mnavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jumptograph);
        //顶部导航栏
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);//HomeAsUp是左上部的导航栏
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }

    //①
        tv_state = (TextView) findViewById(R.id.tv_state);
        Button b_curvol = (Button)findViewById(R.id.jump_bt_cur_vol);
        Button b_songsisudu = (Button)findViewById(R.id.jump_bt_sudu);
        Button b_envir_temp_humi = (Button)findViewById(R.id.jump_envir_temp_humi);
        Button b_hanjiesdwd = (Button)findViewById(R.id.jump_bt_hanjiesdwd);
        final Button mach_info = (Button)findViewById(R.id.jump_mach_info);

//②
        mdrawerlayout = (DrawerLayout)findViewById(R.id.activity_main) ;
        mnavigationView = (NavigationView)findViewById(R.id.nav_view);

        floatingActionButton_mach = (FloatingActionButton)findViewById(R.id.floatingActionButton_mach);



        //页面九个按钮监听，考虑能否变成Button组？？？？？
        //转到GraphActivity的intent
        final Intent intent_to_graph =new Intent(JumpActivity.this, MainActivity.class);
        b_curvol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_to_graph.putExtra("amvalue","vol_cur");
                startActivity(intent_to_graph);
            }
        });
        b_songsisudu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//            Toast.makeText(JumpActivity.this,"ssss", LENGTH_LONG).show();
//            Intent intent = new Intent(JumpActivity.this,MainActivity.class);
//            startActivity(intent);
                intent_to_graph.putExtra("amvalue","sudu_songsi");
                startActivity(intent_to_graph);

            }
        });
        b_envir_temp_humi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_to_graph.putExtra("amvalue","envir_temp_humi");
                startActivity(intent_to_graph);
            }
        });
        b_hanjiesdwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_to_graph.putExtra("amvalue","hanjiesdwd");
                startActivity(intent_to_graph);
            }
        });

        mach_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mach_intent = new Intent(JumpActivity.this, Mach_info_activity.class);
                startActivity(mach_intent);

            }
        });

//②内容

        //监听器
        //mnavigationView.setCheckedItem(R.id.setting);
        mnavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.setting:
                        Toast.makeText(JumpActivity.this,"选中设置", LENGTH_LONG).show();
                        break;
                    case R.id.dev_1:

                        break;
                    case R.id.dev_2:


                        break;

                }

                return true;
            }
        });


        floatingActionButton_mach.setOnClickListener(new View.OnClickListener() {
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
        {
            tv_state.setText("connected");
            tv_state.setBackgroundColor(JumpActivity.this.getResources().getColor(R.color.colorLV));
        }
        else if(!MainActivity.connect_state)
        {

            tv_state.setText("not connected");
            tv_state.setBackgroundColor(JumpActivity.this.getResources().getColor(R.color.colorHONG));
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                break;
            case android.R.id.home://系统参数需要在前面加上android，自定义的不加
                mdrawerlayout.openDrawer(Gravity.START);
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public Handler getMyJumpHandler() {
        return myJumpHandler;
    }

    public Handler myJumpHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
           switch (msg.arg1)
           {
               case MACH_INFO:

                   break;
               case JPTEXTCHANGE:


                   break;


           }
        }
    };


    public static int getMach() {
        return mach;
    }


}



