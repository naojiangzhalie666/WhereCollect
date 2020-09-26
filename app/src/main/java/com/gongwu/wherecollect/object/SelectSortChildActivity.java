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
    @BindView(R.id.sort_tv)
    TextView sort_tv;
    @BindView(R.id.sort_child_tv)
    TextView sort_child_tv;
    @BindView(R.id.definition_sort_tv)
    TextView defSortTv;
    @BindView(R.id.clear)
    ImageView clearView;
    @BindView(R.id.list_view_layuout)
    View list_view_layuout;
    @BindView(R.id.hint_layout)
    View hint_layout;


    private ObjectBean objectBean;
    private SortChildAdapter mAdapter;
    private List<ChannelBean> mlist;
    //添加属性-选择分类子标签
    private boolean initSortByChild;
    //添加属性-选择分类子标签
    //一级分类标签
    private String baseCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_child_sort;
    }

    @Override
    protected void initViews() {

        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        initSortByChild = getIntent().getBooleanExtra("initSortByChild", false);
        mTitleTv.setText(initSortByChild ? "分类子标签" : "分类");
        if (!initSortByChild) {
            sort_tv.setText("建议分类");
            sort_child_tv.setText("子标签");
        }
        mlist = new ArrayList<>();
        mAdapter = new SortChildAdapter(mContext, mlist);
        mAdapter.setAdapterType(initSortByChild);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        seachEdit.addTextChangedListener(this);
        if (initSortByChild && objectBean != null && objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
            baseCode = objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getCode();
        }
        if (!TextUtils.isEmpty(baseCode)) {
            getPresenter().getCategoryDetails(App.getUser(mContext).getId(), baseCode);
        }

        clearView.setVisibility(View.GONE);
        list_view_layuout.setVisibility(View.GONE);
        hint_layout.setVisibility(View.GONE);
        //mRecyclerView固定
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * @param mContext        Context
     * @param objectBean      物品实体类
     * @param initSortByChild 是否为分类子标签
     */
    public static void start(Context mContext, ObjectBean objectBean, boolean initSortByChild) {
        Intent intent = new Intent(mContext, SelectSortChildActivity.class);
        intent.putExtra("objectBean", objectBean);
        intent.putExtra("initSortByChild", initSortByChild);
        ((Activity) mContext).startActivityForResult(intent, AddGoodsPropertyActivity.REQUEST_CODE);
    }

    @Override
    protected AddGoodsPropertyPresenter createPresenter() {
        return AddGoodsPropertyPresenter.getInstance();
    }

    @Override
    public void getCategoryDetailsSuccess(List<ChannelBean> data) {
        mlist.clear();
        if (data != null && data.size() > 0) {
            mlist.addAll(data);
            list_view_layuout.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void getSearchSortSuccess(List<ChannelBean> data) {
        mlist.clear();
        if (data != null && data.size() > 0) {
            mlist.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        defSortTv.setText(String.format(getString(R.string.definition_sort), seachEdit.getText().toString().trim()));
    }

    @Override
    public void saveCustomSubCateSuccess(BaseBean bean) {
        if (bean != null) {
            List<BaseBean> list = new ArrayList<>();
            if (initSortByChild && objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
                list.addAll(objectBean.getCategories());
            }
            bean.set_id(bean.getId());
            list.add(bean);
            objectBean.setCategories(list.size() > 0 ? list : null);
            Intent intent = new Intent();
            intent.putExtra("objectBean", objectBean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick({R.id.back_btn, R.id.clear, R.id.definition_sort_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.clear:
                clearView.setVisibility(View.GONE);
                seachEdit.setText("");
                break;
            case R.id.definition_sort_tv:
                if (initSortByChild && !TextUtils.isEmpty(baseCode)) {
                    getPresenter().saveCustomSubCate(App.getUser(mContext).getId(),
                            seachEdit.getText().toString().trim(), baseCode);
                } else {
                    getPresenter().saveCustomCate(App.getUser(mContext).getId(),
                            seachEdit.getText().toString().trim());
                }

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
            clearView.setVisibility(View.GONE);
            list_view_layuout.setVisibility(View.GONE);
            hint_layout.setVisibility(View.GONE);
            defSortTv.setText("");
            if (initSortByChild && !TextUtils.isEmpty(baseCode)) {
                getPresenter().getCategoryDetails(App.getUser(mContext).getId(), baseCode);
            }
        } else {
            clearView.setVisibility(View.VISIBLE);
            list_view_layuout.setVisibility(View.VISIBLE);
            hint_layout.setVisibility(View.VISIBLE);
            getPresenter().getSearchSort(App.getUser(mContext).getId(), seachEdit.getText().toString());
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
