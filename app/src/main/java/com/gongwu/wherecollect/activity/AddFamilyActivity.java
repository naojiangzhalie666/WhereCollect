package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.util.AnimationUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AddFamilyActivity extends BaseActivity {
    @BindView(R.id.family_new_name_et)
    EditText mEditText;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_family;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.back_btn, R.id.commit_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.commit_iv:
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    AnimationUtil.StartTranslate(mEditText);
                } else {
                    AddFamilyToSelectRoomsActivity.start(mContext, mEditText.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode) {
            finish();
        }
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, AddFamilyActivity.class);
        mContext.startActivity(intent);
    }


}
