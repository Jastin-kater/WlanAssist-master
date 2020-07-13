package com.ronda.bluetoothassist.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.ronda.bluetoothassist.MainActivity;

import java.util.ArrayList;

import static com.ronda.bluetoothassist.MainActivity.getString_extra;

/**
 * Created by Administrator on 2018/7/1 0001.
 */


public class MChart extends View {
    private String TAG = "MChart";
    private ArrayList<Float> mMileages_V = new ArrayList<>();
    private ArrayList<Float> mMileages = new ArrayList<>();
    private ArrayList<Float> mTimes = new ArrayList<>();
    public ArrayList<Entry> mMileageDatas = new ArrayList<>();  //Voltage
    private ArrayList<String> mDates = new ArrayList<>();
    public ArrayList<Entry> mTimesDatas = new ArrayList<>();    //current;
    private ArrayList<Entry> powerDatas = new ArrayList<>();   //power
    //    private String[] dates = new String[]{"一","二","三","四","五","六","日"};
    private int mTotalWidth;
    private int mTotalHeight;
    private float rTextWidth;
    private float lTextWidth;
    private float xTextWidth;
    private float mXYpadding = 60;
    private float mPointRadius = 2f;
    private float mLinePadding = 10;
    public float line_x=10+rTextWidth+mXYpadding;
    private Paint mTimePaint = new Paint();
    private Paint mTimePaintdata = new Paint();
    private Paint mMileagePaint = new Paint();
    private Paint mMileagePaintdata = new Paint();
    private Paint mTextPaint = new Paint();
    private Paint mLinePaint = new Paint();
    private Paint mXpointPaint = new Paint();

    private Paint mredsetpaint =new Paint();
    private Paint mbluesetpaint =new Paint();
    private Paint mblacksetpaint =new Paint();
    private Path path = new Path();
    private int datanumb=0;
    private int mXcount;

