package com.gongwu.wherecollect.object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.ColorContentAdapter;
import com.gongwu.wherecollect.adapter.ColorGridAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IAddGoodsPropertyContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPropertyPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置物品颜色
 */
public class SelectColorActivity extends BaseMvpActivity<SelectColorActivity, AddGoodsPropertyPresenter> implements IAddGoodsPropertyContract.IAddGoodsPropertyView {

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.contentList)
    RecyclerView contentList;
    @BindView(R.id.add_edit)
    EditText addEdit;
    @BindView(R.id.gridView)
    RecyclerView gridView;
    @BindView(R.id.title_commit_tv)
    TextView commitTv;

    ColorGridAdapter colorAdapter;
    ColorContentAdapter contentAdapter;

    ObjectBean objectBean;
    List<String> mlist = new ArrayList<>();
    List<String> selectList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_color;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mlist.clear();
        selectList.clear();
        mTitleTv.setText("选择颜色");
        commitTv.setVisibility(View.VISIBLE);
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        contentList.setHasFixedSize(true);
        colorAdapter = new ColorGridAdapter(this, mlist, selectList);
        contentAdapter = new ColorContentAdapter(this, selectList, colorAdapter) {
            @Override
            public void deleteItem() {
                setLayout();
            }
        };
        contentList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        contentList.setAdapter(contentAdapter);
        gridView.setLayoutManager(new GridLayoutManager(mContext, 5, LinearLayoutManager.VERTICAL, false));
        gridView.setAdapter(colorAdapter);
        colorAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (selectList.contains(mlist.get(positions))) {
                    selectList.remove(mlist.get(positions));
                } else {
                    selectList.add(mlist.get(positions));
                }
                setLayout();
                colorAdapter.notifyDataSetChanged();
                contentAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (selectList.size() > 0) {
                            contentList.smoothScrollToPosition(selectList.size() - 1);
                        }
                    }
                }, 100);
            }
        });
        initData();
    }

    private void setLayout() {
        LinearLayout.LayoutParams lp;
        if (selectList.size() >= 4) {
            lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        contentList.setLayoutParams(lp);
    }

    private void initData() {
        if (!StringUtils.isEmpty(objectBean.getColors())) {
            selectList.addAll(objectBean.getColors());
            setLayout();
            contentAdapter.notifyDataSetChanged();
        }
        getPresenter().getColors(App.getUser(mContext).getId());
    }


    @Override
    protected AddGoodsPropertyPresenter createPresenter() {
        return AddGoodsPropertyPresenter.getInstance();
    }

    @Override
    public void getColorsSuccess(List<String> data) {
        mlist.addAll(data);
        colorAdapter.notifyDataSetChanged();
    }

    @Override
    public void getChannelSuccess(List<ChannelBean> data) {
        //获取购买渠道接口，SelectColorActivity不用管
    }


    @Override
    public void getCategoryDetailsSuccess(List<ChannelBean> data) {
        //分类接口，SelectColorActivity不用管
    }

    @Override
    public void getChannelListSuccess(List<ChannelBean> data) {
        //搜索购买渠道接口，SelectColorActivity不用管
    }

    @Override
    public void getSearchSortSuccess(List<ChannelBean> data) {

    }

    @Override
    public void saveCustomSubCateSuccess(BaseBean bean) {

    }

    @Override
    public void addChannelSuccess(ChannelBean data) {
        //添加购买渠道接口，SelectColorActivity不用管
    }

    @Override
    public void getFirstCategoryListSuccess(List<BaseBean> data) {
        //分类接口，SelectColorActivity不用管
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_tv:
                commit();
                break;
        }
    }

    private void commit() {
        objectBean.setColor(selectList);
        Intent intent = new Intent();
        intent.putExtra("objectBean", objectBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void start(Context mContext, ObjectBean objectBean) {
        Intent intent = new Intent(mContext, SelectColorActivity.class);
        intent.putExtra("objectBean", objectBean);
        ((Activity) mContext).startActivityForResult(intent, AddGoodsPropertyActivity.REQUEST_CODE);
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
