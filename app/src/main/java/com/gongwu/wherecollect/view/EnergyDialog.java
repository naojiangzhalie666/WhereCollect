package com.gongwu.wherecollect.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.util.PhotosDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mucll on 2018/3/15.
 */

public class EnergyDialog extends Dialog {

    private Context mContext;

    public EnergyDialog(@NonNull Context context) {
        this(context, R.style.Transparent2);
    }

    public EnergyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_energy_layout);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getUserInfo();
            }
        });
    }

    @OnClick({R.id.energy_i_know})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.energy_i_know:
                dismiss();
                break;
        }
    }


    public void show() {
        super.show();
    }

    public void getUserInfo() {

    }
}
