package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupEditBox extends BasePopupWindow {
    @BindView(R.id.edit_reset_name_tv)
    TextView resetNameTv;
    @BindView(R.id.edit_remove_tv)
    TextView removeTv;
    @BindView(R.id.edit_del_tv)
    TextView delTv;

    public PopupEditBox(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_edit_box);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
    }

    @OnClick({R.id.edit_reset_name_tv, R.id.edit_remove_tv, R.id.edit_del_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_reset_name_tv:
                if (listener != null) {
                    listener.onResetNameClick();
                }
                dismiss();
                break;
            case R.id.edit_remove_tv:
                if (listener != null) {
                    listener.onReMoveClick();
                }
                dismiss();
                break;
            case R.id.edit_del_tv:
                if (listener != null) {
                    listener.onDelClick();
                }
                dismiss();
                break;
        }

    }

    private EditInterlayerClickListener listener;

    public interface EditInterlayerClickListener {
        void onResetNameClick();

        void onReMoveClick();

        void onDelClick();
    }

    public void setPopupClickListener(EditInterlayerClickListener listener) {
        this.listener = listener;
    }

}
