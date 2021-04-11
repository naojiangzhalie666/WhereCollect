package com.gongwu.wherecollect.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mucll on 2018/3/15.
 */

public class AddSortDialog extends Dialog {

    @BindView(R.id.dialog_sort_name_tv)
    EditText sortNameTv;
    @BindView(R.id.sort_cancel_tv)
    TextView cancelTv;
    @BindView(R.id.sort_submit_tv)
    TextView submitTv;

    private Context mContext;

    public AddSortDialog(Context context) {
        super(context);
    }

    public void setSortNameTv(String value) {
        if (sortNameTv != null) {
            sortNameTv.setText(value);
            sortNameTv.setSelection(value.length());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_sort_layout);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.sort_cancel_tv, R.id.sort_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_cancel_tv:
                dismiss();
                break;
            case R.id.sort_submit_tv:
                submitSortName(sortNameTv.getText().toString().trim());
                dismiss();
                break;
        }
    }

    public void submitSortName(String value) {

    }

    @Override
    public void show() {
        super.show();
        sortNameTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                StringUtils.showKeyboard(sortNameTv);
            }
        }, 200);

    }
}
