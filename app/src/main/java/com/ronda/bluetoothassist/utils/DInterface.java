package com.ronda.bluetoothassist.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.ronda.bluetoothassist.WifiService;

import java.io.IOException;

public class DInterface {//将AlertDialog进行重新封装，防止多次弹窗，跟Toast类似


        private static AlertDialog ad;

        public static void ShowDialog(Context context, String st1, String st2, Drawable drawable, final com.ronda.bluetoothassist.DealFun ws) {

            if (ad == null) {
                ad = new AlertDialog.Builder(context).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                          ws.getDFIn().close();
                           ws.getDFSocket().close();
                            ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    }
                }).setTitle(st1).setIcon(drawable).setMessage(st2).create();
                ad.show();
                ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
            }
            else
            {

                ad.setTitle(st1);
                ad.setIcon(drawable);
                ad.setButton(AlertDialog.BUTTON_POSITIVE, st2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        try {
                            ws.getDFIn().close();
                            ws.getDFSocket().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                ad.setButton(AlertDialog.BUTTON_NEGATIVE, st2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad.show();


            }

        }


}
