package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.ArticleListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IArticleContract;
import com.gongwu.wherecollect.contract.presenter.ArticlePresenter;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;
import com.gongwu.wherecollect.util.Lg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ArticleActivity extends BaseMvpActivity<ArticleActivity, ArticlePresenter> implements IArticleContract.IArticleView {
    private static final String TAG = "ArticleActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.article_list_view)
    RecyclerView mRecyclerView;

    private List<ArticleBean> mList;
    private ArticleListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article;
    }

    @Override
    protected void initViews() {
        titleTv.setText("帮助与引导");
        mList = new ArrayList<>();
        mAdapter = new ArticleListAdapter(mContext, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        getPresenter().getArticList();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    @Override
    protected ArticlePresenter createPresenter() {
        return ArticlePresenter.getInstance();
    }

    @Override
    public void getArticListSuccess(List<ArticleBean> data) {
        mList.clear();
        mList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ArticleActivity.class);
        context.startActivity(intent);
    }
}
