package com.ronda.bluetoothassist.about;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.ronda.bluetoothassist.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*
 *
 *类描述：
 *创建人：R
 *创建时间：${DATA}22:10
 *
 */public class About extends AppCompatActivity {

     private Toolbar toolbar;
     private ActivityManager activityManager ;
     private TextView right_text_used_memery;
     private TextView right_text_using_memery;
     private float maxmemery = 0;
     private float toltalmemery = 0;
     private float freememery = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar)findViewById(R.id.activity_about_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        right_text_used_memery = (TextView)findViewById(R.id.right_text_used_memery);
        right_text_using_memery = (TextView)findViewById(R.id.right_text_using_memery);
        activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);


        maxmemery = activityManager.getMemoryClass();
        toltalmemery = Runtime.getRuntime().totalMemory()/(1024*1024);
        freememery = maxmemery - toltalmemery;
        Log.i("maxmemery", String.valueOf(maxmemery));
        Log.i("toltalmemery", String.valueOf(toltalmemery));
        Log.i("freememery", String.valueOf(freememery));
        right_text_using_memery.setText(freememery+"M");
        right_text_used_memery.setText(toltalmemery+"M");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(activityManager != null) {
            maxmemery = activityManager.getMemoryClass();
            toltalmemery = Runtime.getRuntime().totalMemory()/(1024*1024);
            freememery = maxmemery - toltalmemery;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
