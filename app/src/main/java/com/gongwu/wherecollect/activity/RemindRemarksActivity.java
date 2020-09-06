package com.gongwu.wherecollect.activity;


import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.EditTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

public class RemindRemarksActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.remind_remarks_et)
    EditText mEditText;
    @BindView(R.id.remind_remarks_text_num_tv)
    TextView numTv;
    @BindView(R.id.title_commit_tv_color_maincolor)
    TextView commitTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_remind_remarks;
    }

    @Override
    protected void initViews() {
        titleTv.setText(R.string.remind_remarks_title_text);
        commitTv.setVisibility(View.VISIBLE);
        String text = getIntent().getStringExtra("remind_remarks");
        mEditText.setText(text);
        numTv.setText(String.format(getString(R.string.remind_remarks_num_text), String.valueOf(mEditText.getText().toString().trim().length())));
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        initEvent();
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv_color_maincolor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.title_commit_tv_color_maincolor:
                Intent intent = new Intent();
                if (!TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    intent.putExtra("remind_remarks", mEditText.getText().toString().trim());
                }
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void initEvent() {

        mEditText.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                numTv.setText(String.format(getString(R.string.remind_remarks_num_text), s.length() + ""));
            }
        });
    }

    @Override
    protected void initPresenter() {

    }
}
