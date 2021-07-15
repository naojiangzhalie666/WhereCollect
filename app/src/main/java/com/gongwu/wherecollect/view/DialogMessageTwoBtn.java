package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.gongwu.wherecollect.ImageSelect.ImageGridActivity;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.CameraMainActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhaojin on 15/11/16.
 */
public class DialogMessageTwoBtn {
    Activity mContext;

    public DialogMessageTwoBtn(Activity context, String msg, String type) {
        this.mContext = context;


        final Dialog dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.dialog_msg_two_btn_layout, null);
        ((TextView) view.findViewById(R.id.dialog_msg_tv)).setText(msg);
        view.findViewById(R.id.msg_submit_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(type);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.msg_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            //设置弹出位置
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();
    }

    public void submit(String type) {

    }
}
