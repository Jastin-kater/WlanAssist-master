package com.ronda.bluetoothassist;

import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class IPChangeActivity extends AppCompatActivity {
    private Button button_setIP;
    private Button button_cancelSetIP;
    private Button button_connect;
    private Button button_disconnect;
    private TextView showIP;
    private TextView showport;
    private EditText editText_IP;
    private EditText editText_PORT;
    private FloatingActionButton floatingActionButton;
    private String regexip = "\\d{3}.\\d{3}.\\d{1,3}.\\d{1,3}";
    private String regexport = "\\d{1,5}";
    private static String Data_ip = "118.112.183.180";
    private static String Data_port = "502";
    private WifiService wifiService;
    private PopupWindow mpopupwindow;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        if(getSupportActionBar() != null)
//            getSupportActionBar().hide();
        setContentView(R.layout.activity_ipchange);
        //顶部导航栏
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);//HomeAsUp是左上部的导航栏
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        editor =  getSharedPreferences("ipdata",MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("ipdata",MODE_PRIVATE);
        button_setIP = (Button) findViewById(R.id.button_setIP);
        button_cancelSetIP = (Button)findViewById(R.id.button_cancelSetIP);
        editText_IP = (EditText)findViewById(R.id.edit_ip);
        editText_PORT =(EditText)findViewById(R.id.edit_port);
        //显示ip和port的两个textview
        showIP = (TextView)findViewById(R.id.ip);
        showport = (TextView)findViewById(R.id.port);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton_ip);
        //获得焦点后弹窗
        editText_IP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b)
                    setIpList() ;

            }
        });


        button_setIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Data_ip = editText_IP.getText().toString();
                Data_port = editText_PORT.getText().toString();
                if(!Data_ip.matches(regexip))
                {
                    Toast.makeText(IPChangeActivity.this,"ip地址错误",Toast.LENGTH_LONG).show();}

                if(!Data_port.matches(regexport))
                {
                    Toast.makeText(IPChangeActivity.this,"端口错误",Toast.LENGTH_LONG).show();}
                if(Data_ip.matches(regexip)&&Data_port.matches(regexport))
                {
                    showIP.setText(Data_ip);
                    showport.setText(Data_port);
                    //完成设置后关闭页面
//                    Intent intent_to_mainactivity = new Intent(IPChangeActivity.this,NavPageActivity.class);
//                    startActivity(intent_to_mainactivity);
                    saveIpData(Data_ip,Data_port,editor,sharedPreferences);
                    finish();
                }

            }
        });
        button_cancelSetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        showIP.setText(Data_ip);
        showport.setText(Data_port);
    }

    public void setIpList(){
        boolean show_flag = false;
        View contentView = LayoutInflater.from(this).inflate(R.layout.ip_list,null);
        final ListView lv = (ListView) contentView.findViewById(R.id.iplist);
        show_flag = setIpListData(lv,sharedPreferences);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickdata = (String)lv.getItemAtPosition(position);
                editText_IP.setText(clickdata.split("\\|",2)[0]);
                editText_PORT.setText(clickdata.split("\\|",2)[1]);

            }
        });
        mpopupwindow = new PopupWindow(contentView,editText_IP.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        mpopupwindow.setOutsideTouchable(true);
        mpopupwindow.setTouchable(true);
        mpopupwindow.setBackgroundDrawable(new BitmapDrawable());

        if(show_flag)
        mpopupwindow.showAsDropDown(editText_IP);
    }


    public void saveIpData(String ip,String port,SharedPreferences.Editor editor,SharedPreferences sp) {
        int i;
        int point;

        if (".".equals(sp.getString("point", "."))) {
            editor.putString("point", "0");
            editor.putString("ip_port" + 0, ip + "|" + port);
            editor.apply();
        } else {
            i = Integer.parseInt(sp.getString("point", "."));
            if (i > 8)
                i = 0;
            else
                i++;

            editor.remove("point");
            editor.putString("point", "" + i);
            editor.remove("ip_port" + i);
            editor.putString("ip_port" + i, ip + "|" + port);
            editor.apply();
        }


    }

    public boolean setIpListData(ListView lv,SharedPreferences sp) {
        int i;

        if (".".equals(sp.getString("point", ".")))
            return false;
        else {
            for (i = 0; i < 10; i++) {

                if (".".equals(sp.getString("ip_port" + i, ".")))
                    break;

            }
            String[] st = new String[i];

            for (int j = 0; j < i; j++) {
                st[j] = sp.getString("ip_port" + j, ".");
            }

            ArrayAdapter<String> aa = new ArrayAdapter<String>(IPChangeActivity.this, R.layout.layout_child_list, st);
            ;
            lv.setAdapter(aa);
            return true;

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static String getdata_ip()
    {
        return Data_ip;

    }
    public static String getdata_port()
    {
        return Data_port;
    }
}
