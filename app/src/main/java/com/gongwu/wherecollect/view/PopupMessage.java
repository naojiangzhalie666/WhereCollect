package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhon.appupdate.utils.ScreenUtil;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.ImportGoodsAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupMessage extends BasePopupWindow {

    @BindView(R.id.pop_msg_tv)
    TextView popMsgTv;

    public PopupMessage(Context context) {
        super(context);
    }


    public void initData(String message) {
        if (popMsgTv != null) {
            popMsgTv.setText(message);
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_message);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);

    }

}
