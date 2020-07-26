package com.gongwu.wherecollect.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.RelationGoodsAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IRelationGoodsContract;
import com.gongwu.wherecollect.contract.presenter.RelationGoodsPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RelationGoodsListActivity extends BaseMvpActivity<RelationGoodsListActivity, RelationGoodsPresenter> implements IRelationGoodsContract.IRelationGoodsView, MyOnItemClickListener {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.relation_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.relation_goods_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.relation_goods_et)
    EditText mEditText;

    private int page = AppConstant.DEFAULT_PAGE;
    private RelationGoodsAdapter mAdapter;
    private List<ObjectBean> mlist = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_relation_goods_list;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        titleTv.setText(R.string.act_title_relation);
        mAdapter = new RelationGoodsAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        initEvent();
    }

    private void initEvent() {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(this);
        }
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                page = AppConstant.DEFAULT_PAGE;
                getPresenter().getRelationGoodsList(App.getUser(mContext).getId(), "all", mEditText.getText().toString(), page);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout mRefreshLayout) {
                page++;
                getPresenter().getRelationGoodsList(App.getUser(mContext).getId(), "all", mEditText.getText().toString(), page);
            }
        });
        //软键盘点击确定
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mRefreshLayout != null) {
                        page = AppConstant.DEFAULT_PAGE;
                        mRefreshLayout.autoRefresh();
                    }
                }
                return false;
            }
        });
        if (mRefreshLayout != null) {
            page = AppConstant.DEFAULT_PAGE;
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected RelationGoodsPresenter createPresenter() {
        return new RelationGoodsPresenter();
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

    @Override
    public void getRelationGoodsSuccess(List<ObjectBean> data) {
        if (page == AppConstant.DEFAULT_PAGE) {
            mlist.clear();
        }
        if (data != null && data.size() > 0) {
            mlist.addAll(data);
        } else {
            if (page > AppConstant.DEFAULT_PAGE) page--;
            ToastUtil.show(mContext, getString(R.string.no_more_data), Toast.LENGTH_SHORT);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getRelationCategoriesSuccess(List<BaseBean> data) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {
        mRefreshLayout.finishRefresh(true);
        mRefreshLayout.finishLoadMore(true);
    }

    @Override
    public void onError(String result) {
        mRefreshLayout.finishRefresh(true);
        mRefreshLayout.finishLoadMore(true);
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int positions, View view) {
        if (mlist != null && mlist.size() > positions) {
            Intent intent = new Intent();
            intent.putExtra("objectBean", mlist.get(positions));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
