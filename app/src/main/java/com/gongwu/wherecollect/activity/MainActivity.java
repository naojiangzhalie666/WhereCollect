package com.gongwu.wherecollect.activity;

import android.Manifest;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.azhon.appupdate.utils.ApkUtil;
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
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.MessageBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VersionBean;
import com.gongwu.wherecollect.object.AddGoodsActivity;
import com.gongwu.wherecollect.permission.FloatWindowManager;
import com.gongwu.wherecollect.service.TimerService;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.ActivityTaskManager;
import com.gongwu.wherecollect.view.DialogMessageTwoBtn;
import com.gongwu.wherecollect.view.EnergyDialog;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.BuyEnergyDialog;
import com.gongwu.wherecollect.view.StubDialog;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.tencent.wxop.stat.StatService;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseMvpActivity<MainActivity, MainPresenter> implements IMainContract.IMainView {
    private static final String TAG = "MainActivity";

    public static RoomFurnitureBean moveLayerBean;
    public static ObjectBean moveBoxBean;
    public static List<ObjectBean> moveGoodsList;

    private static final int TAB_SPACE = 0;
    private static final int TAB_LOOK = 1;
    private static final int TAB_REMIND = 2;
    private static final int TAB_ME = 3;
    private int current_index_of = -1;
    private boolean initData, initTab;


    @BindView(R.id.main_tab_layout)
    View mainTabLayout;
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
    @BindView(R.id.main_tab_space_tv)
    TextView tabSpaceTv;
    @BindView(R.id.main_tab_look_tv)
    TextView tabLookTv;
    @BindView(R.id.main_tab_remind_tv)
    TextView tabRemindTv;
    @BindView(R.id.main_tab_me_tv)
    TextView tabMeTv;
    @BindView(R.id.hint_add_goods_or_energy_layout)
    View energyView;

    private SparseArray<BaseFragment> fragments;
    private DownloadManager manager;
    private long exitTime, downTime;
    private String energyCode;

    @Override
    protected void initViews() {
        initUM();
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
        getPresenter().getVersion(StringUtils.getCurrentVersionName(mContext));
    }

    public void selectTab(int indexOf) {
        onCheckedChanged(indexOf);
        switch (indexOf) {
            case TAB_SPACE:
                setSelectView(tabSpaceTv);
                break;
            case TAB_LOOK:
                setSelectView(tabLookTv);
                break;
            case TAB_REMIND:
                setSelectView(tabRemindTv);
                break;
            case TAB_ME:
                setSelectView(tabMeTv);
                break;
        }
        if (initTab) {
            initTab = false;
            initEditLayout();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.main_tab_space_tv, R.id.main_tab_look_tv, R.id.main_tab_remind_tv, R.id.main_tab_me_tv,
            R.id.add_goods_iv, R.id.main_place_tv, R.id.main_cancel_tv, R.id.main_move_goods_iv, R.id.start_energy_act,
            R.id.energy_i_know})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_space_tv:
                setSelectView(tabSpaceTv);
                onCheckedChanged(TAB_SPACE);
                break;
            case R.id.main_tab_look_tv:
                setSelectView(tabLookTv);
                onCheckedChanged(TAB_LOOK);
                if ((System.currentTimeMillis() - downTime) > 1000) {
                    downTime = System.currentTimeMillis();
                } else {
                    if (fragments.size() == 4 && fragments.get(TAB_LOOK) instanceof LookFragment) {
                        ((LookFragment) fragments.get(TAB_LOOK)).startSealGoodsActivity();
                    }
                }
                break;
            case R.id.main_tab_remind_tv:
                setSelectView(tabRemindTv);
                onCheckedChanged(TAB_REMIND);
                break;
            case R.id.main_tab_me_tv:
                setSelectView(tabMeTv);
                onCheckedChanged(TAB_ME);
                break;
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
                            checkPermissionRequestEach();
                        }
                    }).setCancelable(true);
                } else {
                    checkPermissionRequestEach();
                }
                break;
            case R.id.main_place_tv:
            case R.id.main_move_goods_iv:
                Toast.makeText(mContext, "请选择要放置的家具", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_cancel_tv:
                MainActivity.moveBoxBean = null;
                MainActivity.moveGoodsList = null;
                MainActivity.moveLayerBean = null;
                mainTabLayout.setVisibility(View.VISIBLE);
                moveLayerView.setVisibility(View.GONE);
                break;
            case R.id.start_energy_act:
                WebActivity.start(mContext, Config.WEB_ENERGY_NAME, Config.WEB_ENERGY_URL, 50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initEnergyDialog();
                    }
                }, 1000);
                break;
            case R.id.energy_i_know:
                initEnergyDialog();
                break;
        }
    }

    private void initEnergyDialog() {
        energyView.setVisibility(View.GONE);
        SaveDate.getInstence(mContext).setEnergyHint(true);
        EnergyDialog energyDialog = new EnergyDialog(mContext) {
            @Override
            public void getUserInfo() {
                getPresenter().getUserInfo(App.getUser(mContext).getId());
            }
        };
        energyDialog.show();
    }

    private void checkPermissionRequestEach() {
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
//                    CameraMainActivity.start(mContext, false, null);
                    AddGoodsActivity.start(mContext, null, null, null);
                } else {
                    Toast.makeText(mContext, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setSelectView(TextView selectView) {
        initTabView();
        selectView.setSelected(true);
    }

    private void initTabView() {
        tabSpaceTv.setSelected(false);
        tabLookTv.setSelected(false);
        tabRemindTv.setSelected(false);
        tabMeTv.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initEditLayout();
    }

    private void initEditLayout() {
        if (MainActivity.moveLayerBean != null) {
            moveLayerView.setVisibility(View.VISIBLE);
            moveLayout.setVisibility(View.VISIBLE);
            moveGoodsView.setVisibility(View.GONE);
            mainTabLayout.setVisibility(View.GONE);
            // 使用代码设置drawableTop
            Drawable drawable = getResources().getDrawable(R.drawable.icon_place);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            moveView.setCompoundDrawables(null, drawable, null, null);
        } else if (MainActivity.moveBoxBean != null) {
            moveLayerView.setVisibility(View.VISIBLE);
            moveLayout.setVisibility(View.VISIBLE);
            moveGoodsView.setVisibility(View.GONE);
            mainTabLayout.setVisibility(View.GONE);
            // 使用代码设置drawableTop
            Drawable drawable = getResources().getDrawable(R.drawable.icon_move_box);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            moveView.setCompoundDrawables(null, drawable, null, null);
        } else if (MainActivity.moveGoodsList != null && MainActivity.moveGoodsList.size() > 0) {
            moveLayerView.setVisibility(View.VISIBLE);
            moveGoodsView.setVisibility(View.VISIBLE);
            moveLayout.setVisibility(View.GONE);
            mainTabLayout.setVisibility(View.GONE);
            moveGoodsIV.setCircle(MainActivity.moveGoodsList.get(MainActivity.moveGoodsList.size() - 1));
            moveGoodsIV.setTextSize(8);
            if (MainActivity.moveGoodsList.size() > 1) {
                redNumberTv.setVisibility(View.VISIBLE);
                redNumberTv.setText(String.valueOf(MainActivity.moveGoodsList.size()));
            } else {
                redNumberTv.setVisibility(View.GONE);
            }
            if (!SaveDate.getInstence(mContext).getGoodsLocationStub()) {
                new StubDialog(this, R.string.add_goods_location_hint) {
                    @Override
                    public void onDismissDialog() {
                        SaveDate.getInstence(mContext).setGoodsLocationStub(true);
                    }
                };
            }
        } else {
            MainActivity.moveBoxBean = null;
            MainActivity.moveGoodsList = null;
            MainActivity.moveLayerBean = null;
            mainTabLayout.setVisibility(View.VISIBLE);
            moveLayerView.setVisibility(View.GONE);
        }
        initEnergyCode();
    }


    class ContentRunThread implements Runnable {
        @Override
        public void run() {
            // 获取剪贴板数据
            String content = null;
            content = StringUtils.getClipContent(mContext);
            if (content.contains("SN-")) {
                if (content.equals(energyCode)) {
                    return;
                }
                energyCode = content;
                content = content.substring(3, content.length());
                new DialogMessageTwoBtn(MainActivity.this, "检测到剪切板中有领取码，是否领取？", content) {
                    @Override
                    public void submit(String type) {
                        if (fragments != null && fragments.size() > TAB_ME) {
                            getPresenter().getEnergyCode(App.getUser(mContext).getId(), type);
                        }
                    }
                };
            }
        }
    }

    private void onCheckedChanged(int indexof) {
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
    AlertDialog alertDialog;
    boolean isShow;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.GetMessageList msg) {
//        if (!FloatWindowManager.getInstance().applyOrShowFloatWindow(mContext)) {
//            AppConstant.isShowMsg = false;
//            return;
//        }
        if (!hasFocus) {
            return;
        }
        AppConstant.isShowMsg = true;
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
        if (alertDialog != null && alertDialog.isShowing() && !isShow) return;
        isShow = true;
        alertDialog = DialogUtil.showMsg("", messageBean.getContent(), okStr, cancelStr, MainActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConstant.isShowMsg = false;
                isShow = false;
                if (!messageBean.isIs_read()) {
                    getPresenter().dealWithShareRequest(App.getUser(mContext).getId(), okUrl);
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConstant.isShowMsg = false;
                isShow = false;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.SelectHomeTab msg) {
        initTab = true;
        if (msg.isShowEndTimeHint) {
            StringUtils.showMessage(mContext, R.string.add_end_time_hint_text);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.SelectHomeFragmentTab msg) {
        selectTab(AppConstant.DEFAULT_INDEX_OF);
        initEditLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (initData) {
            selectTab(AppConstant.DEFAULT_INDEX_OF);
            initData = false;
        } else if (initTab) {
            initTab = false;
            selectTab(AppConstant.DEFAULT_INDEX_OF);
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
    public void getUserInfoSuccess(UserBean data) {
        if (data != null) {
            SaveDate.getInstence(this).setUser(JsonUtils.jsonFromObject(data));
            App.setUser(data);
            if (data.getEnergy_value() == 0) {
                new BuyEnergyDialog(MainActivity.this, App.getUser(mContext).isIs_vip(), 0);
            }
        }
    }

    private void initEnergyCode() {
        //必须放在 hander的 runable 异步线程中（不能放在Thread  中，有的手机（oppo就会出现）会报线程中不能使用hander 的异常，
//        可能获取剪切板的操作 在不能在Thread线程中使用吧）
        Handler mHandler = new Handler(Looper.getMainLooper());
        //延迟一秒
        mHandler.postDelayed(new ContentRunThread(), 1000);
    }

    @Override
    public void getVersionSuccess(VersionBean bean) {
        if (!TextUtils.isEmpty(bean.getVersion()) && !TextUtils.isEmpty(bean.getNewlyVersion())
                && !bean.getVersion().equals(bean.getNewlyVersion())) {
            manager = DownloadManager.getInstance(MainActivity.this);
            UpdateConfiguration configuration = new UpdateConfiguration()
                    //输出错误日志
                    .setEnableLog(true)
                    //设置自定义的下载
                    //.setHttpManager()
                    //下载完成自动跳动安装页面
                    .setJumpInstallPage(true)
                    //设置对话框背景图片 (图片规范参照demo中的示例图)
                    //.setDialogImage(R.drawable.ic_dialog)
                    //设置按钮的颜色
                    //.setDialogButtonColor(Color.parseColor("#E743DA"))
                    //设置对话框强制更新时进度条和文字的颜色
                    //.setDialogProgressBarColor(Color.parseColor("#E743DA"))
                    //设置按钮的文字颜色
//                    .setDialogButtonTextColor(Color.WHITE)
                    //设置是否显示通知栏进度
                    .setShowNotification(true)
                    //设置是否提示后台下载toast
//                    .setShowBgdToast(false)
                    //设置强制更新
                    .setForcedUpgrade(bean.isForce());
            //设置对话框按钮的点击监听
//                    .setButtonClickListener(this)
            //设置下载过程的监听
//                    .setOnDownloadListener(this);
            int versionCode = ApkUtil.getVersionCode(mContext) + 1;
            manager.setApkName("wherecollect.apk")
                    .setApkUrl(bean.getDownload_url())
                    .setApkVersionCode(versionCode)
                    .setConfiguration(configuration)
                    .setSmallIcon(R.drawable.icon_app_img)
                    .setApkDescription("有新的内容需要更新")
                    .setApkVersionName(bean.getNewlyVersion())
                    .setApkSize("6")
                    .download();
        } else {
            //能量值介绍
            if (!SaveDate.getInstence(mContext).getEnergyHint()) {
                energyView.setVisibility(View.VISIBLE);
            } else {
                getPresenter().getUserInfo(App.getUser(mContext).getId());
            }
        }
    }

    @Override
    public void dealWithShareRequestSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().post(new EventBusMsg.UpdateShareMsg());
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
        }
    }

    @Override
    public void getEnergyCodeSuccess(RequestSuccessBean data) {
        if (data != null) {
            ToastUtil.show(mContext, data.getContent());
            StringUtils.clearClipboard(mContext);
        }
    }

    /**
     * 启动服务
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.StartService msg) {
        //启动Android定时器，并且启动服务
        TimerService.getConnet(this);
    }

    /**
     * 停止服务
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.StopService msg) {
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
        if (result.contains("领取码")) {
            StringUtils.clearClipboard(mContext);
            Toast.makeText(mContext, "领取码不存在或已使用", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            ActivityTaskManager.getInstance().finishAllActivity();
        }
    }

    private void initUM() {
        //获取消息推送代理示例
        //注册推送服务，每次调用register方法都会回调该接口
        PushAgent mPushAgent = PushAgent.getInstance(mContext.getApplicationContext());
        //设置通知栏显示数量
        mPushAgent.setDisplayNotificationNumber(5);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                AppConstant.DEVICE_TOKEN = deviceToken;
//                LogUtil.e("PushAgent register Success:" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                //收到推送
                EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
                systemMode();
                return super.getNotification(context, msg);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
    }


    private void systemMode() {
        //获取声音管理器
        AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        switch (audio.getRingerMode()) {//获取系统设置的铃声模式
            case AudioManager.RINGER_MODE_VIBRATE:
                startVibrator();
                break;
            case AudioManager.RINGER_MODE_NORMAL://声音模式   系统提示音
                startVibrator();
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(this, RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    mp.prepare();
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //震动
    private void startVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次   0一直震动
        if (vibrator != null) {
            vibrator.vibrate(pattern, -1);
        }
    }

    private boolean hasFocus;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.hasFocus = hasFocus;
    }
}
