package com.gongwu.wherecollect.object;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.SortChildAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddGoodsPropertyContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPropertyPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectSortChildActivity extends BaseMvpActivity<SelectColorActivity, AddGoodsPropertyPresenter> implements IAddGoodsPropertyContract.IAddGoodsPropertyView, MyOnItemClickListener, TextWatcher {
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.serchListView)
    RecyclerView mRecyclerView;
    @BindView(R.id.seach_edit)
    EditText seachEdit;
    @BindView(R.id.clear)
    ImageView clear;

    private ObjectBean objectBean;
    private SortChildAdapter mAdapter;
    private List<ChannelBean> mlist;
    private String baseCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_child_sort;
    }

    @Override
    protected void initViews() {
        mTitleTv.setText("分类");
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        baseCode = objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getCode();
        mlist = new ArrayList<>();
        mAdapter = new SortChildAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        seachEdit.addTextChangedListener(this);

        getPresenter().getCategoryDetails(App.getUser(mContext).getId(), baseCode);
    }

    public static void start(Context mContext, ObjectBean objectBean) {
        Intent intent = new Intent(mContext, SelectSortChildActivity.class);
        intent.putExtra("objectBean", objectBean);
        ((Activity) mContext).startActivityForResult(intent, AddGoodsPropertyActivity.REQUEST_CODE);
    }

    @Override
    protected AddGoodsPropertyPresenter createPresenter() {
        return AddGoodsPropertyPresenter.getInstance();
    }

    @Override
    public void getCategoryDetailsSuccess(List<ChannelBean> data) {
        mlist.clear();
        mlist.addAll(data);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void getSearchSortSuccess(List<ChannelBean> data) {
        mlist.clear();
        mlist.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(int positions, View view) {
        List<BaseBean> list = new ArrayList<>();
        if (mlist.get(positions) != null) {
            list.addAll(mlist.get(positions).getParents());
            BaseBean baseBean = new BaseBean();
            baseBean.setCode(mlist.get(positions).getCode());
            baseBean.setLevel(mlist.get(positions).getLevel());
            baseBean.setName(mlist.get(positions).getName());
            list.add(baseBean);
        }
        objectBean.setCategories(list.size() > 0 ? list : null);
        Intent intent = new Intent();
        intent.putExtra("objectBean", objectBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(seachEdit.getText())) {
            clear.setVisibility(View.GONE);
            getPresenter().getCategoryDetails(App.getUser(mContext).getId(), baseCode);
        } else {
            clear.setVisibility(View.VISIBLE);
            getPresenter().getSearchSort(App.getUser(mContext).getId(), baseCode, seachEdit.getText().toString());
        }
    }

    @Override
    public void getColorsSuccess(List<String> data) {
        //不用管
    }

    @Override
    public void getChannelSuccess(List<ChannelBean> data) {
        //不用管
    }

    @Override
    public void getFirstCategoryListSuccess(List<BaseBean> data) {
        //不用管
    }

    @Override
    public void getChannelListSuccess(List<ChannelBean> data) {
        //不用管
    }

    @Override
    public void addChannelSuccess(ChannelBean data) {
        //不用管
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}