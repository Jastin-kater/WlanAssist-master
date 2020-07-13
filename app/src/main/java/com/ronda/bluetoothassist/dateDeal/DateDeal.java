package com.ronda.bluetoothassist.dateDeal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeal {

    public static long stringToLong(String st_time,String st_time_format)
    {   long dateToSecond = 0;

        try{
        SimpleDateFormat sdf = new SimpleDateFormat(st_time_format);
        //sdf.parse()实现日期转换为Date格式，然后getTime()转换为毫秒数值
        dateToSecond = sdf.parse(st_time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToSecond;
    }

    public static String longToString(long time_ms,String string)
    {
        Date date = new Date(time_ms);
        SimpleDateFormat sdf = new SimpleDateFormat(string);
        String str = sdf.format(date);
        return  str;
    }

}
