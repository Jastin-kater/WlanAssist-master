package com.ronda.bluetoothassist.linesets;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;

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
import com.ronda.bluetoothassist.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MyChatActivity extends AppCompatActivity {


    LineChart lc;

    List<MyBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychart);
        lc = findViewById(R.id.lc);
        initData();
        initChart();



    }

    private void initData() {
        list = new ArrayList<>();
        // list.add(new MyBean("0",8f));
        list.add(new MyBean("3/25", 3.8f));
        list.add(new MyBean("3/24", 6.8f));
        list.add(new MyBean("3/23", 7.8f));
        list.add(new MyBean("3/22", 5.4f));
        list.add(new MyBean("3/21", 0f));
        list.add(new MyBean("3/20", 6f));
        list.add(new MyBean("3/19", 9f));
        list.add(new MyBean("3/25", 3.8f));
        list.add(new MyBean("3/24", 6.8f));
        list.add(new MyBean("3/23", 7.8f));
        list.add(new MyBean("3/22", 5.4f));
        list.add(new MyBean("3/21", 0f));
        list.add(new MyBean("3/20", 6f));
        list.add(new MyBean("3/19", 9f));
        list.add(new MyBean("3/25", 3.8f));
        list.add(new MyBean("3/24", 6.8f));
        list.add(new MyBean("3/23", 7.8f));
        list.add(new MyBean("3/22", 5.4f));
        list.add(new MyBean("3/21", 0f));
        list.add(new MyBean("3/20", 6f));
        list.add(new MyBean("3/19", 9f));
    }

    private void initChart() {

        List<Float> m = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            float t = list.get(i).getF();
            m.add(t);
        }

        List<Entry> entries = new ArrayList<>();
        Float mMin = m.get(0);
        Float mMax = m.get(0);
        for (int i = 0; i < m.size(); i++) {
            if (mMin > m.get(i)) {
                mMin = m.get(i);
            }
            if (mMax < m.get(i)) {
                mMax = m.get(i);
            }
            entries.add(new Entry(i, m.get(i)));

        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Log.e("TAG", "----->getFormattedValue: " + value);
                int v = (int) value;
                if (v <= list.size() && v >= 0) {
                    String st = list.get(v).getS();
                    String tim1 = "";
                    tim1 = st;
                    return tim1;
                } else {
                    return null;
                }

            }

        };

        List<String> list_time = new ArrayList<>();
        for (MyBean sleepModel : list) {
            String st = sleepModel.getS();

            list_time.add(st);

        }

        lc.setTouchEnabled(true);//可接触
        lc.setDragEnabled(false);//可拖拽
        lc.setScaleEnabled(false);//可缩放
        lc.setDoubleTapToZoomEnabled(false);
        lc.setScaleYEnabled(false);
        lc.setDrawGridBackground(false);//画网格背景
        lc.setDrawBorders(false);  //是否在折线图上添加边框
        lc.setPinchZoom(false);//设置少量移动

        //图表注解
        lc.getLegend().setEnabled(false);
        lc.getDescription().setEnabled(false);
        //x轴坐标
        XAxis xAxis = lc.getXAxis();
        xAxis.setTextColor(Color.BLACK);//x轴文字颜色
        xAxis.setTextSize(12f);

        xAxis.setEnabled(true);//显示x轴

        xAxis.setGridLineWidth(0f);
        xAxis.setLabelCount(7, false);

        xAxis.setDrawAxisLine(true);//是否绘制轴线

        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置

        xAxis.setGranularity(1);

        xAxis.setGridLineWidth(0.5f);
        xAxis.setGridColor(Color.parseColor("#9E9E9E"));

        xAxis.setValueFormatter(formatter);

        LimitLine ll2 = new LimitLine(8f, "");
        ll2.setLineWidth(3f);
        ll2.setLineColor(ContextCompat.getColor(this, android.R.color.black));
        ll2.enableDashedLine(5f, 5f, 0f);//虚线
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);//设置标签显示的位置
        ll2.setTextColor(ContextCompat.getColor(this, android.R.color.background_dark));
        ll2.setTextSize(10f);


        //Y轴坐标
        YAxis yAxis = lc.getAxisLeft();


        yAxis.setAxisMinValue(0);
        yAxis.setAxisMaxValue(12f);

//        if (isf) {
//            yAxis.setAxisMinValue(80f);
//            yAxis.setAxisMaxValue(108f);
//        } else {
//            yAxis.setAxisMinValue(25f);//设置y轴最小值
//            yAxis.setAxisMaxValue(42f);//设置y轴最大值
//        }
//        yAxis.setSpaceTop(5);//设置y轴的顶部间隔
//        yAxis.setSpaceBottom(5);//设置y轴的底部间隔
        //yAxis.setDrawGridLines(false);//设置网格线
        yAxis.setDrawGridLines(false);//取消网格线
        yAxis.setEnabled(true);//不显示Y轴
        yAxis.addLimitLine(ll2);
        yAxis.setLabelCount(7, true);
        //   yAxis.setGridColor(Color.GRAY);
        lc.getAxisRight().setEnabled(false);//不显示右侧


        LineDataSet lineDataSet = new LineDataSet(entries, "");
       /* lineDataSet.setLineWidth(1.6f);
        lineDataSet.setDrawCircles(false);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(Color.parseColor("#651fff"));
        lineDataSet.setDrawFilled(true);*/
        // Drawable drawable = getResources().getDrawable(R.drawable.shape_change_line);
        //lineDataSet.setFillDrawable(drawable);


        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置折线图的显示模式，可以自行设置上面的值进行查看不同之处
        lineDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));//设置线的颜色
        lineDataSet.setLineWidth(1.5f);//设置线的宽度
        lineDataSet.setCircleColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));//设置圆圈的颜色
        lineDataSet.setCircleColorHole(ContextCompat.getColor(this, android.R.color.holo_blue_light));//设置圆圈内部洞的颜色
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);//设置线数据依赖于左侧y轴
        lineDataSet.setDrawFilled(true);//设置不画数据覆盖的阴影层
        lineDataSet.setDrawValues(true);//不绘制线的数据
        // lineDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.text_color));//设置数据的文本颜色，如果不绘制线的数据 这句代码也不用设置了
        lineDataSet.setValueTextSize(15f);//如果不绘制线的数据 这句代码也不用设置了
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//没看出来效果
        lineDataSet.setFormLineWidth(10f);//只有lineDataSet.setForm(Legend.LegendForm.LINE);时才有作用 这里我们设置的是圆所以这句代码直接注释
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));//设置虚线，只有lineDataSet.setForm(Legend.LegendForm.LINE);时才有作用
        lineDataSet.setCircleRadius(4f);//设置每个折线点的大小
        lineDataSet.setFormSize(15.f);//设置当前这条线的图例的大小
        lineDataSet.setForm(Legend.LegendForm.LINE);//设置图例显示为线


        LineData data = new LineData(lineDataSet);//创建图表数据源

        lc.setData(data);//设置图表数据

    }
}
