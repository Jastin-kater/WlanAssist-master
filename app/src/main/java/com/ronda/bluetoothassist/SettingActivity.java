package com.ronda.bluetoothassist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ronda.bluetoothassist.datamanager.DataCleanManager;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {
    private static boolean notion_state = true;
    private TextView tv_cache;
    private TextView tv_database;
    private File cache;
    private File file;
    private  File sharepre;
    private Switch aSwitch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar)findViewById(R.id.setting_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RelativeLayout layout_clearcache = findViewById(R.id.layout_clearcache);
        RelativeLayout layout_cleardatabase = findViewById(R.id.layout_cleardatabase);
        aSwitch = findViewById(R.id.right_text_notion);
         tv_cache = findViewById(R.id.right_text_clear_cache);
         tv_database = findViewById(R.id.right_text_clear_database);
         cache =new File(this.getCacheDir().getPath());
         file =new File(this.getFilesDir().getPath());
         sharepre = new File("/data/data/"
                + getApplicationContext().getPackageName() + "/shared_prefs");
        try {
            tv_cache.setText(DataCleanManager.getCFSSize(cache,file,sharepre));
            tv_database.setText(DataCleanManager.getCacheSize(new File("/data/data/" + getApplicationContext().getPackageName() + "/databases")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        layout_clearcache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab_cache = new AlertDialog.Builder(SettingActivity.this);
                ab_cache.setTitle("清除缓存")
                .setMessage("清除缓存会导致下载内容以及使用记录被删除，是否确定？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DataCleanManager.cleanInternalCache(getApplicationContext());
                        DataCleanManager.cleanFiles(getApplicationContext());
                        DataCleanManager.cleanSharedPreference(getApplicationContext());
                        DataCleanManager.cleanPathFile(new File("/data/data/"
                                + getApplicationContext().getPackageName() + "/shared_prefs"));
                        try {
                            tv_cache.setText(DataCleanManager.getCFSSize(cache,file,sharepre));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
            }
        });
        layout_cleardatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder ab_cache = new AlertDialog.Builder(SettingActivity.this);
                ab_cache.setTitle("清除数据库")
                        .setMessage("清除缓存会导致数据库中的数据被删除，是否确定？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataCleanManager.cleanDatabases(getApplicationContext());

                                try {
                                    tv_database.setText(DataCleanManager.getCacheSize(new File("/data/data/" + getApplicationContext().getPackageName() + "/databases")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();

            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    setNotion_state(true);
                    Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();
                }
                else{
                    setNotion_state(false);
                    Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_LONG).show();
                }
            }
        });

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

    public static boolean isNotion_state() {
        return notion_state;
    }

    public static void setNotion_state(boolean notion_state) {
        SettingActivity.notion_state = notion_state;
    }
}
