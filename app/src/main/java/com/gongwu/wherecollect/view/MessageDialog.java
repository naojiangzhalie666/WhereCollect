package com.gongwu.wherecollect.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mucll on 2018/3/15.
 */

public class MessageDialog extends Dialog {

    @BindView(R.id.dialog_msg_tv)
    TextView msgTv;

    private Context context;

    public MessageDialog(@NonNull Context context) {
        this(context, R.style.Transparent2);
    }

    public MessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void setMessage(int strId) {
        if (msgTv != null) {
            msgTv.setText(context.getString(strId));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message_layout);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.msg_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.msg_submit_tv:
                submit();
                dismiss();
                break;
        }
    }

    public void show() {
        super.show();
    }

    public void submit() {

    }

}
