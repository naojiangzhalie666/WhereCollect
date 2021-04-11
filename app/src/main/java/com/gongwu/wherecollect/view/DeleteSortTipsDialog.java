package com.gongwu.wherecollect.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
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

public class DeleteSortTipsDialog extends Dialog {

    @BindView(R.id.delete_sort_hint_msg_tv)
    TextView hintMsgTv;
    @BindView(R.id.sort_cancel_tv)
    TextView cancelTv;
    @BindView(R.id.sort_submit_tv)
    TextView submitTv;

    private Context mContext;

    public DeleteSortTipsDialog(Context context) {
        super(context);
    }

    public void setMsgText(int value) {
        hintMsgTv.setText(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_sort_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.sort_cancel_tv, R.id.sort_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_cancel_tv:
                dismiss();
                break;
            case R.id.sort_submit_tv:
                submitSort();
                dismiss();
                break;
        }
    }

    public void submitSort() {

    }
}
