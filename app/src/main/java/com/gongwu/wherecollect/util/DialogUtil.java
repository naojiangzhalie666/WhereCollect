package com.gongwu.wherecollect.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.gongwu.wherecollect.base.BaseActivity;


/**
 * Created by zhaojin on 2016/4/6.
 */
public class DialogUtil {
    public static AlertDialog show(String title, String msg, String button, Activity context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .setMessage(msg)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        if (BaseActivity.getScreenWidth(context) < BaseActivity.getScreenHeigth(context)) {
            params.width = (int) (BaseActivity.getScreenWidth(context) * 6.0f / 7.0f);
        }
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    /**
     * @param title     标题
     * @param msg       消息
     * @param okStr     确认按钮文字
     * @param cancalStr 取消按钮文字
     * @param activity
     * @param listener1 确认监听
     * @param listener2 取消监听
     * @return
     */
    public static AlertDialog show(String title, String msg, String okStr, String cancalStr,
                                   Activity activity, DialogInterface.OnClickListener listener1,
                                   DialogInterface.OnClickListener listener2) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setPositiveButton(okStr, listener1)
                .setNegativeButton(cancalStr, listener2)
                .setMessage(msg)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        if (BaseActivity.getScreenWidth(activity) < BaseActivity.getScreenHeigth(activity)) {
            params.width = (int) (BaseActivity.getScreenWidth(activity) * 6.0f / 7.0f);
        }
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    /**
     * @param title     标题
     * @param msg       消息
     * @param okStr     确认按钮文字
     * @param cancalStr 取消按钮文字
     * @param context
     * @param listener1 确认监听
     * @param listener2 取消监听
     * @return
     */
    public static AlertDialog showMsg(String title, String msg, String okStr, String cancalStr,
                                      Activity context, DialogInterface.OnClickListener listener1,
                                      DialogInterface.OnClickListener listener2) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(okStr, listener1)
                .setNegativeButton(cancalStr, listener2)
                .setMessage(msg)
                .create();
        // 加入系统服务
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0
//                    mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//                } else {
//                    mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                }
        //8.0系统加强后台管理，禁止在其他应用和窗口弹提醒弹窗，如果要弹，必须使用TYPE_APPLICATION_OVERLAY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        } else {
            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }
        dialog.setCancelable(false);
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        if (BaseActivity.getScreenWidth(context) < BaseActivity.getScreenHeigth(context)) {
            params.width = (int) (BaseActivity.getScreenWidth(context) * 6.0f / 7.0f);
        }
        dialog.getWindow().setAttributes(params);
        return dialog;
    }
}


