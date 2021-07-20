package com.gongwu.wherecollect.FragmentMain;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.ArticleActivity;
import com.gongwu.wherecollect.activity.BuyEnergyActivity;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.FeedBackActivity;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.MessageListActivity;
import com.gongwu.wherecollect.activity.PersonActivity;
import com.gongwu.wherecollect.activity.ShareListActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.IMeContract;
import com.gongwu.wherecollect.contract.presenter.MePresenter;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.AnimationUtil;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.ShareUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.BuyEnergyDialog;
import com.gongwu.wherecollect.view.InputPasswordDialog;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.UserCodeDialog;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页-我的fragment
 */
public class MeFragment extends BaseFragment<MePresenter> implements IMeContract.IMeView {

    private static final String TAG = "MeFragment";
    private final int START_CODE = 1012;
    private final int START_BUY_VIP_CODE = 1032;
    private final int ANIMATION_TIME = 500;

    @BindView(R.id.user_buy_vip_iv)
    ImageView buyVipIv;
    @BindView(R.id.person_iv)
    ImageView personIv;
    @BindView(R.id.vip_iv)
    ImageView vipView;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_id_tv)
    TextView userId;
    @BindView(R.id.privacy_policy_tv)
    TextView privacyPolicyTv;
    @BindView(R.id.user_energy_num_tv)
    TextView userEnergyNumTv;
    @BindView(R.id.view_content)
    LinearLayout viewContent;

    private UserBean user;
    private int vipViewHeight;
    private Loading loading;
    private boolean anim = false;
    private boolean isShowVip = false;
    private Handler handler = new Handler();

    private MeFragment() {
    }

    public static MeFragment getInstance() {
        return new MeFragment();
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isAdded()) return;
        initBar();
        user = App.getUser(getActivity());
    }

    private void initBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshUi();
        getPresenter().getUserInfo(App.getUser(mContext).getId());
        if (user == null || buyVipIv == null) return;
        if (user.isIs_vip()) {
            buyVipIv.setVisibility(View.GONE);
            return;
        } else {
            buyVipIv.setVisibility(View.VISIBLE);
        }
        buyVipIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (buyVipIv == null) return;
                vipViewHeight = buyVipIv.getMeasuredHeight();
                buyVipIv.setVisibility(View.GONE);
                buyVipIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (anim && viewContent != null) {
                    AnimationUtil.downSlide(viewContent, ANIMATION_TIME, vipViewHeight);
                    AnimationUtil.StartTranslateOutside(buyVipIv, true);
                    isShowVip = true;
                    anim = false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null && !user.isIs_vip()) {
            handler.postDelayed(runnable, 800);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (vipViewHeight != 0 && viewContent != null) {
                AnimationUtil.downSlide(viewContent, ANIMATION_TIME, vipViewHeight);
                AnimationUtil.StartTranslateOutside(buyVipIv, true);
                isShowVip = true;
            } else {
                anim = true;
            }
        }
    };

    /**
     * 刷新 UI
     */
    private void refreshUi() {
        if (user == null) return;
        vipView.setVisibility(user.isIs_vip() ? View.VISIBLE : View.GONE);
        ImageLoader.loadCircle(getActivity(), personIv, user.getAvatar(), R.drawable.ic_user_error);
        userName.setText(user.getNickname());
        userId.setText(String.format(getString(R.string.user_usid_text), user.getUsid()));
        userEnergyNumTv.setText(String.format(getString(R.string.definition_energy), String.valueOf(user.getEnergy_value())));
        privacyPolicyTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        privacyPolicyTv.setVisibility(StringUtils.isTencent(mContext) ? View.VISIBLE : View.GONE);
        privacyPolicyTv.setVisibility(View.GONE);
    }

    @OnClick({R.id.person_iv, R.id.person_details_layout, R.id.qr_code_tv, R.id.start_share_tv, R.id.user_code_iv,
            R.id.msg_iv, R.id.user_buy_vip_iv, R.id.feed_back_tv, R.id.user_share_app, R.id.privacy_policy_tv,
            R.id.guider_tv, R.id.user_energy_num_tv, R.id.collection_code_tv, R.id.replenish_energy_tv, R.id.user_energy_num_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_iv:
            case R.id.person_details_layout:
                startActivity(new Intent(getActivity(), PersonActivity.class));
                break;
            case R.id.qr_code_tv:
                Intent intent = new Intent(mContext, CaptureActivity.class);
                intent.putExtra("title", "清单扫码");
                startActivityForResult(intent, START_CODE);
                break;
            case R.id.start_share_tv:
                ShareListActivity.start(mContext);
                break;
            case R.id.user_code_iv:
                Glide.with(getContext()).load(user.getAvatar().equals("http://7xroa4.com1.z0.glb.clouddn.com/default/shounaer_icon.png") ? R.drawable.ic_user_error : user.getAvatar()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        UserCodeDialog userCodeDialog = new UserCodeDialog(getActivity());
                        userCodeDialog.showDialog(user.getUsid(), resource);
                    }
                });
                break;
            case R.id.msg_iv:
                MessageListActivity.start(mContext);
                break;
            case R.id.user_buy_vip_iv:
                Intent intent1 = new Intent(mContext, BuyVIPActivity.class);
                startActivityForResult(intent1, START_BUY_VIP_CODE);
                break;
            case R.id.feed_back_tv:
                FeedBackActivity.start(mContext);
                break;
            case R.id.user_share_app:
                if (user == null) return;
                ShareUtil.openShareDialog(getActivity(), user.getId());
                break;
            case R.id.privacy_policy_tv:
                WebActivity.start(mContext, Config.WEB_PRIVACY_NAME, Config.WEB_PRIVACY_URL);
                break;
            case R.id.guider_tv:
                ArticleActivity.start(mContext);
                break;
            case R.id.user_energy_num_tv:
            case R.id.user_energy_num_iv:
                BuyEnergyActivity.start(mContext);
                break;
            case R.id.collection_code_tv:
                if (user == null) return;
                new InputPasswordDialog((Activity) mContext, "请输入领取码", "【如何获取领取码?】", Config.WEB_COLLECTION_NAME, Config.WEB_COLLECTION_URL) {
                    @Override
                    public void result(String result) {
                        getPresenter().getEnergyCode(user.getId(), result);
                    }
                };
                break;
            case R.id.replenish_energy_tv:
                new BuyEnergyDialog((Activity) mContext, App.getUser(mContext).isIs_vip(), user.getEnergy_value());
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_BUY_VIP_CODE && resultCode == Activity.RESULT_OK) {
            refreshUi();
        }
        if (requestCode == START_CODE && resultCode == CaptureActivity.result) {//扫描的到结果
            String result = data.getStringExtra("result");
            String[] codes = result.split(",");
            String family_code = null;
            String room_id = null;
            String room_code = null;
            String furniture_code = null;
            for (int i = 0; i < codes.length; i++) {
                String string = codes[i];
                if (string.contains("goFurniture:fc")) {
                    furniture_code = string.split("=")[1];
                } else if (string.contains("rd")) {
                    room_id = string.split("=")[1];
                } else if (string.contains("rc")) {
                    room_code = string.split("=")[1];
                } else if (string.contains("fmc")) {
                    family_code = string.split("=")[1];
                }
            }
            if (!TextUtils.isEmpty(family_code)
                    && !TextUtils.isEmpty(room_id)
                    && !TextUtils.isEmpty(room_code)
                    && !TextUtils.isEmpty(furniture_code)) {
                RoomBean roomBean = new RoomBean();
                roomBean.set_id(room_id);
                roomBean.setCode(room_code);
                FurnitureBean furnitureBean = new FurnitureBean();
                furnitureBean.setCode(furniture_code);
                FurnitureLookActivity.start(mContext, family_code, furnitureBean, null, roomBean);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!user.isIs_vip()) {
            AnimationUtil.upSlide(viewContent, ANIMATION_TIME, buyVipIv.getHeight());
            AnimationUtil.StartTranslateOutside(buyVipIv, false);
            isShowVip = false;
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public MePresenter initPresenter() {
        return MePresenter.getInstance();
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
    public void getUserInfoSuccess(UserBean data) {
        if (data != null) {
            user = data;
            refreshUi();
            if (isShowVip && user.isIs_vip()) {
                AnimationUtil.upSlide(viewContent, ANIMATION_TIME, buyVipIv.getHeight());
                AnimationUtil.StartTranslateOutside(buyVipIv, false);
                isShowVip = false;
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }
            }
        }
    }

    @Override
    public void getEnergyCodeSuccess(RequestSuccessBean data) {
        if (data != null) {
            ToastUtil.show(mContext, data.getContent());
            getPresenter().getUserInfo(App.getUser(mContext).getId());
        }
    }
}
