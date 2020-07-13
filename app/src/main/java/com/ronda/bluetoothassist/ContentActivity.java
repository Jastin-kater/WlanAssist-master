package com.ronda.bluetoothassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import database.MydatabaseHelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {
    private String query_data = "暂无警报数据";
    private int data_num =0;
    private MydatabaseHelper mydatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Intent intent_type = getIntent();
        String type = intent_type.getStringExtra("type");
        Toolbar toolbar = findViewById(R.id.activity_jingbao_toolbar);
        toolbar.setTitle("警报数据");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView textView = findViewById(R.id.content_baojing);
        mydatabaseHelper =new MydatabaseHelper(this,"machDataBase.db",null,1);
        switch (type)
        {
            case "0":
                Log.i("type","0");
                if(MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN)
                query_data = queryData("voltage1",null,"name=?",new String[]{"LIMIT_GENHAN"},"weld_vol");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_REHAN)
                    query_data = queryData("voltage1",null,"name=?",new String[]{"LIMIT_REHAN"},"weld_vol");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN)
                    query_data = queryData("voltage1",null,"name=?",new String[]{"LIMIT_GAIMIAN"},"weld_vol");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG)
                    query_data = queryData("voltage1",null,"name=?",new String[]{"LIMIT_TIANCHONG"},"weld_vol");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_BUXIANSHI)
                    query_data = "请先选择焊接类型！";
                break;
            case "1":
                Log.i("type","1");
                if(MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN)
                    query_data = queryData("current",null,"name=?",new String[]{"LIMIT_GENHAN"},"weld_cur");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_REHAN)
                    query_data = queryData("current",null,"name=?",new String[]{"LIMIT_REHAN"},"weld_cur");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN)
                    query_data = queryData("current",null,"name=?",new String[]{"LIMIT_GAIMIAN"},"weld_cur");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG)
                    query_data = queryData("current",null,"name=?",new String[]{"LIMIT_TIANCHONG"},"weld_cur");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_BUXIANSHI)
                    query_data = "请先选择焊接类型！";

                break;

            case "2":
                Log.i("type","2");
                if(MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN)
                    query_data = queryData("songsi",null,"name=?",new String[]{"LIMIT_GENHAN"},"speed_songsi");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_REHAN)
                    query_data = queryData("songsi",null,"name=?",new String[]{"LIMIT_REHAN"},"speed_songsi");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN)
                    query_data = queryData("songsi",null,"name=?",new String[]{"LIMIT_GAIMIAN"},"speed_songsi");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG)
                    query_data = queryData("songsi",null,"name=?",new String[]{"LIMIT_TIANCHONG"},"speed_songsi");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_BUXIANSHI)
                    query_data = "请先选择焊接类型！";
                break;

            case "3":
                Log.i("type","3");
                if(MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN)
                    query_data = queryData("hanjie",null,"name=?",new String[]{"LIMIT_GENHAN"},"speed_hanjie");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_REHAN)
                    query_data = queryData("hanjie",null,"name=?",new String[]{"LIMIT_REHAN"},"speed_hanjie");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN)
                    query_data = queryData("hanjie",null,"name=?",new String[]{"LIMIT_GAIMIAN"},"speed_hanjie");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG)
                    query_data = queryData("hanjie",null,"name=?",new String[]{"LIMIT_TIANCHONG"},"speed_hanjie");
                else if(MainActivity.getData_limit() == MainActivity.LIMIT_BUXIANSHI)
                    query_data = "请先选择焊接类型！";
                break;

            case "4":

                break;

            case "5":

                break;
            case "6":

                break;
        }
        System.out.println(query_data);
        textView.setText(query_data);
    }

    private String queryData(String tablename,String[] col,String sel,String [] sel_args,String specialdata){

         StringBuffer stringBuffer = new StringBuffer();

        SQLiteDatabase sd = mydatabaseHelper.getWritableDatabase();
        Cursor cursor = sd.query(tablename,col,sel,sel_args,null,null,null);
        data_num = cursor.getCount();
        if(cursor.moveToFirst())
        {
            do{
                stringBuffer.append(cursor.getFloat(cursor.getColumnIndex(specialdata)));
                stringBuffer.append("  ");
                stringBuffer.append(cursor.getString(cursor.getColumnIndex("time")));
                stringBuffer.append("////");

            }while (cursor.moveToNext());
            cursor.close();
        }
        return stringBuffer.toString();
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
