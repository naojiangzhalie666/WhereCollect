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

public class PopupEditInterlayer extends BasePopupWindow {
    @BindView(R.id.edit_interlayer_tv)
    TextView secondTv;
    @BindView(R.id.edit_furniture_name_tv)
    TextView firstTv;

    public PopupEditInterlayer(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_edit_interlayer);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
    }

    @OnClick({R.id.edit_furniture_name_tv, R.id.edit_interlayer_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_interlayer_tv:
                if (listener != null) {
                    listener.onSecondClick();
                }
                dismiss();
                break;
            case R.id.edit_furniture_name_tv:
                if (listener != null) {
                    listener.onFirstClick();
                }
                dismiss();
                break;
        }

    }

    private EditInterlayerClickListener listener;

    public interface EditInterlayerClickListener {
        void onFirstClick();

        void onSecondClick();
    }

    public void setPopupClickListener(EditInterlayerClickListener listener) {
        this.listener = listener;
    }

    public void setItemName(int firstStrId, int secondStrId) {
        firstTv.setText(firstStrId);
        secondTv.setText(secondStrId);
    }
}
