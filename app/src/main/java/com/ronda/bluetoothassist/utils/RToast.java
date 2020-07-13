package com.ronda.bluetoothassist.utils;

import android.content.Context;
import android.widget.Toast;

import com.ronda.bluetoothassist.R;

public class RToast {//将Toast方法重新封装了一遍，防止出现多次弹窗的状况
    private static Toast toast;

    public static void showToast(Context context,String string,int time){

        if(toast == null)
        {
            toast = Toast.makeText(context, string,time);
            toast.show();

        }
        else {
            toast.setText(string);
            toast.setDuration(time);
        }


    }


}
