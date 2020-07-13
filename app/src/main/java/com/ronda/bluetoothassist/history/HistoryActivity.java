package com.ronda.bluetoothassist.history;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ronda.bluetoothassist.MainActivity;
import com.ronda.bluetoothassist.R;
import com.ronda.bluetoothassist.dateDeal.DateDeal;
import com.ronda.bluetoothassist.linesets.LineChartManager;
import com.ronda.bluetoothassist.linesets.LineChatUtil;
import com.ronda.bluetoothassist.linesets.MyBean;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private LineChart lineChart1;
    private LineChartManager lineChartManager1;
    private TextView query_date;
    private TextView query_time;
    private Spinner myspinner;
    private Spinner myWeldSpinner;
    private Button button_query;
    private ArrayList typeList;
    private ArrayList weldTypeList;
    private ArrayAdapter arrayAdapter;
    private ArrayAdapter weldTypeAdapter;
    private long dateTime = DateDeal.stringToLong("2019-11-5 9:24:30","yyyy-MM-dd HH:mm:ss");
    private String type = "weld_vol";
    private int weldType = MainActivity.LIMIT_BUXIANSHI;
    private int updateNum;
    private LineChatUtil lineChatUtil;
    private float linex;
    private float line_x;
    private static int y = 2019;
    private static int m = 10;
    private static int d = 29;
    private static int h = 10;
    private static int mu = 0;
    private FloatingActionButton floatingActionButton;
    private boolean isPress = false;
    private StringBuffer stringBufferTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lineChart1 = findViewById(R.id.line_chart1);
        lineChatUtil = new LineChatUtil(HistoryActivity.this,lineChart1);
        query_date = findViewById(R.id.query_date);
        query_time = findViewById(R.id.query_time);
        myspinner = findViewById(R.id.type_spinner);
        myWeldSpinner = findViewById(R.id.hanjie_spinner);
        button_query = findViewById(R.id.button_query);
        floatingActionButton =findViewById(R.id.floatingActionButton_history);
        stringBufferTime = new StringBuffer();
        query_date.setText(y+"-"+m+"-"+d);
        query_time.setText(h+":"+mu);
        typeList = new ArrayList();
        typeList.add("电压");
        typeList.add("电流");
        typeList.add("送丝速度");
        typeList.add("焊接速度");
        typeList.add("环境温度");
        typeList.add("环境湿度");
        typeList.add("焊层温度");
        arrayAdapter = new ArrayAdapter(HistoryActivity.this,R.layout.simple_item_spinner,typeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(arrayAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myspinner.setDropDownVerticalOffset(75);
        }

        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    type = "weld_vol";
                }
                else if(position == 1)
                { type = "weld_cur";}
                else if(position == 2)
                {type = "speed_songsi";}
                else if(position == 3)
                {type = "speed_weld";}
                else if(position == 4)
                {type = "envir_temp";}
                else if(position == 5)
                {type = "envir_humi";}
                else if(position == 6)
                {type = "welding_layer_temp";}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        weldTypeList = new ArrayList();
        weldTypeList.add("根焊");
        weldTypeList.add("热焊");
        weldTypeList.add("填充");
        weldTypeList.add("盖面");
        weldTypeList.add("不显示");
        weldTypeAdapter = new ArrayAdapter(HistoryActivity.this,R.layout.simple_item_spinner,weldTypeList);
        weldTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myWeldSpinner.setAdapter(weldTypeAdapter);
        myWeldSpinner.setSelection(4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myWeldSpinner.setDropDownVerticalOffset(75);
        }
        myWeldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {weldType = MainActivity.LIMIT_GENHAN;}
                else if(position == 1)
                {weldType = MainActivity.LIMIT_REHAN;}
                else if(position == 2)
                {weldType = MainActivity.LIMIT_TIANCHONG;}
                else if(position == 3)
                {weldType = MainActivity.LIMIT_GAIMIAN;}
                else if(position == 4)
                {weldType = MainActivity.LIMIT_BUXIANSHI;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        query_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = getLayoutInflater().inflate(R.layout.datepicker_layout,null);
                DatePicker datePicker = view.findViewById(R.id.dp_datetimepicker_date);
                datePicker.init(2019, 10, 29, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        y = year; m = monthOfYear; d = dayOfMonth;
                    }
                });
                AlertDialog.Builder ab = new AlertDialog.Builder(HistoryActivity.this);
                ab.setView(view);
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        query_date.setText(y+"-"+(m+1)+"-"+d);

                    }
                });
                ab.setNegativeButton("取消",null);
                ab.create().show();





            }
        });
        query_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View viewtime = getLayoutInflater().inflate(R.layout.timepicker,null);
                TimePicker timePicker = viewtime.findViewById(R.id.tp_datetimepicker_time);
                timePicker.setIs24HourView(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    timePicker.setMinute(0);
                    timePicker.setHour(10);
                }

                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        h = hourOfDay; mu = minute;

                    }


                });

                AlertDialog.Builder ab = new AlertDialog.Builder(HistoryActivity.this);
                ab.setView(viewtime);
                ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        query_time.setText(h+":"+mu);
                    }
                });
                ab.setNegativeButton("取消",null);
                ab.create().show();
            }
        });

        button_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                finish();
            }
        });

        button_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringStart = "";
                String stringEnd = "";
                stringStart = stringBufferTime.append(y+"-"+(m+1)+"-"+d+" "+h+":"+mu+":"+0).toString();
                stringEnd = stringBufferTime.append(y+"-"+(m+1)+"-"+d+" "+h+":"+mu+":"+59).toString();
                

            }
        });
    lineChart1.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    linex = event.getX();
                    isPress = true;
                    break;

                    case MotionEvent.ACTION_MOVE:
                     line_x = event.getX();
                    break;
                    case MotionEvent.ACTION_UP:
                        isPress = false;
                    break;
            }
            if(!isPress)
            {
                updateNum = (int) ((line_x - linex)/40);
                if(updateNum > 20)
                    updateNum = 20;
                Toast.makeText(HistoryActivity.this,"T:"+updateNum,Toast.LENGTH_LONG).show();
            }



            return true;
        }
    });
        LineChatUtil.initData();
        lineChatUtil.initChart();
    }



}
