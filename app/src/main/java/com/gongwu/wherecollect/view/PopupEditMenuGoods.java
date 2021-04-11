package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupEditMenuGoods extends BasePopupWindow {

    public PopupEditMenuGoods(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_edit_goods_menu);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
    }

    @OnClick({R.id.menu_edit_tv, R.id.menu_add_remind_tv, R.id.menu_delete_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_edit_tv:
                if (listener != null) {
                    listener.onEditGoodsClick();
                }
                dismiss();
                break;
            case R.id.menu_add_remind_tv:
                if (listener != null) {
                    listener.onAddRemingClick();
                }
                dismiss();
                break;
            case R.id.menu_delete_tv:
                if (listener != null) {
                    listener.onDeleteGoodsClick();
                }
                dismiss();
                break;
        }
    }

    private EditMenuPopupClickListener listener;

    public interface EditMenuPopupClickListener {
        void onEditGoodsClick();

        void onAddRemingClick();

        void onDeleteGoodsClick();
    }

    public void setPopupClickListener(EditMenuPopupClickListener listener) {
        this.listener = listener;
    }
}
