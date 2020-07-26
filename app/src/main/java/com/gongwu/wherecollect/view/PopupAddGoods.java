package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;


import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupAddGoods extends BasePopupWindow {

    public PopupAddGoods(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_add_goods);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
    }

    @OnClick({R.id.cancel_iv, R.id.camera_tv, R.id.photo_tv, R.id.scan_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_iv:
                dismiss();
                break;
            case R.id.camera_tv:
                if (listener != null) {
                    listener.onCameraClick();
                }
                dismiss();
                break;
            case R.id.photo_tv:
                if (listener != null) {
                    listener.onPhotoClick();
                }
                dismiss();
                break;
            case R.id.scan_tv:
                if (listener != null) {
                    listener.onScanClick();
                }
                dismiss();
                break;
        }
    }

    private AddGoodsPopupClickListener listener;

    public interface AddGoodsPopupClickListener {
        void onCameraClick();

        void onPhotoClick();

        void onScanClick();
    }

    public void setPopupClickListener(AddGoodsPopupClickListener listener) {
        this.listener = listener;
    }
}