    private boolean LineV = true;
    private boolean LineI = true;
    private boolean Lookflag = false;
    private String str_red_line;
    private String str_blue_line;
    public void setString(String st1red,String st2blue)
    {
        this.str_red_line = st1red;
        this.str_blue_line = st2blue;

    }
    @Override
    protected void onDraw(Canvas canvas) {

        //画坐标线
        canvas.drawLine(mXYpadding,mTotalHeight-mXYpadding,mTotalWidth-mXYpadding,mTotalHeight-mXYpadding,mLinePaint);
        canvas.drawLine(mXYpadding,mXYpadding,mXYpadding,mTotalHeight-mXYpadding,mredsetpaint);
        canvas.drawLine(mTotalWidth-mXYpadding,mXYpadding,mTotalWidth-mXYpadding,mTotalHeight-mXYpadding,mbluesetpaint);

//        canvas.drawCircle(2*mTotalWidth/3-200,mXYpadding/2-15,15,mbluesetpaint);
//        canvas.drawText(str_blue_line,(float)(2*mTotalWidth/3-130),mXYpadding/2,mTextPaint);
//
//        canvas.drawCircle((float)(2*mTotalWidth/3+70+2*xTextWidth),mXYpadding/2-15,15,mredsetpaint);
//        canvas.drawText(str_red_line,(float)(2*mTotalWidth/3+100+2*xTextWidth),mXYpadding/2,mTextPaint);
        canvas.drawCircle((mTotalWidth-2*mXYpadding)/2,mXYpadding/2-15,15,mbluesetpaint);
        canvas.drawText(str_blue_line,(float)(mTotalWidth-2*mXYpadding)/2+50,mXYpadding/2,mTextPaint);
        canvas.drawCircle(mXYpadding,mXYpadding/2-15,15,mredsetpaint);
        canvas.drawText(str_red_line,mXYpadding+50,mXYpadding/2,mTextPaint);

        //计算文字占的横向空间大小
         rTextWidth = mTextPaint.measureText("100");
         lTextWidth = mTextPaint.measureText("100");
         xTextWidth = mTextPaint.measureText("电");
        //画Y坐标数字（0、250、500）
        for(int i=0;i<mMileages.size();i++){
            canvas.drawText((int)(float)mMileages.get(i)+"",0
                    ,getMileageY(mMileages.get(i))
                    ,mredsetpaint);//画左边的  红色
            canvas.drawText((int)(float)mTimes.get(i)+""
                    ,mTotalWidth-rTextWidth,getTimeY(mTimes.get(i))
                    ,mbluesetpaint);//画右边的  蓝色
            //按里程划线  虚线
          //  canvas.drawLine(lTextWidth+mLinePadding,getMileageY(mMileages.get(i))-mTextPaint.getTextSize()/2
           //         ,mTotalWidth-rTextWidth-mLinePadding,getMileageY(mMileages.get(i))-mTextPaint.getTextSize()/2,mLinePaint);
            path.moveTo(lTextWidth+mLinePadding,getMileageY(mMileages.get(i))-mTextPaint.getTextSize()/2);
            path.lineTo(mTotalWidth-rTextWidth-mLinePadding,getMileageY(mMileages.get(i))-mTextPaint.getTextSize()/2);
            canvas.drawPath(path,mLinePaint);

        }
        //画X坐标显示文字
//        for(int i=0;i<mDates.size();i++){
//            canvas.drawText(mDates.get(i),getTextX(i)-xTextWidth/2,mTotalHeight,mTextPaint);
//            //canvas.drawCircle(getTextX(i),mTotalHeight-mXYpadding,mPointRadius,mXpointPaint);
//        }
        for(int i=0;i<mDates.size();i++){
            canvas.drawText(mDates.get(i),getTextX(i),mTotalHeight,mTextPaint);
            //canvas.drawCircle(getTextX(i),mTotalHeight-mXYpadding,mPointRadius,mXpointPaint);
        }


        if(LineV) {//判断是否需要显示V曲线，为true则显示//蓝色
            //画点，连线 里程 //画V曲线
            for (int i = 0; i < datanumb; i++) {
                canvas.drawCircle(getX(mMileageDatas.get(i).x), getTimeY(mMileageDatas.get(i).y)-18, mPointRadius, mMileagePaintdata);//画点
                if (i > 0) {
                    canvas.drawLine(getX(mMileageDatas.get(i - 1).x), getTimeY(mMileageDatas.get(i - 1).y)-18//连线
                            , getX(mMileageDatas.get(i).x), getTimeY(mMileageDatas.get(i).y)-18, mMileagePaintdata);
                }
            }
            //最后一段线
            //canvas.drawLine(getX(mMileageDatas.get(mMileageDatas.size() - 2).x), getMileageY(mMileageDatas.get(mMileageDatas.size() - 2).y)
             //       , getX(mMileageDatas.get(mMileageDatas.size() - 1).x), getMileageY(mMileageDatas.get(mMileageDatas.size() - 1).y), mMileagePaint);

        }
        if (LineI) {
            //画I曲线//红色
            for (int i = 0; i < datanumb; i++) {
                canvas.drawCircle(getX(mTimesDatas.get(i).x), getMileageY(mTimesDatas.get(i).y)-18, mPointRadius, mTimePaintdata);//画点
                if (i > 0) {
                    canvas.drawLine(getX(mTimesDatas.get(i - 1).x), getMileageY(mTimesDatas.get(i - 1).y)-18//连线
                            , getX(mTimesDatas.get(i).x), getMileageY(mTimesDatas.get(i).y)-18, mTimePaintdata);
                }
            }
            //最后一段线
          //  canvas.drawLine(getX(mTimesDatas.get(mTimesDatas.size() - 2).x), getMileageY(mTimesDatas.get(mTimesDatas.size() - 2).y)
            //        , getX(mTimesDatas.get(mTimesDatas.size() - 1).x), getMileageY(mTimesDatas.get(mTimesDatas.size() - 1).y), mTimePaint);

        }

        //画观察数值的竖线
        if(Lookflag)
        canvas.drawLine(line_x,0,line_x,mTotalHeight-mXYpadding,mXpointPaint);

//判断图线
        if("vol_cur".equals(getString_extra()))
        {
            //判断应该的划线
            if (MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN) {
                canvas.drawLine(mXYpadding, getRightY(60)-18, mTotalWidth - mXYpadding,  getRightY(60)-18, mTimePaint);
                canvas.drawLine(mXYpadding,  getRightY(100)-18, mTotalWidth - mXYpadding,  getRightY(100)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(24)-18, mTotalWidth - mXYpadding, getLeftY(24)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(34)-18, mTotalWidth - mXYpadding, getLeftY(34)-18, mMileagePaint);
            }
            if(MainActivity.getData_limit() == MainActivity.LIMIT_REHAN) {
                canvas.drawLine(mXYpadding, getRightY(170)-18, mTotalWidth - mXYpadding, getRightY(170)-18, mTimePaint);
                canvas.drawLine(mXYpadding, getRightY(240)-18, mTotalWidth - mXYpadding, getRightY(240)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(18)-18, mTotalWidth - mXYpadding,getLeftY(18)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(22)-18, mTotalWidth - mXYpadding, getLeftY(22)-18, mMileagePaint);
            }
            if(MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG) {
                canvas.drawLine(mXYpadding,  getRightY(180)-18, mTotalWidth - mXYpadding,  getRightY(180)-18, mTimePaint);
                canvas.drawLine(mXYpadding,  getRightY(270)-18, mTotalWidth - mXYpadding,  getRightY(270)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(18)-18, mTotalWidth - mXYpadding, getLeftY(18)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(22)-18, mTotalWidth - mXYpadding, getLeftY(22)-18, mMileagePaint);
            }
            if(MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN) {
                canvas.drawLine(mXYpadding,  getRightY(160)-18, mTotalWidth - mXYpadding,  getRightY(160)-18, mTimePaint);
                canvas.drawLine(mXYpadding,  getRightY(230)-18, mTotalWidth - mXYpadding,  getRightY(230)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(18)-18, mTotalWidth - mXYpadding, getLeftY(18)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(22)-18, mTotalWidth - mXYpadding, getLeftY(22)-18, mMileagePaint);
            }

        }
        else if("sudu_songsi".equals(getString_extra()))
        {
            if (MainActivity.getData_limit() == MainActivity.LIMIT_GENHAN) {
//                canvas.drawLine(mXYpadding, 60, mTotalWidth - mXYpadding, 60, mTimePaint);
//                canvas.drawLine(mXYpadding, 100, mTotalWidth - mXYpadding, 100, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(6)-18, mTotalWidth - mXYpadding,getLeftY(6)-18, mMileagePaint);
                canvas.drawLine(mXYpadding,getLeftY(12)-18, mTotalWidth - mXYpadding, getLeftY(12)-18, mMileagePaint);
            }
            if(MainActivity.getData_limit() == MainActivity.LIMIT_REHAN) {
                canvas.drawLine(mXYpadding,  getRightY(80)-18, mTotalWidth - mXYpadding,  getRightY(80)-18, mTimePaint);
                canvas.drawLine(mXYpadding,  getRightY(110)-18, mTotalWidth - mXYpadding,  getRightY(110)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(16)-18, mTotalWidth - mXYpadding, getLeftY(16)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(22)-18, mTotalWidth - mXYpadding,getLeftY( 22)-18, mMileagePaint);
            }
            if(MainActivity.getData_limit() == MainActivity.LIMIT_TIANCHONG) {
                canvas.drawLine(mXYpadding,  getRightY(90)-18, mTotalWidth - mXYpadding,  getRightY(90)-18, mTimePaint);
                canvas.drawLine(mXYpadding,  getRightY(110)-18, mTotalWidth - mXYpadding,  getRightY(110)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(16)-18, mTotalWidth - mXYpadding, getLeftY(16)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(22)-18, mTotalWidth - mXYpadding,getLeftY( 22)-18, mMileagePaint);
            }
            if(MainActivity.getData_limit() == MainActivity.LIMIT_GAIMIAN) {
                canvas.drawLine(mXYpadding,  getRightY(80)-18, mTotalWidth - mXYpadding,  getRightY(80)-18, mTimePaint);
                canvas.drawLine(mXYpadding,  getRightY(100)-18, mTotalWidth - mXYpadding,  getRightY(100)-18, mTimePaint);

                canvas.drawLine(mXYpadding, getLeftY(14)-18, mTotalWidth - mXYpadding, getLeftY(14)-18, mMileagePaint);
                canvas.drawLine(mXYpadding, getLeftY(20)-18, mTotalWidth - mXYpadding, getLeftY(20)-18, mMileagePaint);
            }
            }



//
//        //画点，连线 里程 for power
//        for(int i=0;i<powerDatas.size();i++){
//            canvas.drawCircle(getX(powerDatas.get(i).x),getMileageY(powerDatas.get(i).y),mPointRadius,mXpointPaint);
//
//            if(i>0){
//                canvas.drawLine(getX(powerDatas.get(i-1).x),getMileageY(powerDatas.get(i-1).y)
//                        ,getX(powerDatas.get(i).x),getMileageY(powerDatas.get(i).y),mXpointPaint);
//            }
//        }
//        //最后一段线
//        canvas.drawLine(getX(powerDatas.get(powerDatas.size()-2).x),getMileageY(powerDatas.get(powerDatas.size()-2).y)
//                ,getX(powerDatas.get(powerDatas.size()-1).x),getMileageY(powerDatas.get(powerDatas.size()-1).y),mXpointPaint);


//        //画点，连线 时间   for current
//        for(int i=0;i<mTimesDatas.size();i++){
//            canvas.drawCircle(getX(mTimesDatas.get(i).x),getTimeY(mTimesDatas.get(i).y),mPointRadius,mTimePaint);
//            if(i>0){
//                canvas.drawLine(getX(mTimesDatas.get(i-1).x),getTimeY(mTimesDatas.get(i-1).y)
//                        ,getX(mTimesDatas.get(i).x),getTimeY(mTimesDatas.get(i).y),mTimePaint);
//            }
//        }
//        //最后一段线
//        canvas.drawLine(getX(mTimesDatas.get(mTimesDatas.size()-2).x),getTimeY(mTimesDatas.get(mTimesDatas.size()-2).y)
//                ,getX(mTimesDatas.get(mTimesDatas.size()-1).x),getTimeY(mTimesDatas.get(mTimesDatas.size()-1).y),mTimePaint);
    }

