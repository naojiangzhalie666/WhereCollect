package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IDetailedListContract;
import com.gongwu.wherecollect.contract.presenter.DetailedListPresenter;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.view.DetailedListView;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.SplitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

    private String userId, family_code, room_id, room_code, furniture_code, qrcodeString;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailed_list;
    }

    @Override
    protected void initViews() {
        titleTv.setText("物品清单");
        structure = (RoomFurnitureResponse) getIntent().getSerializableExtra("structure");
        userId = App.getUser(mContext).getId();
        family_code = getIntent().getStringExtra("family_code");
        room_id = getIntent().getStringExtra("room_id");
        room_code = getIntent().getStringExtra("room_code");
        furniture_code = getIntent().getStringExtra("furniture_code");
        qrcodeString = new StringBuilder("goFurniture:fc=").append(furniture_code).
                append(",rd=").append(room_id)
                .append(",rc=").append(room_code)
                .append(",fmc=").append(family_code).toString();
        getPresenter().getDetailedList(userId, family_code, room_code, furniture_code);
    }

    private int indexof = 1;
    private int initCount = 1;
    private List<String> gcCodes = new ArrayList<>();
    private List<String> boxCodes = new ArrayList<>();

    private void initData(DetailedListBean data) {
        if (structure == null) return;
        DetailedListView detailedListView = new DetailedListView(mContext);
        DetailedListBean newBean = detailedListView.initData(data, qrcodeString, structure, indexof, initCount, gcCodes, boxCodes);
        contentLayout.addView(detailedListView);
        if (newBean != null && newBean.getObjects() != null && newBean.getObjects().size() > 0) {
            indexof++;
            contentLayout.addView(new SplitView(mContext));
            initData(newBean);
        }
    }

    @Override
    protected DetailedListPresenter createPresenter() {
        return DetailedListPresenter.getInstance();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public void getDetailedListSuccess(DetailedListBean data) {
        if (data != null) {
            int initLine = 0;
            initCount = 1;
            indexof = 1;
            gcCodes.clear();
            boxCodes.clear();
            for (int i = 0; i < data.getObjects().size(); i++) {
                DetailedListGoodsBean childBean = data.getObjects().get(i);
                int line = (int) Math.ceil((childBean.getObjs().size()) / 5.0f);
                if (initLine >= 5) {
                    initLine = 0;
                    initCount++;
                }
                initLine += line;
            }
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

    public static void start(Context context, String family_code, String room_id, String room_code, String furniture_code, RoomFurnitureResponse mRoomFurniture) {
        Intent intent = new Intent(context, DetailedListActivity.class);
        intent.putExtra("family_code", family_code);
        intent.putExtra("room_id", room_id);
        intent.putExtra("room_code", room_code);
        intent.putExtra("furniture_code", furniture_code);
        intent.putExtra("structure", mRoomFurniture);
        context.startActivity(intent);
    }
}
