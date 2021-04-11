package com.gongwu.wherecollect.object;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
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
import com.gongwu.wherecollect.contract.ISelectSortContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPropertyPresenter;
import com.gongwu.wherecollect.contract.presenter.SelectSortPresenter;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.AnimationUtil;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.ActivityTaskManager;
import com.gongwu.wherecollect.view.AddSortDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置物品分类
 */
public class SelectSortActivity extends BaseMvpActivity<SelectColorActivity, SelectSortPresenter> implements ISelectSortContract.ISelectSortView {

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_commit_tv)
    TextView commitTv;
    @BindView(R.id.sort_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.sort_customize_list_view)
    RecyclerView mSortCustomizeListView;
    @BindView(R.id.sort_child_list_view)
    RecyclerView mChildListView;
    @BindView(R.id.sort_child_layout)
    View mChildLayout;
    @BindView(R.id.title_sort_customize_layout)
    View customizeTitleView;
    @BindView(R.id.sort_edit_layout)
    View editViewLayout;
    @BindView(R.id.sort_add_tv)
    TextView addSortTv;
    @BindView(R.id.sort_search_tv)
    TextView sortSeatchTv;
    @BindView(R.id.sort_rename_tv)
    TextView sortRenameTv;
    @BindView(R.id.sort_delete_tv)
    TextView sortDeleteTv;

    private ObjectBean sortBean;
    private SortGridAdapter mAdapter;
    private SortGridAdapter mCustomizeAdapter;
    private SortChildGridAdapter mChildAdapter;
    private List<BaseBean> mlist;
    private List<BaseBean> mCustomizeList;
    private List<ChannelBean> mChildList;
    private BaseBean selectBaseBean;
    private ChannelBean selectChildBean;

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
        mCustomizeList = new ArrayList<>();
        mChildList = new ArrayList<>();

        mAdapter = new SortGridAdapter(this, mlist);
        mAdapter.setSelectBaseBean(selectBaseBean);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mCustomizeAdapter = new SortGridAdapter(this, mCustomizeList);
        mCustomizeAdapter.setSelectBaseBean(selectBaseBean);
        mSortCustomizeListView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSortCustomizeListView.setAdapter(mCustomizeAdapter);

        mChildAdapter = new SortChildGridAdapter(this, mChildList);
        mChildAdapter.setSelectBaseBean(selectChildBean);
        mChildListView.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mChildListView.setAdapter(mChildAdapter);

        getPresenter().getFirstCategoryList(App.getUser(mContext).getId());
        initListener();

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (selectBaseBean == null) {
                    selectBaseBean = mlist.get(positions);
                } else {
                    selectBaseBean = mlist.get(positions).getCode() == selectBaseBean.getCode() ? null : mlist.get(positions);
                }
                mAdapter.setSelectBaseBean(selectBaseBean);
                mCustomizeAdapter.setSelectBaseBean(selectBaseBean);
                showDefaultEditView();
                if (selectBaseBean != null) {
                    getPresenter().getCategoryDetails(App.getUser(mContext).getId(), selectBaseBean.getCode());
                } else {
                    hideChildLayout();
                }
            }
        });
        mCustomizeAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (selectBaseBean == null) {
                    selectBaseBean = mCustomizeList.get(positions);
                } else {
                    selectBaseBean = mCustomizeList.get(positions).getCode() == selectBaseBean.getCode() ? null : mCustomizeList.get(positions);
                }
                mAdapter.setSelectBaseBean(selectBaseBean);
                mCustomizeAdapter.setSelectBaseBean(selectBaseBean);
                showEditView();
                hideChildLayout();
            }
        });
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

    @OnClick({R.id.back_btn, R.id.title_commit_tv, R.id.sort_search_tv, R.id.sort_add_tv, R.id.sort_rename_tv,
            R.id.sort_delete_tv})
    public void onClick(View view) {
        AddSortDialog sortDialog;
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_tv:
                commit();
                break;
            case R.id.sort_search_tv:
                SelectSortChildActivity.start(mContext, sortBean, false);
                break;
            case R.id.sort_add_tv:
                sortDialog = new AddSortDialog(mContext) {
                    @Override
                    public void submitSortName(String value) {
                        if (!TextUtils.isEmpty(value)) {
                            getPresenter().saveCustomCate(App.getUser(mContext).getId(), value);
                        }
                    }
                };
                sortDialog.show();
                break;
            case R.id.sort_rename_tv:
                if (selectBaseBean == null) return;
                sortDialog = new AddSortDialog(mContext) {
                    @Override
                    public void submitSortName(String value) {
                        if (!TextUtils.isEmpty(value)) {
                            getPresenter().editCustomizeName(App.getUser(mContext).getId(), selectBaseBean.get_id(), selectBaseBean.getCode(), value);
                        }
                    }
                };
                sortDialog.show();
                sortDialog.setSortNameTv(selectBaseBean.getName());
                break;
            case R.id.sort_delete_tv:
                if (selectBaseBean == null) return;
                getPresenter().deleteCustomize(App.getUser(mContext).getId(), selectBaseBean.get_id(), selectBaseBean.getCode());
                break;
        }
    }

    private void hideChildLayout() {
        AnimationUtil.downSlide(mChildLayout, 300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChildLayout.setVisibility(View.INVISIBLE);
                editViewLayout.setVisibility(View.VISIBLE);
            }
        }, 300);
    }

    private void showEditView() {
        addSortTv.setVisibility(selectBaseBean != null ? View.GONE : View.VISIBLE);
        sortSeatchTv.setVisibility(selectBaseBean != null ? View.GONE : View.VISIBLE);
        sortRenameTv.setVisibility(selectBaseBean == null ? View.GONE : View.VISIBLE);
        sortDeleteTv.setVisibility(selectBaseBean == null ? View.GONE : View.VISIBLE);
    }

    private void showDefaultEditView() {
        addSortTv.setVisibility(View.VISIBLE);
        sortSeatchTv.setVisibility(View.VISIBLE);
        sortRenameTv.setVisibility(View.GONE);
        sortDeleteTv.setVisibility(View.GONE);
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
        if (list.size() > 0) {
            sortBean.setCategories(list);
        }
        Intent intent = new Intent();
        intent.putExtra("objectBean", sortBean);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected SelectSortPresenter createPresenter() {
        return SelectSortPresenter.getInstance();
    }

    public static void start(Context mContext, ObjectBean sortBean) {
        Intent intent = new Intent(mContext, SelectSortActivity.class);
        intent.putExtra("objectBean", sortBean);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    public void getFirstCategoryListSuccess(List<BaseBean> data) {
        mlist.clear();
        mCustomizeList.clear();
        selectBaseBean = null;
        selectChildBean = null;
        if (data != null && data.size() > 0) {
            for (BaseBean baseBean : data) {
                if (baseBean.isUser()) {
                    mCustomizeList.add(baseBean);
                } else {
                    mlist.add(baseBean);
                }
            }
            customizeTitleView.setVisibility(mCustomizeList.size() > 0 ? View.VISIBLE : View.GONE);
            mSortCustomizeListView.setVisibility(mCustomizeList.size() > 0 ? View.VISIBLE : View.GONE);
        }
        mAdapter.notifyDataSetChanged();
        mCustomizeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCategoryDetailsSuccess(List<ChannelBean> data) {
        if (mChildLayout.getVisibility() == View.INVISIBLE) {
            mChildLayout.setVisibility(View.VISIBLE);
            editViewLayout.setVisibility(View.GONE);
            AnimationUtil.upSlide(mChildLayout, 300);
        }
        mChildList.clear();
        mChildList.addAll(data);
        mChildAdapter.notifyDataSetChanged();
    }

    @Override
    public void saveCustomCateSuccess(BaseBean bean) {
        getPresenter().getFirstCategoryList(App.getUser(mContext).getId());
    }

    @Override
    public void editCustomizeNameSuccess(RequestSuccessBean bean) {
        getPresenter().getFirstCategoryList(App.getUser(mContext).getId());
        ToastUtil.show(mContext, "编辑成功");
        showDefaultEditView();
    }

    @Override
    public void deleteCustomizeSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            getPresenter().getFirstCategoryList(App.getUser(mContext).getId());
            ToastUtil.show(mContext, "删除成功");
            sortBean.setCategories(null);
            showDefaultEditView();
        }
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
    public void onBackPressed() {
        if (mChildLayout.getVisibility() == View.VISIBLE) {
            hideChildLayout();
        } else {
            finish();
        }
    }

}
