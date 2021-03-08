package com.gongwu.wherecollect.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.AddSharePersonOldListAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddShareContract;
import com.gongwu.wherecollect.contract.presenter.AddSharePresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.PopupAddShareSpace;
import com.gongwu.wherecollect.view.EditTextWatcher;
import com.gongwu.wherecollect.view.Loading;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddSharedPersonActivity extends BaseMvpActivity<AddSharedPersonActivity, AddSharePresenter> implements IAddShareContract.IAddShareView, PopupAddShareSpace.PopupCommitClickListener {

    private final int START_CODE = 101;

    @BindView(R.id.add_share_title_tv)
    TextView titleView;
    @BindView(R.id.add_share_edit)
    EditText addShareEditView;
    @BindView(R.id.add_share_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.delete_btn)
    ImageButton deleteBtn;

    private Loading loading;
    private PopupAddShareSpace popup;

    private List<SharedPersonBean> mlist = new ArrayList<>();
    private AddSharePersonOldListAdapter mAdapter;
    private boolean init, initList, search;
    private SharedPersonBean selectBean;
    private String location_codes, content_text;
    private SharedLocationBean sharedLocationBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_share_person;
    }

    @Override
    protected void initViews() {
        titleView.setText("添加共享人");
        sharedLocationBean = (SharedLocationBean) getIntent().getSerializableExtra("locationBean");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AddSharePersonOldListAdapter(this, mlist);
        mRecyclerView.setAdapter(mAdapter);
        initEvent();
        initData();
    }

    private void initEvent() {
        if (mAdapter != null) {
        }
        addShareEditView.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (count > 0) {
                    deleteBtn.setVisibility(View.VISIBLE);
                } else {
                    deleteBtn.setVisibility(View.GONE);
                    if (initList) {
                        initData();
                    }
                }
            }
        });
        addShareEditView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(addShareEditView.getText().toString().trim())) {
                        ToastUtil.show(mContext, "输入共享人ID", Toast.LENGTH_SHORT);
                    } else {
                        //会被调用2次
                        if (!init) {
                            init = true;
                            search = true;
                            getPresenter().getShareUserCodeInfo(App.getUser(mContext).getId(), addShareEditView.getText().toString().trim());
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                //点击+号进来的 需要选择共享的空间
                if (mlist != null && mlist.size() >= positions) {
                    selectBean = mlist.get(positions);
                    //如果是从空间点进来的直接共享该空间 不用去选择空间
                    if (sharedLocationBean != null) {
                        location_codes = sharedLocationBean.getCode();
                        content_text = sharedLocationBean.getName();
//                        startDialog();
                        return;
                    }
                    getPresenter().getShareRoomList(App.getUser(mContext).getId(), App.getSelectFamilyBean().getCode(), selectBean.getId());
                }
            }
        });

    }

    private void initData() {
        initList = false;
        getPresenter().getSharePersonOldList(App.getUser(mContext).getId());
    }

    @OnClick({R.id.add_share_back_btn, R.id.add_share_scan_ib, R.id.delete_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_share_back_btn:
                finish();
                break;
            case R.id.add_share_scan_ib:
                Intent intent = new Intent(mContext, CaptureActivity.class);
                intent.putExtra("title", "添加共享人");
                intent.putExtra("content", "请对方打开“我的”中的二维码进行扫码\n添加");
                startActivityForResult(intent, START_CODE);

                break;
            case R.id.delete_btn:
                deleteBtn.setVisibility(View.GONE);
                addShareEditView.setText("");
                if (initList) {
                    initData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected AddSharePresenter createPresenter() {
        return AddSharePresenter.getInstance();
    }

    @Override
    public void getSharePersonOldListSuccess(List<SharedPersonBean> data) {
        mlist.clear();
        mlist.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getShareUserCodeInfoSuccess(SharedPersonBean data) {
        mlist.clear();
        if (data != null) {
            mlist.add(data);
        }
        if (search && mlist.size() > 0) {
            selectBean = mlist.get(0);
            search = false;
        }
        mAdapter.notifyDataSetChanged();
        initList = true;
        getPresenter().getShareRoomList(App.getUser(mContext).getId(), App.getSelectFamilyBean().getCode(), selectBean.getId());
    }

    @Override
    public void setShareLocationSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().post(new EventBusMsg.UpdateShareMsg());
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void getShareRoomListSuccess(List<BaseBean> data) {
        if (popup == null) {
            popup = new PopupAddShareSpace(mContext);
            popup.setPopupClickListener(this);
            popup.setPopupGravity(Gravity.CENTER);
        }
        List<BaseBean> mlist = new ArrayList<>();
        for (BaseBean baseBean : data) {
            if (!baseBean.isBeShared()) {
                mlist.add(baseBean);
            }
        }
        popup.setTitleView(R.string.select_room, mlist);
        if (mlist.size() > 0) {
            popup.showPopupWindow();
        } else {
            Toast.makeText(mContext, "暂无房间分享", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCommitClickListener(List<BaseBean> data) {
        StringBuilder sb = new StringBuilder();
        List<String> roomCodes = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelect()) {
                sb.append("【" + data.get(i).getName() + "】");
                if (i != data.size() - 1) {
                    sb.append(",");
                }
                roomCodes.add(data.get(i).getCode());
            }
        }
        content_text = sb.toString();
        startDialog(roomCodes);
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


    public static void start(Context context, int START_CODE) {
        Intent intent = new Intent(context, AddSharedPersonActivity.class);
        ((Activity) context).startActivityForResult(intent, START_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CODE && resultCode == CaptureActivity.result) {//扫描的到结果
            String result = data.getStringExtra("result");
            addShareEditView.setText(result);
            addShareEditView.setSelection(result.length());
            search = true;
            getPresenter().getShareUserCodeInfo(App.getUser(mContext).getId(), result);
        }
    }

    /**
     * 提示
     */
    private void startDialog(List<String> roomCodes) {
        String content;
        if (selectBean.isValid()) {
            content = "已与" + selectBean.getNickname() + "建立连接,直接共享" + content_text + "?";
        } else {
            content = "是否邀请@" + selectBean.getNickname() + ",并共享" + content_text + "?" + "\n（共享后，双方可同时查看和编辑该空间及空间内物品）";
        }
        DialogUtil.show("", content, "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getPresenter().setShareLocation(App.getUser(mContext).getId(), App.getSelectFamilyBean().getId()
                        , App.getSelectFamilyBean().getCode(), roomCodes, selectBean);
            }
        }, null);

    }

}
