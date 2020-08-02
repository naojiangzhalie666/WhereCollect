package com.gongwu.wherecollect.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.FragmentMain.LookFragment;
import com.gongwu.wherecollect.FragmentMain.MeFragment;
import com.gongwu.wherecollect.FragmentMain.RemindFragment;
import com.gongwu.wherecollect.FragmentMain.HomeFragment;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IMainContract;
import com.gongwu.wherecollect.contract.presenter.MainPresenter;
import com.gongwu.wherecollect.net.entity.response.MessageBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.permission.FloatWindowManager;
import com.gongwu.wherecollect.service.TimerService;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.view.ActivityTaskManager;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseMvpActivity<MainActivity, MainPresenter> implements IMainContract.IMainView, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";

    public static RoomFurnitureBean moveLayerBean;
    public static ObjectBean moveBoxBean;
    public static List<ObjectBean> moveGoodsList;

    private static final int TAB_SPACE = 0;
    private static final int TAB_LOOK = 1;
    private static final int TAB_REMIND = 3;
    private static final int TAB_ME = 4;
    private int current_index_of = -1;
    private boolean initData;

    @BindView(R.id.main_tab_rg)
    RadioGroup main_tab_rg;
    @BindView(R.id.move_layer_layout)
    View moveLayerView;
    @BindView(R.id.main_place_tv)
    TextView moveView;
    @BindView(R.id.main_place_layout)
    View moveLayout;
    @BindView(R.id.main_move_goods_view)
    View moveGoodsView;
    @BindView(R.id.main_move_goods_iv)
    GoodsImageView moveGoodsIV;
    @BindView(R.id.main_move_goods_number)
    TextView redNumberTv;


    SparseArray<BaseFragment> fragments;


    @Override
    protected void initViews() {
        main_tab_rg.setOnCheckedChangeListener(this);
        fragments = new SparseArray<>();
        fragments.put(TAB_SPACE, HomeFragment.getInstance());
        fragments.put(TAB_LOOK, LookFragment.getInstance());
        fragments.put(TAB_REMIND, RemindFragment.getInstance());
        fragments.put(TAB_ME, MeFragment.getInstance());
        selectTab(AppConstant.DEFAULT_INDEX_OF);
        if (!App.getUser(mContext).isTest()) {
            //启动Android定时器，并且启动服务 请求消息接口
            TimerService.getConnet(this);
        }
        EventBus.getDefault().register(this);
    }

    private void selectTab(int indexOf) {
        ((RadioButton) main_tab_rg.findViewById(main_tab_rg.getChildAt(indexOf).getId())).setChecked(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.add_goods_iv, R.id.main_place_tv, R.id.main_cancel_tv, R.id.main_move_goods_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_goods_iv:
                if (App.getUser(MainActivity.this).isTest()) {
                    DialogUtil.show("注意", "目前为试用账号，登录后将清空试用账号所有数据", "去登录", "知道了", MainActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkPermissionRequestEach(MainActivity.this, true);
                        }
                    }).setCancelable(true);
                } else {
                    checkPermissionRequestEach(MainActivity.this, true);
                }
                break;
            case R.id.main_place_tv:
            case R.id.main_move_goods_iv:
                Toast.makeText(mContext, "请选择隔层", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_cancel_tv:
                MainActivity.moveBoxBean = null;
                MainActivity.moveGoodsList = null;
                MainActivity.moveLayerBean = null;
                main_tab_rg.setVisibility(View.VISIBLE);
                moveLayerView.setVisibility(View.GONE);
                break;
        }
    }

    private void checkPermissionRequestEach(FragmentActivity activity, boolean start) {
        PermissionX.init(this).permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).
                onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                }).onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
            }
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted) {
                    CameraMainActivity.start(mContext, false);
                } else {
                    Toast.makeText(mContext, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.moveLayerBean != null) {
            moveLayerView.setVisibility(View.VISIBLE);
            moveLayout.setVisibility(View.VISIBLE);
            moveGoodsView.setVisibility(View.GONE);
            main_tab_rg.setVisibility(View.GONE);
            // 使用代码设置drawableTop
            Drawable drawable = getResources().getDrawable(R.drawable.icon_place);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            moveView.setCompoundDrawables(null, drawable, null, null);
        } else if (MainActivity.moveBoxBean != null) {
            moveLayerView.setVisibility(View.VISIBLE);
            moveLayout.setVisibility(View.VISIBLE);
            moveGoodsView.setVisibility(View.GONE);
            main_tab_rg.setVisibility(View.GONE);
            // 使用代码设置drawableTop
            Drawable drawable = getResources().getDrawable(R.drawable.icon_move_box);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            moveView.setCompoundDrawables(null, drawable, null, null);
        } else if (MainActivity.moveGoodsList != null && MainActivity.moveGoodsList.size() > 0) {
            moveLayerView.setVisibility(View.VISIBLE);
            moveGoodsView.setVisibility(View.VISIBLE);
            moveLayout.setVisibility(View.GONE);
            main_tab_rg.setVisibility(View.GONE);
            moveGoodsIV.setCircle(MainActivity.moveGoodsList.get(MainActivity.moveGoodsList.size() - 1));
            moveGoodsIV.setTextSize(8);
            if (MainActivity.moveGoodsList.size() > 1) {
                redNumberTv.setVisibility(View.VISIBLE);
                redNumberTv.setText(String.valueOf(MainActivity.moveGoodsList.size()));
            } else {
                redNumberTv.setVisibility(View.GONE);
            }
        } else {
            MainActivity.moveBoxBean = null;
            MainActivity.moveGoodsList = null;
            MainActivity.moveLayerBean = null;
            main_tab_rg.setVisibility(View.VISIBLE);
            moveLayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int indexof = group.indexOfChild(group.findViewById(checkedId));
        Lg.getInstance().d(TAG, "select tab:" + indexof);
        //避免重复调用
        if (current_index_of == indexof) return;
        if (fragments != null && fragments.size() > 0) {
            //提醒与个人中心 需要正式用户
            if ((TAB_REMIND == indexof || TAB_ME == indexof) && App.getUser(mContext) != null && App.getUser(mContext).isTest()) {
                startActivity(new Intent(mContext, LoginActivity.class));
                selectTab(current_index_of);
                return;
            }
            current_index_of = indexof;
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            BaseFragment fragment = fragments.get(indexof);
            fm.replace(R.id.main_frame_layout, fragment);
            fm.commit();
        } else {
            Lg.getInstance().e(TAG, "onCheckedChanged error:replace fragment");
        }
    }

    String okStr;
    String okUrl;
    String cancelStr;
    String cancelUrl;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.GetMessageList msg) {
        if (!FloatWindowManager.getInstance().applyOrShowFloatWindow(mContext)) {
            AppConstant.isShowMsg = false;
            return;
        }
        final MessageBean messageBean = msg.messageBean;
        String msgId = messageBean.getId();

        if (messageBean.getButtons().size() > 0) {
            for (int i = 0; i < messageBean.getButtons().size(); i++) {
                if (messageBean.getButtons().get(i).getColor().equals("SUCCESS")) {
                    okStr = messageBean.getButtons().get(i).getText();
                    okUrl = TextUtils.isEmpty(messageBean.getButtons().get(i).getApi_url()) ? "" : messageBean.getButtons().get(i).getApi_url();
                    okUrl = okUrl.substring(1, okUrl.length());
                }
                if (messageBean.getButtons().get(i).getColor().equals("DANGER") || messageBean.getButtons().get(i).getColor().equals("DEFAULT")) {
                    cancelStr = messageBean.getButtons().get(i).getText();
                    cancelUrl = TextUtils.isEmpty(messageBean.getButtons().get(i).getApi_url()) ? "" : messageBean.getButtons().get(i).getApi_url();
                    cancelUrl = cancelUrl.substring(1, cancelUrl.length());
                }
            }
        } else {
            AppConstant.isShowMsg = false;
            Lg.getInstance().e(TAG, "消息没有buttons");
            return;
        }

        DialogUtil.showMsg("", messageBean.getContent(), okStr, cancelStr, MainActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConstant.isShowMsg = false;
                if (!messageBean.isIs_read()) {
                    getPresenter().dealWithShareRequest(App.getUser(mContext).getId(), okUrl);
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConstant.isShowMsg = false;
                if (!messageBean.isIs_read()) {
                    getPresenter().dealWithShareRequest(App.getUser(mContext).getId(), cancelUrl);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.MainTabMessage msg) {
        if (msg.position >= 0) {
            initData = true;
            if (!App.getUser(mContext).isTest()) {
                //启动Android定时器，并且启动服务 请求消息接口
                TimerService.getConnet(this);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (initData) {
            selectTab(AppConstant.DEFAULT_INDEX_OF);
            EventBus.getDefault().post(new EventBusMsg.RefreshFragment());
            initData = false;
        }
    }

    @Override
    protected void onDestroy() {
        //停止由AlarmManager启动的循环
        TimerService.stop(this);
        //停止由服务启动的循环
        Intent intent = new Intent(this, TimerService.class);
        stopService(intent);
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected MainPresenter createPresenter() {
        return MainPresenter.getInstance();
    }

    @Override
    public void dealWithShareRequestSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().post(new EventBusMsg.updateShareMsg());
        }
    }

    /**
     * 启动服务
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.startService msg) {
        //启动Android定时器，并且启动服务
        TimerService.getConnet(this);
    }

    /**
     * 停止服务
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.stopService msg) {
        //停止由AlarmManager启动的循环
        TimerService.stop(this);
        //停止由服务启动的循环
        Intent intent = new Intent(this, TimerService.class);
        stopService(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityTaskManager.getInstance().finishAllActivity();
    }
}
