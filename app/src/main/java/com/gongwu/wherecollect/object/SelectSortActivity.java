package com.gongwu.wherecollect.object;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.SortChildGridAdapter;
import com.gongwu.wherecollect.adapter.SortGridAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddGoodsPropertyContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPropertyPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.AnimationUtil;
import com.gongwu.wherecollect.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置物品分类
 */
public class SelectSortActivity extends BaseMvpActivity<SelectColorActivity, AddGoodsPropertyPresenter> implements IAddGoodsPropertyContract.IAddGoodsPropertyView, MyOnItemClickListener {

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_commit_tv)
    TextView commitTv;
    @BindView(R.id.sort_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.sort_child_list_view)
    RecyclerView mChildListView;
    @BindView(R.id.sort_child_layout)
    View mChildLayout;

    private ObjectBean sortBean;
    private SortGridAdapter mAdapter;
    private SortChildGridAdapter mChildAdapter;
    List<BaseBean> mlist;
    List<ChannelBean> mChildList;
    BaseBean selectBaseBean;
    ChannelBean selectChildBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_sort;
    }

    @Override
    protected void initViews() {
        mTitleTv.setText("分类");
        commitTv.setVisibility(View.VISIBLE);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        sortBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        if (sortBean == null) {
            sortBean = new ObjectBean();
        }
        mlist = new ArrayList<>();
        mChildList = new ArrayList<>();

        mAdapter = new SortGridAdapter(this, mlist);
        mAdapter.setSelectBaseBean(selectBaseBean);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mChildAdapter = new SortChildGridAdapter(this, mChildList);
        mChildAdapter.setSelectBaseBean(selectChildBean);
        mChildListView.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mChildListView.setAdapter(mChildAdapter);

        getPresenter().getFirstCategoryList(App.getUser(mContext).getId());
        initListener();

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(this);
        mChildAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (selectChildBean == null) {
                    selectChildBean = mChildList.get(positions);
                } else {
                    selectChildBean = mChildList.get(positions).getCode() == selectChildBean.getCode() ? null : mChildList.get(positions);
                }
                mChildAdapter.setSelectBaseBean(selectChildBean);
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv, R.id.search_sort_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_tv:
                commit();
                break;
            case R.id.search_sort_layout:
                SelectSortChildActivity.start(mContext, sortBean, false);
                break;
        }
    }

    @Override
    public void onItemClick(int positions, View view) {
        if (selectBaseBean == null) {
            selectBaseBean = mlist.get(positions);
        } else {
            selectBaseBean = mlist.get(positions).getCode() == selectBaseBean.getCode() ? null : mlist.get(positions);
        }
        mAdapter.setSelectBaseBean(selectBaseBean);
        if (selectBaseBean != null) {
            getPresenter().getCategoryDetails(App.getUser(mContext).getId(), selectBaseBean.getCode());
        } else {
            AnimationUtil.downSlide(mChildLayout, 300);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChildLayout.setVisibility(View.INVISIBLE);
                }
            }, 300);
        }

    }

    private void commit() {
        List<BaseBean> list = new ArrayList<>();
        if (selectChildBean != null) {
            list.addAll(selectChildBean.getParents());
            BaseBean baseBean = new BaseBean();
            baseBean.setCode(selectChildBean.getCode());
            baseBean.setLevel(selectChildBean.getLevel());
            baseBean.setName(selectChildBean.getName());
            list.add(baseBean);
        } else if (selectBaseBean != null) {
            list.add(selectBaseBean);
        }
        sortBean.setCategories(list.size() > 0 ? list : null);
        Intent intent = new Intent();
        intent.putExtra("objectBean", sortBean);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected AddGoodsPropertyPresenter createPresenter() {
        return AddGoodsPropertyPresenter.getInstance();
    }

    public static void start(Context mContext, ObjectBean sortBean) {
        Intent intent = new Intent(mContext, SelectSortActivity.class);
        intent.putExtra("objectBean", sortBean);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    public void getFirstCategoryListSuccess(List<BaseBean> data) {
        mlist.clear();
        mlist.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCategoryDetailsSuccess(List<ChannelBean> data) {
        mChildLayout.setVisibility(View.VISIBLE);
        AnimationUtil.upSlide(mChildLayout, 300);
        mChildList.clear();
        mChildList.addAll(data);
        mChildAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AddGoodsPropertyActivity.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            ObjectBean objectBean = (ObjectBean) data.getSerializableExtra("objectBean");
            Intent intent = new Intent();
            intent.putExtra("objectBean", objectBean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void getColorsSuccess(List<String> data) {
        //选择颜色接口，SelectChannelActivity不用管
    }

    @Override
    public void getChannelSuccess(List<ChannelBean> data) {
        //获取购买渠道接口，SelectSortActivity不用管
    }


    @Override
    public void getChannelListSuccess(List<ChannelBean> data) {
        //搜索购买渠道接口，SelectSortActivity不用管
    }

    @Override
    public void getSearchSortSuccess(List<ChannelBean> data) {

    }

    @Override
    public void addChannelSuccess(ChannelBean data) {
        //添加购买渠道接口，SelectSortActivity不用管
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
