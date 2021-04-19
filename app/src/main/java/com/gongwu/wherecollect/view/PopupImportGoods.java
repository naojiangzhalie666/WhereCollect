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

public class PopupImportGoods extends BasePopupWindow {
    @BindView(R.id.popup_family_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.look_more_tv)
    TextView lookMoreTv;

    private List<ObjectBean> mlist;
    private ImportGoodsAdapter mAdapter;

    public PopupImportGoods(Context context) {
        super(context);
    }

    public void initData(List<ObjectBean> mlist) {
        if (mlist != null && mAdapter != null) {
            this.mlist.clear();
            this.mlist.addAll(mlist);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_import_goods);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
        mlist = new ArrayList<>();
        mAdapter = new ImportGoodsAdapter(getContext(), mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (listener != null) {
                    listener.onItemsClick(positions, mlist.get(positions));
                    mlist.remove(positions);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        if (ScreenUtil.checkDeviceHasNavigationBar(getContext())) {
            setHeight(ScreenUtil.getHeight(getContext()));
        }
    }

    @OnClick({R.id.look_more_tv, R.id.add_more_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.look_more_tv:
                if (listener != null) {
                    listener.onLookMoreClick();
                    dismiss();
                }
                break;
            case R.id.add_more_tv:
                if (listener != null) {
                    listener.onAddMoreClick();
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected Animation onCreateShowAnimation(int width, int height) {
        //这里完成展示动画
        TranslateAnimation showAnimation = new TranslateAnimation(width, 0, 0, 0);
        showAnimation.setDuration(300);
        return showAnimation;
    }


    @Override
    protected Animation onCreateDismissAnimation(int width, int height) {
        //这里完成消失动画
        TranslateAnimation showAnimation = new TranslateAnimation(0, width, 0, 0);
        showAnimation.setDuration(300);
        return showAnimation;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemsClick(int position, ObjectBean bean);

        void onLookMoreClick();

        void onAddMoreClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLookMoveVisibility(int visibility) {
        lookMoreTv.setVisibility(visibility);
    }
}