    public float getX(float x){
        return (mTotalWidth - 2*mXYpadding)/(mXcount+1)*(x+1) + mXYpadding;
    }
//    private float getTextX(int x){
//        return (mTotalWidth - 2*mXYpadding)/(mDates.size()+1)*(x+1) + mXYpadding;
//    }
    private float getTextX(int x){
        return (mTotalWidth - 2*mXYpadding)/(mDates.size()-1)*(x) + mXYpadding/3;
    }
    private float getMileageY(float mile){
        float maxY = mMileages.get(mMileages.size()-1);

        return (1-mile/maxY)*(mTotalHeight-2*mXYpadding) + mXYpadding + mTextPaint.getStrokeWidth();
    }
    private float getTimeY(float time){
        float maxY = mTimes.get(mTimes.size()-1);

        return (1-time/maxY)*(mTotalHeight-2*mXYpadding) + mXYpadding + mTextPaint.getStrokeWidth();
    }
    private float getLeftY(float time){
        float maxY = mTimes.get(mTimes.size()-1);

        return (1-time/maxY)*(mTotalHeight-2*mXYpadding) + mXYpadding + mTextPaint.getStrokeWidth();
    }
    private float getRightY(float mile){
        float maxY = mMileages.get(mMileages.size()-1);

        return (1-mile/maxY)*(mTotalHeight-2*mXYpadding) + mXYpadding + mTextPaint.getStrokeWidth();
    }
    /**
     * 初始化
     */
    private void init(){

        mredsetpaint.setColor(Color.RED);
        mredsetpaint.setStrokeWidth(4);
        mredsetpaint.setTextSize(36);

        mbluesetpaint.setColor(Color.BLUE);
        mbluesetpaint.setStrokeWidth(4);
        mbluesetpaint.setTextSize(36);

        mblacksetpaint.setColor(Color.RED);
        mblacksetpaint.setStrokeWidth(2);

        mMileagePaint.setColor(Color.BLUE);
        mMileagePaint.setStrokeWidth(4);

        mMileagePaintdata.setColor(Color.BLUE);
        mMileagePaintdata.setStrokeWidth(2);

        mTimePaint.setColor(Color.RED);
        mTimePaint.setStrokeWidth(4);

        mTimePaintdata.setColor(Color.RED);
        mTimePaintdata.setStrokeWidth(2);


        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(5);
        mTextPaint.setTextSize(36);

        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));

        mXpointPaint.setColor(Color.GREEN);
        mXpointPaint.setStrokeWidth(3);
    }
    public MChart(Context context) {
        super(context);
        init();
    }

    public MChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void setLookFlag(boolean lookflag){ this.Lookflag = lookflag;}
    public void setLineIstatus(boolean linei){this.LineI = linei;}
    public void setLineVstatus(boolean linev){this.LineV = linev;}
    public void setDatanum(int datanum){this.datanumb = datanum;}
    public void setTextPaint(Paint paint){
        mTextPaint = paint;
    }
    public void setMileagePaint(Paint paint){
        mMileagePaint = paint;
    }
    public void setTimePaint(Paint paint){
        mTimePaint = paint;
    }
    public void setLintPaint(Paint paint){
        mLinePaint = paint;
    }

    /**
     * 设置里程纵坐标
     * 划线的条数根据list.size(),如果需要每条线之间距离相等,需要将最大值平分成几分设置到list中
     *
     */
    public void setMileageList(ArrayList<Float> list){
        mMileages = list;
    }


    /*
    设置移动竖线时X的横坐标
    */
    public void set_X(float x)
    {
        this.line_x = x;



    }

    /**
     * 设置时间纵坐标
     */
    public void setTimeList(ArrayList<Float> list){
        mTimes = list;
    }

    /**
     * 设置里程数据
     */
    public void setMileageDataList(ArrayList<Entry> list){
        mMileageDatas = list;
    }

    /**
     * 设置时间数据
     */
    public void setTimeDataList(ArrayList<Entry> list){
        mTimesDatas = list;
    }

    public void setPowerDataList(ArrayList<Entry> list){
        powerDatas = list;
    }
    /**
     * 设置X轴显示时间，设置需要显示的最大的天数
     * @param list
     * @param count 最大天数
     */
    public void setDatesList(ArrayList<String> list,int count){
        mDates = list;
        mXcount = count;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTotalWidth = getWidth();
        mTotalHeight = getHeight();
    }
}

