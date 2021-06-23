package com.gongwu.wherecollect.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.azhon.appupdate.utils.ApkUtil;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.view.PrivacyDialog;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.List;

public class SplashActivity extends BaseActivity {

    private boolean init;

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initViews() {
        //判断缓存的版本与当前版本是否一致,不一致弹隐私提示
        int cacheVersion = SaveDate.getInstence(mContext).getCacheVersion();
        if (cacheVersion > 0 && cacheVersion == Config.VERSION) {
            checkPermissionRequestEach();
        } else {
            PrivacyDialog dialog = new PrivacyDialog(mContext) {
                @Override
                public void submit() {
                    SaveDate.getInstence(mContext).setCacheVersion(Config.VERSION);
                    checkPermissionRequestEach();
                }

                @Override
                public void cancel() {
                    finish();
                }
            };
            dialog.show();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(SaveDate.getInstence(mContext).getUser())) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra(LoginActivity.TYPE_SPLASH, true);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, MainActivity.class));
                }
                finish();
            }
        }, 1500);
    }

    private void checkPermissionRequestEach() {
        PermissionX.init(this).permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
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
                //框架BUG 这里会调用2次 可能是 读取一次，相机一起
                if (allGranted && deniedList.size() == 0 && !init) {
                    App.initUM(mContext);
                    startMainActivity();
                    init = true;
                } else if (deniedList.size() > 0) {
                    Toast.makeText(mContext, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
