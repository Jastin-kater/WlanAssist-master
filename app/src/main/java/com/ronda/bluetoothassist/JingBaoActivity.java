package com.ronda.bluetoothassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class JingBaoActivity extends AppCompatActivity {

    private List<Jingbao_data> datalist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jing_bao);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton_jingbao);
        initdata();
        RecyclerView recyclerView = findViewById(R.id.jingbao_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        JingbaoAdapter ja =new JingbaoAdapter(datalist);
        ja.setContext(JingBaoActivity.this);
        recyclerView.setAdapter(ja);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initdata()
    {
        Jingbao_data dianya = new Jingbao_data("电压超限","",R.mipmap.jingbao0,R.mipmap.next);
        datalist.add(dianya);

        Jingbao_data dianliu = new Jingbao_data("电流超限","",R.mipmap.jingbao1,R.mipmap.next);
        datalist.add(dianliu);

        Jingbao_data songsisudu = new Jingbao_data("送丝速度超限","",R.mipmap.jingbao2,R.mipmap.next);
        datalist.add(songsisudu);

        Jingbao_data hanjiesudu = new Jingbao_data("焊接速度超限","",R.mipmap.jingbao3,R.mipmap.next);
        datalist.add(hanjiesudu);

        Jingbao_data huanjingwendu = new Jingbao_data("环境温度超限","",R.mipmap.jingbao4,R.mipmap.next);
        datalist.add(huanjingwendu);

        Jingbao_data huanjingshidu = new Jingbao_data("环境湿度超限","",R.mipmap.jingbao5,R.mipmap.next);
        datalist.add(huanjingshidu);

        Jingbao_data hancengwendu = new Jingbao_data("焊层温度超限"," ",R.mipmap.jingbao6,R.mipmap.next);
        datalist.add(hancengwendu);

    }
}
