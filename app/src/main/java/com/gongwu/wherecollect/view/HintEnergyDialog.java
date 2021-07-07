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

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.net.Config;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mucll on 2018/3/15.
 */

public class HintEnergyDialog {

    private Context mContext;
    private View view;

    public HintEnergyDialog(Activity context, boolean isVip) {
        mContext = context;
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        //2、设置布局
        view = View.inflate(context, R.layout.dialog_hint_energy_layout, null);
        view.findViewById(R.id.energy_buy_vip_tv).setVisibility(isVip ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.hint_start_energy_act_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mContext, Config.WEB_ENERGY_NAME, Config.WEB_ENERGY_URL, 50);
            }
        });
        view.findViewById(R.id.energy_buy_vip_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyVIPActivity.start(mContext);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        dialog.show();

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }
}
