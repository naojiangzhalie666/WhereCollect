package com.gongwu.wherecollect.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongwu.wherecollect.R;

/********************************************************************
 *******************************************************************/
public class Loading extends Dialog {
    private String msg = "";

    private Loading(Context context) {
        super(context);
    }

    private Loading(Context context, int theme) {
        super(context, theme);
    }

    private static Loading createDialog(Context context) {
        Loading loading = new Loading(context, R.style.CustomProgressDialog);
        loading.setContentView(R.layout.loading_layout);
        loading.getWindow().getAttributes().gravity = Gravity.CENTER;
        return loading;
    }

    public static Loading show(Loading loading, Context context, String text) {
        if (loading == null) {
            loading = Loading.createDialog(context);
        }
        loading.setMessage(text);
        loading.setCancelable(true);
        loading.setCanceledOnTouchOutside(false);
        try {
            loading.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loading;
    }

    public static Loading show(Loading loading, Context context) {
        if (loading == null) {
            loading = Loading.createDialog(context);
        }
        loading.setMessage(context.getString(R.string.loading_text));
        try {
            loading.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loading;
    }

    public String getMsg() {
        return msg;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    /**
     * [Summary]
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public Loading setMessage(String strMessage) {
        TextView tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);
        this.msg = strMessage;
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return this;
    }
}
