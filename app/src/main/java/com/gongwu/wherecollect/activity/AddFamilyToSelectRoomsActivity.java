package com.gongwu.wherecollect.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.AddFamilyToSelectRoomsAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddFamilyToSelectRoomsContract;
import com.gongwu.wherecollect.contract.presenter.AddFamilyToSelectRoomsPresenter;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddFamilyToSelectRoomsActivity extends BaseMvpActivity<AddFamilyToSelectRoomsActivity, AddFamilyToSelectRoomsPresenter> implements IAddFamilyToSelectRoomsContract.IAddFamilyToSelectRoomsView, AddFamilyToSelectRoomsAdapter.OnItemClickListener {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_tv)
    TextView commitTv;
    @BindView(R.id.select_rooms_list_view)
    RecyclerView mRecyclerView;

    private AddFamilyToSelectRoomsAdapter mAdapter;
    private List<RoomFurnitureBean> mlist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_family_to_select_rooms;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        titleTv.setText(getIntent().getStringExtra("familyName"));
        commitTv.setText(R.string.confirm);
        commitTv.setVisibility(View.VISIBLE);
        mlist = new ArrayList<>();
        mAdapter = new AddFamilyToSelectRoomsAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEditClickListener(this);
        getPresenter().getRoomsTemplate(App.getUser(mContext).getId());
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.title_commit_tv:
                getPresenter().createFamily(App.getUser(mContext).getId(), getIntent().getStringExtra("familyName"), mlist);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemsClick(int position, View v) {
        if (mAdapter != null && position != mlist.size() - 1) {
            mlist.get(position).setSelect(!mlist.get(position).isSelect());
            mAdapter.notifyItemChanged(position);
        } else if (mAdapter != null && position == mlist.size() - 1) {
            showPopupWindow();
        }
    }

    PopupEditFurnitureName editNamePopup;

    private void showPopupWindow() {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setBackground(Color.TRANSPARENT);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {

                @Override
                public void onCommitClick(FurnitureBean bean) {

                }

                @Override
                public void onEditNameCommitClick(String name) {
                    RoomFurnitureBean bean = new RoomFurnitureBean();
                    bean.setName(name);
                    bean.setSelect(true);
                    mlist.add(mlist.size() - 1, bean);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onImgClick() {

                }
            });
        }
        editNamePopup.showPopupWindow();
        editNamePopup.initData(R.string.edit_rooms_name, null, null, false);
    }

    @Override
    protected AddFamilyToSelectRoomsPresenter createPresenter() {
        return AddFamilyToSelectRoomsPresenter.getInstance();
    }

    @Override
    public void getRoomsTemplateSuccess(List<RoomFurnitureBean> beans) {
        mlist.clear();
        mlist.addAll(beans);
        RoomFurnitureBean bean = new RoomFurnitureBean();
        bean.setName("自定义");
        mlist.add(bean);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void createFamilySuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            setResult(RESULT_OK);
            finish();
        }
    }

    public static void start(Context mContext, String familyName) {
        Intent intent = new Intent(mContext, AddFamilyToSelectRoomsActivity.class);
        intent.putExtra("familyName", familyName);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

}
