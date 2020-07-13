package ren.define.circlepictext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ronda.bluetoothassist.R;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 *
 *类描述：
 *创建人：R
 *创建时间：${DATA}16:06
 *
 */public class CirclePicText extends RelativeLayout {
     private TextView pictextView;
     private CircleImageView piccircleImageView;

    public CirclePicText(Context context) {
        super(context);
    }

    public CirclePicText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.imag_text_bt,this,true);
        //当attachtoroot是true时，会把加载的layout布局返回的view控件加入到this，并且返回root布局。
        // 如果当attachtoroot是false时，则会直接返回layout，不将view添加到this中，但是layout的参数会起作用   测试结果：attach变成false以后程序无法启动
        //如果root传入的是null，则最后得到的layout的layout_width  layout_height属性不会起作用
        piccircleImageView =(CircleImageView)findViewById(R.id.image_pic_view);
        pictextView = (TextView)findViewById(R.id.image_text);

        Log.i("circle","createcircle");
        setClickable(true);//layout默认是不可点击的   测试成功:监听器可以将控件变成可点击的
        setFocusable(true);

    }


    public void setText_Image(int pic, String name, String color)
    {
        pictextView.setText(name);
        piccircleImageView.setImageResource(pic);
        setImageBackgroundColor(piccircleImageView,color);

    }

    private void setImageBackgroundColor(CircleImageView piccircleImageView, String color) {

        if(piccircleImageView ==null)
            return;

        GradientDrawable gradientDrawable = (GradientDrawable)piccircleImageView.getBackground();
        gradientDrawable.setColor(Color.parseColor(color));


    }



}
