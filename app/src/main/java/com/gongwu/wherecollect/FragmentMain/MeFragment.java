package com.gongwu.wherecollect.FragmentMain;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.gongwu.wherecollect.activity.MessageListActivity;
import com.gongwu.wherecollect.activity.PersonActivity;
import com.gongwu.wherecollect.activity.ShareListActivity;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.ShareUtil;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.UserCodeDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页-我的fragment
 */
public class MeFragment extends BaseFragment {

    private static final String TAG = "MeFragment";

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
        if (!init) {
            refreshUi();
            init = true;
        }
        initUI();
    }

    private void initUI() {
        StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.activity_bg));
        StatusBarUtil.setLightStatusBar(getActivity(), true);
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
    }

    @OnClick({R.id.person_iv, R.id.person_details_layout, R.id.start_share_tv, R.id.user_code_iv, R.id.msg_iv, R.id.buy_vip_iv, R.id.feed_back_tv, R.id.user_share_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_iv:
            case R.id.person_details_layout:
                startActivity(new Intent(getActivity(), PersonActivity.class));
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
                BuyVIPActivity.start(mContext);
                break;
            case R.id.feed_back_tv:
                FeedBackActivity.start(mContext);
                break;
            case R.id.user_share_app:
                ShareUtil.openShareDialog(getActivity());
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
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
