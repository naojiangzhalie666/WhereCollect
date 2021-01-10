package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IDetailedListContract;
import com.gongwu.wherecollect.contract.presenter.DetailedListPresenter;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.view.DetailedListView;
import com.gongwu.wherecollect.view.Loading;

import butterknife.BindView;

/**
 * 清单
 */
public class DetailedListActivity extends BaseMvpActivity<DetailedListActivity, DetailedListPresenter> implements IDetailedListContract.IDetailedListView {

    private Loading loading;
    private RoomFurnitureResponse structure;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailed_list;
    }

    @Override
    protected void initViews() {
        titleTv.setText("物品清单");
        structure = (RoomFurnitureResponse) getIntent().getSerializableExtra("structure");
        getPresenter().getDetailedList(App.getUser(mContext).getId(),
                getIntent().getStringExtra("family_code"),
                getIntent().getStringExtra("room_code"),
                getIntent().getStringExtra("furniture_code"));
    }

    private void initData(DetailedListBean data) {
        if (structure == null) return;
        DetailedListView detailedListView = new DetailedListView(mContext);
        DetailedListBean newBean = detailedListView.initData(data, structure);
        contentLayout.addView(detailedListView);
        if (newBean != null && newBean.getObjects() != null && newBean.getObjects().size() > 0) {
            initData(newBean);
        }
    }

    @Override
    protected DetailedListPresenter createPresenter() {
        return DetailedListPresenter.getInstance();
    }

    @Override
    public void getDetailedListSuccess(DetailedListBean data) {
        if (data != null) {
            initData(data);
        }
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {

    }

    public static void start(Context context, String family_code, String room_code, String furniture_code, RoomFurnitureResponse mRoomFurniture) {
        Intent intent = new Intent(context, DetailedListActivity.class);
        intent.putExtra("family_code", family_code);
        intent.putExtra("room_code", room_code);
        intent.putExtra("furniture_code", furniture_code);
        intent.putExtra("structure", mRoomFurniture);
        context.startActivity(intent);
    }
}
