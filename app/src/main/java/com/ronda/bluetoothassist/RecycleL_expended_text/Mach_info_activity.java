package com.ronda.bluetoothassist.RecycleL_expended_text;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ronda.bluetoothassist.DealFun;
import com.ronda.bluetoothassist.MainActivity;
import com.ronda.bluetoothassist.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Mach_info_activity extends AppCompatActivity {

    private static boolean fresh_state = false;
    public static final int READ_ERROR = 1;
    public static final int MACH_INFORMATION = 0;
    public static int readerror = 0;
    private RecyclerView mRecyclerView;
    private List<DataBean> dataBeanList;
    private DataBean dataBean;
    private RecyclerAdapter mAdapter;
    private DealFun dealFun_mach;
    private String[] parent_name = new String[]{"设备信息","工程项目ID","机组编号","工艺规程","设备编号","焊接工艺","人员编号","焊口编号","焊接位置","焊层名称","工作异常报警"};
    private static String[] init_info = new String[]{"设备信息","工程项目ID","机组编号","工艺规程","设备编号","焊接工艺","人员编号","焊口编号","焊接位置","焊层名称","工作异常报警"};
    private NavigationView navigationView;
    private int[] parent_image = new int[]{R.mipmap.active,R.mipmap.lable,R.mipmap.colorful,R.mipmap.qr,R.mipmap.smart,R.mipmap.share,R.mipmap.purse,R.mipmap.input,R.mipmap.delete,R.mipmap.coupons,R.mipmap.back};
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static boolean activity_Start = false;
    private  String[] mach_info_buf;
    public static void setFresh_state(boolean fresh_state) {
        Mach_info_activity.fresh_state = fresh_state;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acytivity_mach_info);
        //顶部导航栏
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_mach);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);//HomeAsUp是左上部的导航栏
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_mach);
        navigationView = (NavigationView)findViewById(R.id.nav_view_mach);
        dealFun_mach = MainActivity.getDealFun();
        dealFun_mach.setMach_handler(myMachInfoHandler);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        FloatingActionButton floatingActionButton_machinfo = (FloatingActionButton)findViewById(R.id.floatingActionButton_machinfo);
        activity_Start = true;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                initData(init_info);
            }
        });
        initData(init_info);

        floatingActionButton_machinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.setting:

                        break;
                    case R.id.dev_1:

                        break;
                    case R.id.dev_2:


                        break;

                }

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("come into onResume");
        System.out.println(mach_info_buf == null);
        if(mach_info_buf != null)
        initData(mach_info_buf);
        else
            initData(init_info);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                break;
            case android.R.id.home://系统参数需要在前面加上android，自定义的不加
                drawerLayout.openDrawer(Gravity.START);
                break;
        }
        return true;
    }

    /**
     * 模拟数据
     */
    public void initData(String[] mach_info){
        if (mach_info.length < 11)
            throw new NumberFormatException();
        dataBeanList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
           // System.out.println("string_data:"+mach_info[i]);
            dataBean = new DataBean();
            dataBean.setID(i+"");
            dataBean.setType(0);
            dataBean.setParentImage(parent_image[i]);
            dataBean.setParentTxt(parent_name[i]);
            dataBean.setChildTxt(mach_info[i]);
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        setData();
    }

    private void setData(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(this,dataBeanList);
        mRecyclerView.setAdapter(mAdapter);
        //滚动监听
        mAdapter.setOnScrollListener(new RecyclerAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                mRecyclerView.scrollToPosition(pos);
            }
        });
    }


    public Handler myMachInfoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MACH_INFORMATION:


                    String[] data = (String[]) msg.obj;
                    if(readerror != READ_ERROR)
                    {
                        mach_info_buf = data;
                        init_info = data;
                    }
                    Log.i("time",data[11]);
                    break;

            }


        }
    };





}
