package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;


import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyVIPActivity extends BaseActivity {

    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
        lp.setMargins(0, StringUtils.getStatusBarHeight(mContext), 0, 0);
    }

    @Override
    protected void initPresenter() {

    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;

            default:
                break;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BuyVIPActivity.class);
        context.startActivity(intent);
    }
}
