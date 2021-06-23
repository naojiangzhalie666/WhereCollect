package com.gongwu.wherecollect.base;


import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;

import androidx.core.content.ContextCompat;

import com.gongwu.wherecollect.BuildConfig;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.SaveDate;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePalApplication;

import java.io.File;

/**
 * 基类
 */
public class App extends Application {
    public static String CACHEPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/shouner/";

    private static FamilyBean selectFamilyBean;
    private static UserBean user;

    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
        ClassicsHeader.REFRESH_HEADER_UPDATE = "上次更新 M-d HH:mm";
        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
        IWXAPI api = WXAPIFactory.createWXAPI(this, AppConstant.WX_APP_ID);
        api.registerApp(AppConstant.WX_APP_ID);
//        UMConfigure.setLogEnabled(true);
        UMConfigure.preInit(this, "5a2de126f43e484ca70000ef", null);
        initCache();
        try {//必须加上/否则剪切照片可能会出错
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                builder.detectFileUriExposure();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {//1
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    private void initCache() {
        File file = new File(CACHEPATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void initUM(Context mContext) {
        // 初始化SDK
//        参数1:上下文，必须的参数，不能为空。
//        参数2:【友盟+】 AppKey，非必须参数，如果Manifest文件中已配置AppKey，该参数可以传空，则使用Manifest中配置的AppKey，否则该参数必须传入。
//        参数3:【友盟+】 Channel，非必须参数，如果Manifest文件中已配置Channel，该参数可以传空，则使用Manifest中配置的Channel，否则该参数必须传入，Channel命名请详见Channel渠道命名规范。
//        参数4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机。
//        参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空。
        UMConfigure.init(mContext.getApplicationContext(), "5a2de126f43e484ca70000ef", "",
                UMConfigure.DEVICE_TYPE_PHONE, "de26ade0140020dd98cc6999598a4ff6");
        // 选用LEGACY_AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);

        PlatformConfig.setWeixin(AppConstant.WX_APP_ID, "e1777498993b4eecbc20e9ef8c520c5d");
//        PlatformConfig.setQQZone("1105780975", "YtsbvRT5V9PUaG8X1");
        PlatformConfig.setQQZone("1106091663", "DknG4bIDrqPOQPSa");
        PlatformConfig.setSinaWeibo("2932944667", "ce56f1cd16996a7895964192463a3027", "https://sns.whalecloud" +
                ".com/sina2/callback");
        MobclickAgent.setCatchUncaughtExceptions(true);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(mContext.getApplicationContext()).setShareConfig(config);
    }

    public static UserBean getUser(Context context) {
        if (user == null) {
            user = JsonUtils.objectFromJson(SaveDate.getInstence(context).getUser(), UserBean.class);
        }
        return user;
    }

    public static FamilyBean getSelectFamilyBean() {
        return selectFamilyBean;
    }

    public static void setSelectFamilyBean(FamilyBean familyBean) {
        selectFamilyBean = familyBean;
    }

    public static void setUser(UserBean user) {
        App.user = user;
    }

}
