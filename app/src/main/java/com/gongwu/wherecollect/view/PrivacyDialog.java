package com.gongwu.wherecollect.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.SplashActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.net.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mucll on 2018/3/15.
 */

public class PrivacyDialog extends Dialog {

    @BindView(R.id.privacy_msg_tv)
    TextView msgTv;

    private Context mContext;

    public PrivacyDialog(@NonNull Context context) {
        this(context, R.style.Transparent2);
    }

    public PrivacyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public void setMessage(int strId) {
        if (msgTv != null) {
            msgTv.setText(mContext.getString(strId));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_privacy_layout);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        getClickableSpan(msgTv);
    }

    @OnClick({R.id.privacy_submit_tv, R.id.privacy_cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.privacy_submit_tv:
                submit();
                dismiss();
                break;
            case R.id.privacy_cancel_tv:
                cancel();
                dismiss();
                break;
        }
    }

    public void show() {
        super.show();
    }

    public void submit() {

    }

    public void cancel() {

    }

    private void getClickableSpan(TextView textView) {
        String a = "欢迎使用收哪儿！我们非常重视你的隐私和个人信息保护。在你使用收哪儿之前，请认真阅读";
        String b = "和";
        String c = "，你同意并接受全部条款后方可开始使用收哪儿。此外，在未经用户允许情况下，收哪儿不会主动将用户敏感信息泄露给任何第三方。";
        String agreement = "《服务使用协议》";
        SpannableString spStr = new SpannableString(agreement);

        ClickableSpan clickSpan = new CustomizedClickableSpan(agreement);
        spStr.setSpan(clickSpan, 0, agreement.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        String privacy = "《隐私政策》";
        SpannableString spStr1 = new SpannableString(privacy);
        ClickableSpan clickSpan1 = new CustomizedClickableSpan(privacy);
        spStr1.setSpan(clickSpan1, 0, privacy.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(a);
        textView.append(spStr);
        textView.append(b);
        textView.append(spStr1);
        textView.append(c);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private class CustomizedClickableSpan extends ClickableSpan {

        String text;

        public CustomizedClickableSpan(String text) {
            super();
            this.text = text;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//	        ds.setColor(ds.linkColor);
            ds.setColor(mContext.getResources().getColor(R.color.maincolor));
//	        ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if ("《服务使用协议》".equals(text)) {
                WebActivity.start(mContext, Config.WEB_SERVICE_NAME, Config.WEB_SERVICE_URL,50);
            } else if ("《隐私政策》".equals(text)) {
                WebActivity.start(mContext, Config.WEB_PRIVACY_NAME, Config.WEB_PRIVACY_URL,50);
            }
        }
    }
}
