package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.net.Config;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputPasswordDialog {

    private Context mContext;
    private View view;

    public InputPasswordDialog(Activity context, String hintET, String helpTips, String urlTitle, String urlPath) {
        mContext = context;
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        //2、设置布局
        view = View.inflate(context, R.layout.dialog_input_password_layout, null);

        EditText mEditText = view.findViewById(R.id.input_edit_et);
        mEditText.setHint(hintET);
        TextView helpTipsTv = view.findViewById(R.id.help_tips_tv);
        helpTipsTv.setText(helpTips);
        helpTipsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.start(mContext, urlTitle, urlPath, 50);
            }
        });
        view.findViewById(R.id.input_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.input_submit_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result(mEditText.getText().toString().trim());
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        dialog.show();

//        WindowManager windowManager = ((Activity) context).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth()); //设置宽度
//        dialog.getWindow().setAttributes(lp);
    }

    public void result(String result) {

    }
}
