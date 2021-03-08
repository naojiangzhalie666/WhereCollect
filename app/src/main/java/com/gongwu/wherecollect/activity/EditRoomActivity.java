package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.EditRoomListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IEditRoomContract;
import com.gongwu.wherecollect.contract.presenter.EditRoomPresenter;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;
import com.gongwu.wherecollect.view.PopupEditMoveFurniture;
import com.gongwu.wherecollect.view.Loading;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditRoomActivity extends BaseMvpActivity<EditRoomActivity, EditRoomPresenter> implements IEditRoomContract.IEditRoomView, OnItemMoveListener, EditRoomListAdapter.OnItemClickListener {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_white_tv)
    TextView cancelView;
    @BindView(R.id.edit_room_recyclerView)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.room_edit_layout)
    View roomEditLayout;
    @BindView(R.id.room_move_tv)
    TextView moveTv;
    @BindView(R.id.room_edit_tv)
    TextView editTv;
    @BindView(R.id.room_add_tv)
    TextView addTv;
    @BindView(R.id.room_cancel_tv)
    TextView cancelTv;

    private String familyCode;
    private List<RoomBean> mlist;
    private EditRoomListAdapter mAdapter;
    private PopupEditFurnitureName editNamePopup;
    private PopupEditMoveFurniture editMovePopup;
    private int selectPosition;
    private String editName;

    private Loading loading;
    private MyHandler handler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_room;
    }

    @Override
    protected void initViews() {
        titleTv.setText(R.string.edit_room);
        cancelView.setText(R.string.cancel_text);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mlist = new ArrayList<>();
        familyCode = getIntent().getStringExtra("familyCode");
        mlist = (List<RoomBean>) getIntent().getSerializableExtra("rooms");
        //设置默认的布局方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置adapter
        mAdapter = new EditRoomListAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLongPressDragEnabled(true);
        mRecyclerView.setOnItemMoveListener(this);
        mAdapter.setEditClickListener(this);
        handler = new MyHandler(this);
    }

    @OnClick({R.id.back_btn, R.id.title_commit_white_tv, R.id.room_add_tv, R.id.room_edit_tv, R.id.room_move_tv, R.id.room_cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                //返回
                finish();
                break;
            case R.id.title_commit_white_tv:
                //取消
                if (mAdapter != null) {
                    mAdapter.setEditType(false);
                    cancelView.setVisibility(View.GONE);
                    roomEditLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setLongPressDragEnabled(true);
                }
                break;
            case R.id.room_add_tv:
                //添加空间房间
                showPopupWindow(null);
                break;
            case R.id.room_edit_tv:
                //更改空间名
                if (mAdapter != null) {
                    mAdapter.setEditType(true);
                    cancelView.setVisibility(View.VISIBLE);
                    roomEditLayout.setVisibility(View.GONE);
                    mRecyclerView.setLongPressDragEnabled(false);
                }
                break;
            case R.id.room_move_tv:
                //移动空间至新的家庭
                if (mAdapter != null) {
                    //移动模式下
                    if (mAdapter.getMoveEditType()) {
                        List<String> codes = new ArrayList<>();
                        for (RoomBean roomBean : mlist) {
                            if (roomBean.isSelect()) {
                                codes.add(roomBean.getCode());
                            }
                        }
                        if (codes.size() == 0) {
                            Toast.makeText(mContext, "请选择房间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showMovePopupWindow(codes);
                    } else {
                        //进入移动模式
                        for (RoomBean bean : mlist) {
                            bean.setSelect(false);
                        }
                        mAdapter.setMoveEditType(true);
                        addTv.setVisibility(View.GONE);
                        editTv.setVisibility(View.GONE);
                        cancelTv.setVisibility(View.VISIBLE);
                    }
                    mRecyclerView.setLongPressDragEnabled(false);
                }
                break;
            case R.id.room_cancel_tv:
                //取消移动模式
                if (mAdapter != null) {
                    mAdapter.setMoveEditType(false);
                    addTv.setVisibility(View.VISIBLE);
                    editTv.setVisibility(View.VISIBLE);
                    cancelTv.setVisibility(View.GONE);
                    mRecyclerView.setLongPressDragEnabled(true);
                }
                break;
        }
    }

    private void showPopupWindow(RoomBean bean) {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setBackground(Color.TRANSPARENT);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {
                @Override
                public void onEditNameCommitClick(String name) {
                    if (bean != null) {
                        editName = name;
                        getPresenter().editRoom(App.getUser(mContext).getId(), bean.getCode(), name);
                    } else {
                        getPresenter().addRoom(App.getUser(mContext).getId(), familyCode, name);
                    }
                }

                @Override
                public void onCommitClick(FurnitureBean bean) {

                }

                @Override
                public void onImgClick() {

                }
            });
        }
        editNamePopup.showPopupWindow();
        editNamePopup.initData(R.string.add_room, bean != null ? bean.getName() : null, null, false);
    }

    private void showMovePopupWindow(List<String> codes) {
        if (editMovePopup == null) {
            editMovePopup = new PopupEditMoveFurniture(mContext);
            editMovePopup.setBackground(Color.TRANSPARENT);
            editMovePopup.setPopupGravity(Gravity.CENTER);
            editMovePopup.setPopupClickListener(new PopupEditMoveFurniture.PopupClickListener() {
                @Override
                public void onCommitClick(String family_code, String room_code) {

                }

                @Override
                public void onMoveRoomClick(FamilyBean bean) {
                    getPresenter().moveRoom(App.getUser(mContext).getId(), familyCode, bean.getCode(), bean.getName(), codes);
                }
            });
        }
        editMovePopup.showPopupWindow();
        editMovePopup.setMoveRoom(true);
        editMovePopup.initData();
    }

    @Override
    protected EditRoomPresenter createPresenter() {
        return EditRoomPresenter.getInstance();
    }

    @Override
    public void onEditNameClick(int position) {
        this.selectPosition = position;
        showPopupWindow(mlist.get(selectPosition));
    }

    @Override
    public void onDelClick(int position) {
        this.selectPosition = position;
        DialogUtil.show("", "是否删除？删除后,该空间内所有家具将被删除，物品变为为“未归位”",
                "确定", "取消", this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().delRoom(App.getUser(mContext).getId(), mlist.get(selectPosition).getCode());

                    }
                }, null);
    }

    @Override
    public void onItemsClick(int position, View v) {
        if (mAdapter != null && mAdapter.getMoveEditType()) {
            mlist.get(position).setSelect(!mlist.get(position).isSelect());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updataRoomPositionSuccess(List<RoomBean> rooms) {
        if (rooms != null && rooms.size() > 0) {
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.RefreshActivity());
        }
    }

    @Override
    public void addRoomSuccess(RoomBean rooms) {
        if (rooms != null) {
            mlist.add(rooms);
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.RefreshActivity());
        }
    }

    @Override
    public void delRoomSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS && selectPosition >= 0) {
            mlist.remove(selectPosition);
            selectPosition = -1;
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.RefreshActivity());
        }
    }

    @Override
    public void editRoomSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS && selectPosition >= 0) {
            mlist.get(selectPosition).setName(editName);
            editName = null;
            selectPosition = -1;
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.RefreshActivity());
        }
    }

    @Override
    public void moveRoomSuccess(RequestSuccessBean rooms) {
        if (rooms.getOk() == AppConstant.REQUEST_SUCCESS) {
            for (int i = 0; i < mlist.size(); i++) {
                if (mlist.get(i).isSelect()) {
                    mlist.remove(i);
                    i--;
                }
            }
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.RefreshActivity());
        }
    }

    public static void start(Context context, String familyCode, List<RoomBean> rooms) {
        Intent intent = new Intent(context, EditRoomActivity.class);
        if (rooms != null) {
            intent.putExtra("rooms", (Serializable) rooms);
        }
        intent.putExtra("familyCode", familyCode);
        context.startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(loading, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        int fromPosition = srcHolder.getAdapterPosition();
        int toPosition = targetHolder.getAdapterPosition();
        // Item被拖拽时，交换数据，并更新adapter。
        mlist.add(toPosition, mlist.remove(fromPosition));
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        handler.removeCallbacks(mStartPostRunnable);
        handler.postDelayed(mStartPostRunnable, 2000);
        return true;
    }

    private void updateList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.getListSize(mlist); i++) {
            sb.append(mlist.get(i).getCode());
            sb.append(",");
        }
        if (sb.length() != 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        getPresenter().updataRoomPosition(App.getUser(mContext).getId(), sb.toString());
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
    }


    private static class MyHandler extends Handler {

        private final WeakReference<BaseActivity> mActivty;

        private MyHandler(BaseActivity mActivty) {
            this.mActivty = new WeakReference<BaseActivity>(mActivty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private Runnable mStartPostRunnable = new Runnable() {
        @Override
        public void run() {
            mAdapter.notifyDataSetChanged();
            updateList();
        }
    };

}
