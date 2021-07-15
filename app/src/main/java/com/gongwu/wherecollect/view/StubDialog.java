package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.Config;

/**
 * Created by mucll on 2018/3/15.
 */

public class StubDialog {

    private View view;

    public StubDialog(Activity context, int textId) {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context,
                R.style.Transparent3);
        dialog.setCanceledOnTouchOutside(true);
        //2、设置布局
        view = View.inflate(context, R.layout.dialog_hint_stub_layout, null);
        TextView textView = view.findViewById(R.id.dialog_stub_text_tv);
        textView.setText(textId);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            //设置弹出位置
            window.setGravity(Gravity.BOTTOM);
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onDismissDialog();
            }
        });
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    public void onDismissDialog() {

    }
}
