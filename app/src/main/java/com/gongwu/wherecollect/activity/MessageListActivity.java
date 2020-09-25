package com.gongwu.wherecollect.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.MessageListAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IMessageContract;
import com.gongwu.wherecollect.contract.presenter.MessagePresenter;
import com.gongwu.wherecollect.net.entity.response.MessageBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StringUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageListActivity extends BaseMvpActivity<MessageListActivity, MessagePresenter> implements IMessageContract.IMessageView {
    private static final String TAG = "MessageListActivity";

    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.relation_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.relation_goods_list_view)
    RecyclerView mRecyclerView;


    private int page = AppConstant.DEFAULT_PAGE;
    private List<MessageBean> mlist = new ArrayList<>();
    private MessageListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void initViews() {
        titleLayout.setBackgroundColor(Color.WHITE);
        titleTv.setText(R.string.message_title);
        mAdapter = new MessageListAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                page = AppConstant.DEFAULT_PAGE;
                getPresenter().getMessagesList(App.getUser(mContext).getId(), page);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout mRefreshLayout) {
                page++;
                getPresenter().getMessagesList(App.getUser(mContext).getId(), page);
            }
        });
        mRefreshLayout.autoRefresh();
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                onItemclick(mlist.get(positions));
            }
        });
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            default:
                break;
        }
    }

    String okStr;
    String okUrl;
    String cancelStr;
    String cancelUrl;

    private void onItemclick(MessageBean messageBean) {
        if (messageBean.getButtons().size() > 0) {
            for (int i = 0; i < messageBean.getButtons().size(); i++) {
                if (messageBean.getButtons().get(i).getColor().equals("SUCCESS")) {
                    okStr = messageBean.getButtons().get(i).getText();
                    okUrl = TextUtils.isEmpty(messageBean.getButtons().get(i).getApi_url()) ? "" :
                            messageBean.getButtons().get(i).getApi_url();
                }
                if (messageBean.getButtons().get(i).getColor().equals("DANGER")
                        || messageBean.getButtons().get(i).getColor().equals("DEFAULT")) {
                    cancelStr = messageBean.getButtons().get(i).getText();
                    cancelUrl = TextUtils.isEmpty(messageBean.getButtons().get(i).getApi_url()) ? "" :
                            messageBean.getButtons().get(i).getApi_url();
                }
            }
        } else {
            Lg.getInstance().e(TAG, "消息没有buttons");
            return;
        }

        DialogUtil.show("", messageBean.getContent(), TextUtils.isEmpty(okStr) ? "" : okStr, TextUtils.isEmpty(cancelStr) ? "" : cancelStr, MessageListActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConstant.isShowMsg = false;
                if (!messageBean.isIs_read() && !TextUtils.isEmpty(okUrl)) {
                    getPresenter().dealWithShareRequest(App.getUser(mContext).getId(), okUrl);
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConstant.isShowMsg = false;
                if (!messageBean.isIs_read() && !TextUtils.isEmpty(cancelUrl)) {
                    getPresenter().dealWithShareRequest(App.getUser(mContext).getId(), cancelUrl);
                }
            }
        });
    }

    @Override
    protected MessagePresenter createPresenter() {
        return MessagePresenter.getInstance();
    }

    @Override
    public void getMessagesListSuccess(List<MessageBean> data) {
        if (page == AppConstant.DEFAULT_PAGE) {//如果是第一页就缓存下
            mlist.clear();
        } else {
            if (StringUtils.isEmpty(data)) {
                page--;
                Toast.makeText(mContext, getString(R.string.no_more_data), Toast.LENGTH_SHORT)
                        .show();
            }
        }
        mlist.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dealWithShareRequestSuccess(RequestSuccessBean data) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {
        closeLoading();
    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public void closeLoading() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(true);
            mRefreshLayout.finishLoadMore(true);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }
}
