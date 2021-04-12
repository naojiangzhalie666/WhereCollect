package com.gongwu.wherecollect.FragmentMain;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.FeedBackActivity;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.MessageListActivity;
import com.gongwu.wherecollect.activity.PersonActivity;
import com.gongwu.wherecollect.activity.ShareListActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.ShareUtil;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.UserCodeDialog;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页-我的fragment
 */
public class MeFragment extends BaseFragment {

    private static final String TAG = "MeFragment";
    private final int START_CODE = 1012;
    private final int START_BUY_VIP_CODE = 1032;

    @BindView(R.id.buy_vip_iv)
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

    private UserBean user;
    private boolean init;

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
        initUI();
    }

    private void initUI() {
        StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.activity_bg));
        StatusBarUtil.setLightStatusBar(getActivity(), true);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshUi();
    }

    /**
     * 刷新UI
     */
    private void refreshUi() {
        user = App.getUser(getActivity());
        if (user == null) return;
        vipView.setVisibility(user.isIs_vip() ? View.VISIBLE : View.GONE);
        ImageLoader.loadCircle(getActivity(), personIv, user.getAvatar(), R.drawable.ic_user_error);
        userName.setText(user.getNickname());
        userId.setText(String.format(getString(R.string.user_usid_text), user.getUsid()));
        buyVipIv.setVisibility(user.isIs_vip() ? View.GONE : View.VISIBLE);
        privacyPolicyTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        privacyPolicyTv.setVisibility(StringUtils.isTencent(mContext) ? View.VISIBLE : View.GONE);
        privacyPolicyTv.setVisibility(View.GONE);
    }

    @OnClick({R.id.person_iv, R.id.person_details_layout, R.id.qr_code_tv, R.id.start_share_tv, R.id.user_code_iv,
            R.id.msg_iv, R.id.buy_vip_iv, R.id.feed_back_tv, R.id.user_share_app, R.id.privacy_policy_tv, R.id.guider_tv})
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
            case R.id.buy_vip_iv:
                Intent intent1 = new Intent(mContext, BuyVIPActivity.class);
                startActivityForResult(intent1, START_BUY_VIP_CODE);
                break;
            case R.id.feed_back_tv:
                FeedBackActivity.start(mContext);
                break;
            case R.id.user_share_app:
                ShareUtil.openShareDialog(getActivity());
                break;
            case R.id.privacy_policy_tv:
                WebActivity.start(mContext, Config.WEB_PRIVACY_NAME, Config.WEB_PRIVACY_URL);
                break;
            case R.id.guider_tv:
//                WebActivity.start(mContext, Config.WEB_PRIVACY_NAME, Config.WEB_PRIVACY_URL);
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
    public BasePresenter initPresenter() {
        return null;
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

}
