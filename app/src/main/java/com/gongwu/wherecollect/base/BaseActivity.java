package com.gongwu.wherecollect.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.ActivityTaskManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;

/**
 * Description : BaseActivity 基类活动
 */


public abstract class BaseActivity extends FragmentActivity {

    private static int screenWidth;
    private static int screenHeigth;
    private static float screenScale;// px dip比例
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightStatusBar(this, true);
        setContentView(getLayoutId());
        mContext = this;
        ButterKnife.bind(this);
        initPresenter();
        initViews();
        ActivityTaskManager.getInstance().putActivity(this);
        PushAgent.getInstance(mContext).onAppStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        ActivityTaskManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 抽象方法：得到布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 抽象方法：初始化控件，一般在BaseActivity中通过ButterKnife来绑定，所以该方法内部一般我们初始化界面相关的操作
     *
     * @return 控件
     */
    protected abstract void initViews();

    /**
     * 抽象方法：实例化Presenter
     */
    protected abstract void initPresenter();

    /**
     * 启动Fragment
     *
     * @param id       id
     * @param fragment 碎片
     */
    protected void startFragment(int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(id, fragment);
        fragmentTransaction.commit();
    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getScreenHeigth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        screenHeigth = dm.heightPixels;
        return screenHeigth;
    }

    public static float getScreenScale(Activity context) {
        if (screenScale <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            screenScale = dm.scaledDensity;
        }
        return screenScale;
    }

}
