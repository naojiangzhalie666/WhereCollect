package com.gongwu.wherecollect.object;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.SelectSortChildAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ISelectSortChildNewContract;
import com.gongwu.wherecollect.contract.presenter.SelectSortChildNewPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.DeleteSortTipsDialog;
import com.gongwu.wherecollect.view.Loading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectSortChildNewActivity extends BaseMvpActivity<SelectSortChildNewActivity, SelectSortChildNewPresenter> implements ISelectSortChildNewContract.ISelectSortChildNewView {

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_commit_bg_main_color_tv)
    TextView mCommitTv;
    @BindView(R.id.main_sort_tv)
    TextView mainSortTv;
    @BindView(R.id.select_sort_child_tv)
    TextView selectSortChildTv;
    @BindView(R.id.sort_one_list_view)
    RecyclerView mOneRecyclerView;
    @BindView(R.id.sort_two_list_view)
    RecyclerView mTwoRecyclerView;
    @BindView(R.id.sort_three_list_view)
    RecyclerView mThreeRecyclerView;
    @BindView(R.id.add_sort_childe_layout)
    View addSortChildeLayout;
    @BindView(R.id.add_sort_childe_et)
    EditText addSortEt;

    private Loading loading;
    private ObjectBean objectBean;
    private SelectSortChildAdapter mOneAdapter;
    private SelectSortChildAdapter mTwoAdapter;
    private SelectSortChildAdapter mThreeAdapter;
    private List<BaseBean> mOneLists = new ArrayList<>();
    private List<BaseBean> mTwoLists = new ArrayList<>();
    private List<BaseBean> mThreeLists = new ArrayList<>();
    private BaseBean selectOneChildBean = null;
    private BaseBean selectTwoChildBean = null;
    private BaseBean selectThreeChildBean = null;

    private int sortLevel = 0;
    private String sortCode = null;
    private BaseBean mainBaseBean;
    private DeleteSortTipsDialog dialog;
    private boolean initSortByType = true;
    private boolean initBelonger = false;
    private String type = "";
    private String addSortStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_sort_child_new;
    }

    @Override
    protected void initViews() {
        mCommitTv.setVisibility(View.VISIBLE);
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        initSortByType = getIntent().getBooleanExtra("initSortByType", true);
        initBelonger = getIntent().getBooleanExtra("initBelonger", false);
        initData();
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.white));
        mOneAdapter = new SelectSortChildAdapter(mContext, mOneLists);
        mOneRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mOneRecyclerView.setAdapter(mOneAdapter);

        mTwoAdapter = new SelectSortChildAdapter(mContext, mTwoLists);
        mTwoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mTwoRecyclerView.setAdapter(mTwoAdapter);

        mThreeAdapter = new SelectSortChildAdapter(mContext, mThreeLists);
        mThreeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mThreeRecyclerView.setAdapter(mThreeAdapter);

        mOneAdapter.setOnItemClickListener(new SelectSortChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                BaseBean baseBean = mOneLists.get(positions);
                if (!TextUtils.isEmpty(baseBean.get_id()) && (initBelonger || !TextUtils.isEmpty(baseBean.getCode()))) {
                    if (selectOneChildBean != null && !TextUtils.isEmpty(selectOneChildBean.get_id()) && selectOneChildBean.get_id().equals(baseBean.get_id()))
                        return;
                    selectOneData(baseBean);
                } else if (!TextUtils.isEmpty(baseBean.getName()) && baseBean.getName().equals("新增")) {
                    if (mainBaseBean == null && initSortByType) return;
                    sortLevel = 1;
                    if (initSortByType) {
                        sortCode = mainBaseBean.getCode();
                    }
                    showAddSortChildLayout();
                }
            }

            @Override
            public void onItemDeleteClick(int positions, View view) {
                sortLevel = 1;
                sortCode = mOneLists.get(positions).getCode();
                deleteSortChildPost(mOneLists.get(positions));
            }
        });

        mTwoAdapter.setOnItemClickListener(new SelectSortChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                BaseBean baseBean = mTwoLists.get(positions);
                if (!TextUtils.isEmpty(baseBean.get_id()) && !TextUtils.isEmpty(baseBean.getCode())) {
                    if (selectTwoChildBean != null && !TextUtils.isEmpty(selectTwoChildBean.getCode()) && selectTwoChildBean.getCode().equals(baseBean.getCode()))
                        return;
                    selectTWOData(baseBean);
                } else if (!TextUtils.isEmpty(baseBean.getName()) && baseBean.getName().equals("新增")) {
                    if (selectOneChildBean == null) return;
                    sortLevel = 2;
                    sortCode = selectOneChildBean.getCode();
                    showAddSortChildLayout();
                }
            }

            @Override
            public void onItemDeleteClick(int positions, View view) {
                sortLevel = 2;
                sortCode = mTwoLists.get(positions).getCode();
                deleteSortChildPost(mTwoLists.get(positions));
            }
        });

        mThreeAdapter.setOnItemClickListener(new SelectSortChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                BaseBean baseBean = mThreeLists.get(positions);
                if (!TextUtils.isEmpty(baseBean.get_id()) && !TextUtils.isEmpty(baseBean.getCode())) {
                    if (selectThreeChildBean != null && !TextUtils.isEmpty(selectThreeChildBean.getCode()) && selectThreeChildBean.getCode().equals(baseBean.getCode()))
                        return;
                    selectThreeData(baseBean);
                } else if (!TextUtils.isEmpty(baseBean.getName()) && baseBean.getName().equals("新增")) {
                    if (selectTwoChildBean == null) return;
                    sortLevel = 3;
                    sortCode = selectTwoChildBean.getCode();
                    showAddSortChildLayout();
                }
            }

            @Override
            public void onItemDeleteClick(int positions, View view) {
                sortLevel = 3;
                sortCode = mThreeLists.get(positions).getCode();
                deleteSortChildPost(mThreeLists.get(positions));
            }
        });

        addSortEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addSortChildPost();
                }
                return false;
            }
        });
    }

    private void initData() {
        StringBuilder sb = new StringBuilder();
        if (initSortByType && objectBean != null && objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
            mTitleTv.setText("添加分类子标签");
            mainBaseBean = objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF);
            mainSortTv.setText(!TextUtils.isEmpty(mainBaseBean.getName()) ? mainBaseBean.getName() : "");
            getPresenter().getSubCategoryList(App.getUser(mContext).getId(), mainBaseBean.getCode(), type);
            for (int i = 0; i < StringUtils.getListSize(objectBean.getCategories()); i++) {
                sb.append(objectBean.getCategories().get(i).getName());
                if (i != objectBean.getCategories().size() - 1) {
                    sb.append("/");
                }
            }
        } else if (initBelonger && !initSortByType) {
            mTitleTv.setText("添加归属人");
            type = AppConstant.BELONGER_TYPE;
            mainSortTv.setVisibility(View.GONE);
            getPresenter().getBelongerList(App.getUser(mContext).getId());
        } else {
            mTitleTv.setText("添加购货渠道");
            type = AppConstant.BUY_TYPE;
            mainSortTv.setVisibility(View.GONE);
            getPresenter().getBuyFirstCategoryList(App.getUser(mContext).getId());
            for (int i = 0; i < StringUtils.getListSize(objectBean.getChannelList()); i++) {
                sb.append(objectBean.getChannelList().get(i));
                if (i != objectBean.getChannelList().size() - 1) {
                    sb.append("/");
                }
            }
        }
        selectSortChildTv.setText(sb.toString());
    }


    @OnClick({R.id.back_btn, R.id.add_sort_childe_submit_tv, R.id.title_commit_bg_main_color_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_sort_childe_submit_tv:
                addSortChildPost();
                break;
            case R.id.title_commit_bg_main_color_tv:
                if (initBelonger) {
                    if (selectOneChildBean != null) {
                        objectBean.setBelonger(selectOneChildBean.getName());
                    } else {
                        ToastUtil.show(mContext, "请选择归属人");
                        return;
                    }
                } else {
                    List<BaseBean> beanList = new ArrayList<>();
                    if (initSortByType) {
                        beanList.add(mainBaseBean);
                    }
                    if (selectOneChildBean != null) {
                        beanList.add(selectOneChildBean);
                    }
                    if (selectTwoChildBean != null) {
                        beanList.add(selectTwoChildBean);
                    }
                    if (selectThreeChildBean != null) {
                        beanList.add(selectThreeChildBean);
                    }
                    if (initSortByType) {
                        objectBean.setCategories(beanList);
                    } else {
                        List<String> channels = new ArrayList<>();
                        for (BaseBean baseBean : beanList) {
                            channels.add(baseBean.getName());
                        }
                        objectBean.setChannel(channels);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("objectBean", objectBean);
                setResult(RESULT_OK, intent);
                finish();
                break;

        }
    }

    /**
     * 新增确定
     */
    private void addSortChildPost() {
        if (!TextUtils.isEmpty(addSortEt.getText().toString().trim())) {
            StringUtils.hideKeyboard(addSortEt);
            addSortStr = addSortEt.getText().toString().trim();
            if (!TextUtils.isEmpty(sortCode)) {
                getPresenter().saveCustomSubCate(App.getUser(mContext).getId(), addSortEt.getText().toString().trim(), sortCode, type);
            } else {
                if (initBelonger) {
                    getPresenter().saveBelonger(App.getUser(mContext).getId(), addSortEt.getText().toString().trim());
                } else {
                    getPresenter().saveCustomCate(App.getUser(mContext).getId(), addSortEt.getText().toString().trim(), type);
                }
            }
            addSortChildeLayout.setVisibility(View.GONE);
            addSortEt.setText("");
        } else {
            ToastUtil.show(mContext, "请输入要添加的名称");
        }
    }

    private void deleteSortChildPost(BaseBean baseBean) {
        dialog = new DeleteSortTipsDialog(mContext) {
            @Override
            public void submitSort() {
                if (initBelonger) {
                    if (selectOneChildBean != null && selectOneChildBean.get_id().equals(baseBean.get_id())) {
                        selectOneChildBean = null;
                        selectSortChildTv.setText("");
                    }
                    getPresenter().deleteBelonger(App.getUser(mContext).getId(), baseBean.get_id());
                } else {
                    getPresenter().deleteCustomize(App.getUser(mContext).getId(), baseBean.get_id(), baseBean.getCode(), type);
                }
            }
        };
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                sortLevel = 0;
            }
        });
        dialog.show();
        if (initBelonger) {
            dialog.setMsgText(R.string.hint_delete_text);
        }
    }

    private void showAddSortChildLayout() {
        addSortChildeLayout.setVisibility(View.VISIBLE);
        if (addSortEt != null) {
            //设置可获得焦点
            addSortEt.setFocusable(true);
            addSortEt.setFocusableInTouchMode(true);
            //请求获得焦点
            addSortEt.requestFocus();
            //调用系统输入法
            StringUtils.showKeyboard(addSortEt);
        }
    }

    @Override
    protected SelectSortChildNewPresenter createPresenter() {
        return SelectSortChildNewPresenter.getInstance();
    }

    @Override
    public void getBelongerListSuccess(List<BaseBean> data) {
        initOneView(data);
    }

    @Override
    public void getBuyFirstCategoryListSuccess(List<BaseBean> data) {
        initOneView(data);
    }

    @Override
    public void getSubCategoryListSuccess(List<BaseBean> data) {
        initOneView(data);
    }

    private void initOneView(List<BaseBean> data) {
        mOneLists.clear();
        if (data != null && data.size() > 0) {
            mOneLists.addAll(data);
        }
        BaseBean baseBean = new BaseBean();
        baseBean.setName("新增");
        mOneLists.add(baseBean);
        mOneAdapter.notifyDataSetChanged();
        if (sortLevel > 0 && !TextUtils.isEmpty(addSortStr)) {
            selectBean(addSortStr, mOneLists);
            mOneRecyclerView.smoothScrollToPosition(mOneLists.size() - 1);
        }
    }

    @Override
    public void getTwoSubCategoryListSuccess(List<BaseBean> data) {
        mTwoLists.clear();
        if (data != null && data.size() > 0) {
            mTwoLists.addAll(data);
        }
        BaseBean baseBean = new BaseBean();
        baseBean.setName("新增");
        mTwoLists.add(baseBean);
        mTwoAdapter.notifyDataSetChanged();
        if (sortLevel > 0 && !TextUtils.isEmpty(addSortStr)) {
            selectBean(addSortStr, mTwoLists);
            mTwoRecyclerView.smoothScrollToPosition(mTwoLists.size() - 1);
        }
    }

    @Override
    public void getThreeSubCategoryListSuccess(List<BaseBean> data) {
        mThreeLists.clear();
        if (data != null && data.size() > 0) {
            mThreeLists.addAll(data);
        }
        BaseBean baseBean = new BaseBean();
        baseBean.setName("新增");
        mThreeLists.add(baseBean);
        mThreeAdapter.notifyDataSetChanged();
        if (sortLevel > 0 && !TextUtils.isEmpty(addSortStr)) {
            selectBean(addSortStr, mThreeLists);
            mThreeRecyclerView.smoothScrollToPosition(mThreeLists.size() - 1);
        }
    }

    @Override
    public void saveCustomSubCateSuccess(BaseBean bean) {
        if (bean == null) return;
        ToastUtil.show(mContext, "添加成功");
        //重新请求添加的那一级列表数据
        initLevelList(sortLevel);
    }

    @Override
    public void saveBelongerSuccess(BaseBean bean) {
        if (bean == null) return;
        ToastUtil.show(mContext, "添加成功");
        //重新请求添加的那一级列表数据
        getPresenter().getBelongerList(App.getUser(mContext).getId());
    }

    private void initLevelList(int level) {
        //新增的那一级
        switch (level) {
            case 1:
                if (initSortByType) {
                    getPresenter().getSubCategoryList(App.getUser(mContext).getId(), mainBaseBean.getCode(), type);
                } else {
                    getPresenter().getBuyFirstCategoryList(App.getUser(mContext).getId());
                }
                break;
            case 2:
                getPresenter().getTwoSubCategoryList(App.getUser(mContext).getId(), selectOneChildBean.getCode(), type);
                break;
            case 3:
                getPresenter().getThreeSubCategoryList(App.getUser(mContext).getId(), selectTwoChildBean.getCode(), type);
                break;
        }
    }

    @Override
    public void deleteCustomizeSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            ToastUtil.show(mContext, "删除成功");
            switch (sortLevel) {
                case 1:
                    if (selectOneChildBean != null && selectOneChildBean.getCode().equals(sortCode)) {
                        selectSortChildTv.setText("");
                        selectOneChildBean = null;
                        mOneAdapter.setSelectBaseBeanNotRefresh(null);
                        mTwoLists.clear();
                        selectTwoChildBean = null;
                        mTwoAdapter.setSelectBaseBean(null);
                        mThreeLists.clear();
                        selectThreeChildBean = null;
                        mThreeAdapter.setSelectBaseBean(null);
                    }
                    sortLevel = 0;
                    getPresenter().getSubCategoryList(App.getUser(mContext).getId(), mainBaseBean.getCode(), type);
                    break;
                case 2:
                    if (selectTwoChildBean != null && selectTwoChildBean.getCode().equals(sortCode)) {
                        selectSortChildTv.setText(selectOneChildBean.getName());
                        mTwoLists.clear();
                        selectTwoChildBean = null;
                        mTwoAdapter.setSelectBaseBeanNotRefresh(null);
                        mThreeLists.clear();
                        selectThreeChildBean = null;
                        mThreeAdapter.setSelectBaseBean(null);
                    }
                    sortLevel = 0;
                    getPresenter().getTwoSubCategoryList(App.getUser(mContext).getId(), selectOneChildBean.getCode(), type);
                    break;
                case 3:
                    if (selectThreeChildBean != null && selectThreeChildBean.getCode().equals(sortCode)) {
                        selectSortChildTv.setText(new StringBuilder(selectOneChildBean.getName()).append("/").append(selectTwoChildBean.getName()).toString());
                        mThreeLists.clear();
                        selectThreeChildBean = null;
                        mThreeAdapter.setSelectBaseBeanNotRefresh(null);
                    }
                    sortLevel = 0;
                    getPresenter().getThreeSubCategoryList(App.getUser(mContext).getId(), selectTwoChildBean.getCode(), type);
                    break;
            }

        }
    }

    @Override
    public void deleteBelongerSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            ToastUtil.show(mContext, "删除成功");
            getPresenter().getBelongerList(App.getUser(mContext).getId());
        }
    }

    /**
     * 高亮新增的item
     */
    private void selectBean(String addSortStr, List<BaseBean> data) {
        for (BaseBean bean : data) {
            if (addSortStr.equals(bean.getName())) {
                bean.setSelect(true);
                sortCode = null;
                switch (sortLevel) {
                    case 1:
                        sortLevel = 0;
                        selectOneData(bean);
                        break;
                    case 2:
                        sortLevel = 0;
                        selectTWOData(bean);
                        break;
                    case 3:
                        sortLevel = 0;
                        selectThreeData(bean);
                        break;
                }
                break;
            }
        }
    }

    /**
     * 高亮第一列表,去请求第二列表数据
     */
    private void selectOneData(BaseBean baseBean) {
        selectOneChildBean = baseBean;
        mOneAdapter.setSelectBaseBean(selectOneChildBean);
        selectTwoChildBean = null;
        mTwoLists.clear();
        mTwoAdapter.setSelectBaseBean(null);
        selectThreeChildBean = null;
        mThreeLists.clear();
        mThreeAdapter.setSelectBaseBean(null);
        selectSortChildTv.setText(selectOneChildBean.getName());
        if (!type.equals(AppConstant.BELONGER_TYPE)) {
            getPresenter().getTwoSubCategoryList(App.getUser(mContext).getId(), selectOneChildBean.getCode(), type);
        }
    }

    /**
     * 高亮第二列表,去请求第三列表数据
     */
    private void selectTWOData(BaseBean baseBean) {
        selectTwoChildBean = baseBean;
        mTwoAdapter.setSelectBaseBean(selectTwoChildBean);
        mThreeLists.clear();
        selectThreeChildBean = null;
        mThreeAdapter.setSelectBaseBean(null);
        selectSortChildTv.setText(new StringBuilder(selectOneChildBean.getName()).append("/").append(selectTwoChildBean.getName()).toString());
        getPresenter().getThreeSubCategoryList(App.getUser(mContext).getId(), selectTwoChildBean.getCode(), type);
    }

    /**
     * 高亮第三表
     */
    private void selectThreeData(BaseBean baseBean) {
        selectThreeChildBean = baseBean;
        mThreeAdapter.setSelectBaseBean(selectThreeChildBean);
        selectSortChildTv.setText(new StringBuilder(selectOneChildBean.getName()).append("/").
                append(selectTwoChildBean.getName()).append("/").append(selectThreeChildBean.getName()).toString());
    }

    /**
     * @param initSortByType true物品分类,false购买渠道
     */
    public static void start(Context mContext, ObjectBean objectBean, boolean initSortByType, boolean initBelonger) {
        Intent intent = new Intent(mContext, SelectSortChildNewActivity.class);
        intent.putExtra("objectBean", objectBean);
        intent.putExtra("initSortByType", initSortByType);
        intent.putExtra("initBelonger", initBelonger);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.START_GOODS_INFO_CODE);
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(loading, mContext);
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {

    }

}
