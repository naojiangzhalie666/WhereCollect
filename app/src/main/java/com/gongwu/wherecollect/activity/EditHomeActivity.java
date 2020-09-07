package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IEditHomeContract;
import com.gongwu.wherecollect.contract.presenter.EditHomePresenter;
import com.gongwu.wherecollect.fragment.FamilyRoomFragment;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;
import com.gongwu.wherecollect.view.PopupEditInterlayer;
import com.gongwu.wherecollect.view.PopupEditMoveFurniture;
import com.gongwu.wherecollect.view.HomeFragmentAdapter;
import com.gongwu.wherecollect.view.Loading;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditHomeActivity extends BaseMvpActivity<EditHomeActivity, EditHomePresenter> implements IEditHomeContract.IEditHomeView, FamilyRoomFragment.SelectItemClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "EditHomeActivity";

    @BindView(R.id.edit_home_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.edit_home_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.furniture_add_tv)
    TextView addView;
    @BindView(R.id.furniture_edit_tv)
    TextView editView;
    @BindView(R.id.furniture_move_tv)
    TextView moveView;
    @BindView(R.id.furniture_del_tv)
    TextView deleteView;
    @BindView(R.id.furniture_top_tv)
    TextView topView;

    private Loading loading;
    private String familyCode;
    private List<BaseFragment> fragments = new ArrayList<>();
    private List<RoomBean> mlist = new ArrayList<>();
    private HomeFragmentAdapter mAdapter;
    private RoomBean selectRoomBean;
    private List<FurnitureBean> mselectlist;
    private int selectPosition = 0;
    private FurnitureBean selectFurnitureBean;
    private boolean refresh, refreshItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_home;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.maincolor));
        StatusBarUtil.setLightStatusBar(this, false);
        EventBus.getDefault().register(this);
        mselectlist = new ArrayList<>();
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), fragments, mlist);
        //联动
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        familyCode = getIntent().getStringExtra("familyCode");
        selectPosition = getIntent().getIntExtra("selectPosition", AppConstant.DEFAULT_INDEX_OF);
        if (!TextUtils.isEmpty(familyCode)) {
            getPresenter().getFamilyRoomList(App.getUser(mContext).getId(), familyCode);
        }
    }

    @Override
    protected EditHomePresenter createPresenter() {
        return EditHomePresenter.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新空间
        if (refresh) {
            refresh = false;
            getPresenter().getFamilyRoomList(App.getUser(mContext).getId(), familyCode);
        }
        //添加新的家具刷新
        if (refreshItem) {
            refreshItem = false;
            if (fragments.size() > selectPosition) {
                fragments.get(selectPosition).refreshFragment();
            }
        }

    }

    @OnClick({R.id.back_btn, R.id.image_btn, R.id.furniture_add_tv, R.id.furniture_edit_tv, R.id.furniture_move_tv, R.id.furniture_del_tv, R.id.furniture_top_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.image_btn:
                EditRoomActivity.start(mContext, familyCode, mlist);
                break;
            case R.id.furniture_add_tv:
                AddFurnitureActivity.start(mContext, familyCode, mlist.get(selectPosition).getCode());
                break;
            case R.id.furniture_edit_tv:
                showPopupWindow();
                break;
            case R.id.furniture_move_tv:
                showEditFurnitureMovePopupWindow();
                break;
            case R.id.furniture_del_tv:
                DialogUtil.show("确认删除该家具吗？", "删除后,家具内物品将标记为“未归位”",
                        "确定", "取消", this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<String> furnitureCodes = new ArrayList<>();
                                for (FurnitureBean bean : mselectlist) {
                                    furnitureCodes.add(bean.getCode());
                                }
                                getPresenter().deleteFurniture(App.getUser(mContext).getId(), selectRoomBean.getCode(), furnitureCodes);
                            }
                        }, null);
                break;
            case R.id.furniture_top_tv:
                List<String> codes = new ArrayList<>();
                for (FurnitureBean bean : mselectlist) {
                    codes.add(bean.getCode());
                }
                getPresenter().topFurniture(App.getUser(mContext).getId(), codes);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    PopupEditInterlayer popup;
    PopupEditFurnitureName editNamePopup;
    PopupEditMoveFurniture editMovePopup;

    private void showPopupWindow() {
        //只选一个才显示编辑模式
        if (popup == null) {
            popup = new PopupEditInterlayer(mContext);
            popup.setBackground(Color.TRANSPARENT);
            popup.setPopupGravity(Gravity.TOP | Gravity.CENTER);
            popup.setPopupClickListener(new PopupEditInterlayer.EditInterlayerClickListener() {
                @Override
                public void onFirstClick() {
                    showEditFurnitureNamePopupWindow(mselectlist.get(AppConstant.DEFAULT_INDEX_OF));
                }

                @Override
                public void onSecondClick() {
                    EditFurniturePatternActivity.start(mContext, mselectlist.get(AppConstant.DEFAULT_INDEX_OF), familyCode);
                }

            });
        }
        popup.showPopupWindow(editView);
    }

    private void showEditFurnitureNamePopupWindow(FurnitureBean bean) {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setBackground(Color.TRANSPARENT);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {

                @Override
                public void onCommitClick(FurnitureBean bean) {
                    if (bean.getBackground_url().contains("http")) {
                        getPresenter().updataFurniture(App.getUser(mContext).getId(), familyCode, bean);
                    } else {
                        EditHomeActivity.this.selectFurnitureBean = bean;
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
        editNamePopup.initData(R.string.pop_edit_furniture_name, null, bean, true);
    }

    private void showEditFurnitureMovePopupWindow() {
        if (editMovePopup == null) {
            editMovePopup = new PopupEditMoveFurniture(mContext);
            editMovePopup.setBackground(Color.TRANSPARENT);
            editMovePopup.setPopupGravity(Gravity.CENTER);
            editMovePopup.setPopupClickListener(new PopupEditMoveFurniture.PopupClickListener() {
                @Override
                public void onCommitClick(String family_code, String room_code) {
                    List<String> codes = new ArrayList<>();
                    for (FurnitureBean bean : mselectlist) {
                        codes.add(bean.getCode());
                    }
                    getPresenter().moveFurniture(App.getUser(mContext).getId(), familyCode, room_code, family_code, codes);
                }

                @Override
                public void onMoveRoomClick(FamilyBean bean) {

                }
            });
        }
        editMovePopup.showPopupWindow();
        editMovePopup.initData();
    }

    @Override
    public void getFamilyRoomListSuccess(List<RoomBean> rooms) {
        fragments.clear();
        mlist.clear();
        if (rooms != null && rooms.size() > 0) {
            Collections.sort(rooms, new Comparator<RoomBean>() {
                @Override
                public int compare(RoomBean lhs, RoomBean rhs) {
                    return rhs.getWeight() - lhs.getWeight();
                }
            });
            mlist.addAll(rooms);
            for (int i = 0; i < rooms.size(); i++) {
                FamilyRoomFragment fragment = FamilyRoomFragment.getInstance(rooms.get(i), familyCode, true);
                fragment.setSelectItemClickListener(this);
                fragments.add(fragment);
            }
            mAdapter.notifyDataSetChanged();
            if (fragments.size() > selectPosition) {
                mViewPager.setCurrentItem(selectPosition);
            }
        }
    }

    SelectImgDialog selectImgDialog;
    File imgOldFile;

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
    public void deleteFurnitureSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            refreshSelectFragment();
        }
    }

    @Override
    public void topFurnitureSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            refreshSelectFragment();
        }
    }

    @Override
    public void moveFurnitureSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            refreshSelectFragment();
        }
    }

    @Override
    public void updataFurnitureSuccess(FurnitureBean bean) {
        //修改图片或名称
        if (bean != null) {
            refreshSelectFragment();
        }
    }

    @Override
    public void onUpLoadSuccess(String path) {
        //上传图片成功
        if (selectFurnitureBean != null) {
            selectFurnitureBean.setBackground_url(path);
            getPresenter().updataFurniture(App.getUser(mContext).getId(), familyCode, selectFurnitureBean);
            selectFurnitureBean = null;
        }
    }

    @Override
    public void onSelectItemClick(RoomBean roomBean, List<FurnitureBean> mlist) {
        this.selectRoomBean = roomBean;
        mselectlist.clear();
        mselectlist.addAll(mlist);
        if (mlist != null) {
            if (mlist.size() == 0) {
                setNotEditLayout();
            } else if (mlist.size() == 1) {
                setOneEditLayout();
            } else {
                setMoreEditLayout();
            }
        } else {
            setNotEditLayout();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            refreshSelectFragment();
        } else {
            if (selectImgDialog != null) {
                selectImgDialog.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void refreshSelectFragment() {
        if (fragments.size() > selectPosition) {
            setNotEditLayout();
            fragments.get(selectPosition).clearSelect();
            fragments.get(selectPosition).refreshFragment();
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
        }
    }

    public static void start(Context mContext, String familyCode, int selectPosition) {
        Intent intent = new Intent(mContext, EditHomeActivity.class);
        intent.putExtra("familyCode", familyCode);
        intent.putExtra("selectPosition", selectPosition);
        mContext.startActivity(intent);
    }

    private void setNotEditLayout() {
        addView.setVisibility(View.VISIBLE);
        editView.setVisibility(View.GONE);
        moveView.setVisibility(View.GONE);
        deleteView.setVisibility(View.GONE);
        topView.setVisibility(View.GONE);
    }

    private void setOneEditLayout() {
        addView.setVisibility(View.GONE);
        editView.setVisibility(View.VISIBLE);
        moveView.setVisibility(View.VISIBLE);
        deleteView.setVisibility(View.VISIBLE);
        topView.setVisibility(View.VISIBLE);
    }

    private void setMoreEditLayout() {
        addView.setVisibility(View.GONE);
        editView.setVisibility(View.GONE);
        moveView.setVisibility(View.VISIBLE);
        deleteView.setVisibility(View.VISIBLE);
        topView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.selectPosition = position;
        setNotEditLayout();
        if (fragments != null && fragments.size() > position) {
            fragments.get(position).clearSelect();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.RefreshActivity msg) {
        refresh = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.RefreshEditItem msg) {
        refreshItem = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
