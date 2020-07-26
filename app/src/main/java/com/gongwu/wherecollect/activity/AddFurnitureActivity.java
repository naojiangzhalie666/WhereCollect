package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.FurnitureTemplateListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IAddFurnitureContract;
import com.gongwu.wherecollect.contract.presenter.AddFurniturePresenter;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;
import com.gongwu.wherecollect.view.EditTextWatcher;
import com.gongwu.wherecollect.view.EmptyView;
import com.gongwu.wherecollect.view.Loading;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddFurnitureActivity extends BaseMvpActivity<AddFurnitureActivity, AddFurniturePresenter> implements IAddFurnitureContract.IAddFurnitureView, FurnitureTemplateListAdapter.OnItemChildClickListener {

    private static final String TAG = "AddFurnitureActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_tv)
    TextView cancelView;
    @BindView(R.id.template_type_left)
    TextView leftTypeView;
    @BindView(R.id.template_type_middle)
    TextView middleTypeView;
    @BindView(R.id.template_type_right)
    TextView rightTypeView;
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.add_furniture_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit_keyword_tv)
    EditText keywordEditView;
    @BindView(R.id.add_furniture_search_layout)
    View searchLayout;
    @BindView(R.id.furniture_edit_layout)
    View editLayout;
    @BindView(R.id.add_furniture_type_layout)
    View templateLayout;
    @BindView(R.id.edit_clear)
    ImageView editClear;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    private String familyCode;
    private String locationCode;
    private String keyword;
    private boolean init;
    private int countItem;

    private Loading loading;
    private File imgOldFile;
    private SelectImgDialog selectImgDialog;
    private FurnitureBean selectFurnitureBean;
    private List<FurnitureTemplateBean> mlist;
    //缓存系统模板列表数据
    private List<FurnitureTemplateBean> moldlist;
    private FurnitureTemplateListAdapter mAdapter;
    private PopupEditFurnitureName editNamePopup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_furniture;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mlist = new ArrayList<>();
        moldlist = new ArrayList<>();
        titleTv.setText(R.string.add_furniture);
        cancelView.setText(R.string.cancel_text);
        //获取code
        familyCode = getIntent().getStringExtra("familyCode");
        locationCode = getIntent().getStringExtra("locationCode");
        //初始化
        mEmptyView.setErrorMsg("");
        mEmptyView.setEmptyImg(mContext, R.drawable.ic_room_empty_furniture);
        mAdapter = new FurnitureTemplateListAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {

                getPresenter().getFurnitureList(App.getUser(mContext).getId(), null, keyword);
            }
        });
        mRefreshLayout.autoRefresh();
        //点击监听
        mAdapter.setOnItemClickListener(this);
        //监听搜索按钮
        keywordEditView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(keywordEditView.getText().toString().trim())) {
                        ToastUtil.show(mContext, "输入共享人ID", Toast.LENGTH_SHORT);
                    } else {
                        //会被调用2次
                        if (!init) {
                            init = true;
                            keyword = keywordEditView.getText().toString().trim();
                            mRefreshLayout.autoRefresh();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        keywordEditView.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                editClear.setVisibility(TextUtils.isEmpty(keywordEditView.getText().toString()) ? View.GONE : View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.template_type_left, R.id.template_type_middle, R.id.template_type_right,
            R.id.furniture_search, R.id.title_commit_tv, R.id.edit_clear, R.id.furniture_user_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.template_type_left:
                setViewType();
                leftTypeView.setSelected(true);
                scrollToPosition(0);
                break;
            case R.id.template_type_middle:
                setViewType();
                middleTypeView.setSelected(true);
                scrollToPosition(1);
                break;
            case R.id.template_type_right:
                setViewType();
                rightTypeView.setSelected(true);
                scrollToPosition(2);
                break;
            case R.id.furniture_search:
                //点击搜索
                editLayout.setVisibility(View.GONE);
                templateLayout.setVisibility(View.GONE);
                cancelView.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.VISIBLE);
                keywordEditView.setText("");
                mlist.clear();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.title_commit_tv:
                //取消搜索状态
                editLayout.setVisibility(View.VISIBLE);
                templateLayout.setVisibility(View.VISIBLE);
                cancelView.setVisibility(View.GONE);
                searchLayout.setVisibility(View.GONE);
                //隐藏键盘
                StringUtils.hideKeyboard(keywordEditView);
                mlist.clear();
                mlist.addAll(moldlist);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.edit_clear:
                //搜索框清空
                keywordEditView.setText("");
                break;
            case R.id.furniture_user_add:
                showEditFurnitureNamePopupWindow();
                break;
            default:
                break;
        }
    }

    @Override
    protected AddFurniturePresenter createPresenter() {
        return AddFurniturePresenter.getInstance();
    }

    @Override
    public void getFurnitureListSuccess(List<FurnitureTemplateBean> bean) {
        keyword = null;
        refreshLayoutFinished();
        mlist.clear();
        mlist.addAll(bean);
        this.countItem = bean.size();
        mAdapter.notifyDataSetChanged();
        if (templateLayout.getVisibility() == View.VISIBLE) {
            if (countItem > 0) {
                leftTypeView.setText(bean.get(0).getName());
            }
            if (countItem > 1) {
                middleTypeView.setText(bean.get(1).getName());
            }
            if (countItem > 2) {
                rightTypeView.setText(bean.get(2).getName());
            }
            moldlist.addAll(mlist);
        }
        mEmptyView.setVisibility(mlist.size() > 0 ? View.GONE : View.VISIBLE);
        init = false;
    }

    private void showEditFurnitureNamePopupWindow() {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setBackground(Color.TRANSPARENT);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {

                @Override
                public void onCommitClick(FurnitureBean bean) {
                    if (bean.getBackground_url().contains("http")) {
                        getPresenter().addFurniture(App.getUser(mContext).getId(), familyCode, locationCode, bean);
                    } else {
                        AddFurnitureActivity.this.selectFurnitureBean = bean;
                        //图片有地址 直接上传
                        getPresenter().uploadImg(mContext, new File(bean.getBackground_url()));
                    }
                }

                @Override
                public void onEditNameCommitClick(String name) {

                }

                @Override
                public void onImgClick() {
                    showSelectDialog();
                }
            });
        }
        editNamePopup.showPopupWindow();
        editNamePopup.initData(R.string.user_add_furniture, null, null, true);
    }

    private void scrollToPosition(int position) {
        try {
            mRecyclerView.scrollToPosition(position);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mLayoutManager.scrollToPositionWithOffset(position, 0);
        } catch (Exception e) {
            Log.e(TAG, "scrollToPosition Error");
            e.printStackTrace();
        }
    }

    @Override
    public void addFurnitureSuccess(FurnitureBean bean) {
        if (bean != null) {
            EventBus.getDefault().post(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.RefreshActivity());
            finish();
        }
    }

    @Override
    public void onUpLoadSuccess(String path) {
        //上传图片成功
        if (selectFurnitureBean != null) {
            selectFurnitureBean.setBackground_url(path);
            selectFurnitureBean.setImage_url(path);
            getPresenter().addFurniture(App.getUser(mContext).getId(), familyCode, locationCode, selectFurnitureBean);
            selectFurnitureBean = null;
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
        refreshLayoutFinished();
        init = false;
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }

    public static void start(Context context, String familyCode, String locationCode) {
        Intent intent = new Intent(context, AddFurnitureActivity.class);
        intent.putExtra("familyCode", familyCode);
        intent.putExtra("locationCode", locationCode);
        context.startActivity(intent);
    }

    private void setViewType() {
        leftTypeView.setSelected(false);
        middleTypeView.setSelected(false);
        rightTypeView.setSelected(false);
    }

    @Override
    public void onItemClick(FurnitureBean bean) {
        getPresenter().addFurniture(App.getUser(mContext).getId(), familyCode, locationCode, bean);
    }

    /**
     * 图片选择dialog
     */
    private void showSelectDialog() {
        if (selectImgDialog == null) {
            selectImgDialog = new SelectImgDialog(this, null, 1, imgOldFile) {
                @Override
                public void getResult(List<File> list) {
                    super.getResult(list);
                    if (list != null && list.size() > 0) {
                        imgOldFile = list.get(0);
                        selectImgDialog.cropBitmap(imgOldFile);
                    }
                }

                @Override
                protected void resultFile(File file) {
                    super.resultFile(file);
                    if (editNamePopup != null) {
                        editNamePopup.setImg(file.getAbsolutePath());
                    }
                }
            };
            selectImgDialog.hintLayout();
        }
        //编辑选择是否隐藏的 根据imgOldFile来判断
        selectImgDialog.showEditIV(imgOldFile == null ? View.GONE : View.VISIBLE);
        selectImgDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (selectImgDialog != null) {
            selectImgDialog.onActivityResult(requestCode, resultCode, data);
        }
    }
}
