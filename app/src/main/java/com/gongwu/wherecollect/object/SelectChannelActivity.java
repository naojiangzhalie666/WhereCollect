package com.gongwu.wherecollect.object;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.ChannelListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IAddGoodsPropertyContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPropertyPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.CatagreyListView;
import com.gongwu.wherecollect.view.EmptyView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置物品购买渠道
 */
public class SelectChannelActivity extends BaseMvpActivity<SelectColorActivity, AddGoodsPropertyPresenter> implements IAddGoodsPropertyContract.IAddGoodsPropertyView, TextWatcher, AdapterView.OnItemClickListener {
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_commit_tv)
    TextView commitTv;
    @BindView(R.id.seach_edit)
    EditText seachEdit;
    @BindView(R.id.guishu_txt)
    TextView guishuTxt;
    @BindView(R.id.serchListView)
    ListView serchListView;
    @BindView(R.id.guishuListView)
    CatagreyListView guishuListView;
    @BindView(R.id.clear)
    ImageView clear;
    @BindView(R.id.empty)
    EmptyView empty;

    ChannelListAdapter adapter;

    String selectChannel;
    ObjectBean objectBean;
    List<ChannelBean> mList = new ArrayList<>();
    List<ChannelBean> searchList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_channel;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mTitleTv.setText("选择购买渠道");
        commitTv.setVisibility(View.VISIBLE);
        guishuListView.init(guishuTxt);
        seachEdit.addTextChangedListener(this);
        adapter = new ChannelListAdapter(this, mList);
        serchListView.setAdapter(adapter);
        serchListView.setEmptyView(empty);
        serchListView.setOnItemClickListener(this);
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        initData();
    }

    private void initData() {
        getPresenter().getChannel(App.getUser(mContext).getId());
        if (objectBean == null || TextUtils.isEmpty(objectBean.getChannel())) {
            clear.setVisibility(View.GONE);
            return;
        }
        clear.setVisibility(View.VISIBLE);
        String[] strs = objectBean.getChannel().split(">");
        seachEdit.setText(strs[strs.length - 1]);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length - 1; i++) {
            sb.append(strs[i]);
            if (i != strs.length - 2) {
                sb.append(">");
            }
        }
        guishuTxt.setText(sb.toString());
    }


    @Override
    protected AddGoodsPropertyPresenter createPresenter() {
        return AddGoodsPropertyPresenter.getInstance();
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv, R.id.clear, R.id.guishu_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_tv:
                commit();
                break;
            case R.id.clear:
                seachEdit.setText("");
                break;
            case R.id.guishu_layout:
                guishuListView.setVisibility(View.VISIBLE);
                serchListView.setVisibility(View.GONE);
                guishuListView.lastList();
                break;
        }
    }

    private void commit() {
        if (TextUtils.isEmpty(seachEdit.getText())) {//如果是空的直接结束
            back("");
            return;
        }
        if (!TextUtils.isEmpty(selectChannel)) {//如果直接点击的搜索的分类就直接带结果返回
            back(selectChannel);
            return;
        }
        if (guishuListView.selectGuishu != null && guishuTxt.getText().toString().equals(guishuListView.selectGuishu
                .getString())) {
            getPresenter().addChannel(App.getUser(mContext).getId(), seachEdit.getText().toString(), guishuListView.selectGuishu.getCode());
            return;
        } else if (TextUtils.isEmpty(guishuTxt.getText()) || "自定义".equals(guishuTxt.getText().toString())) {//如果归属没有添加类型
            getPresenter().addChannel(App.getUser(mContext).getId(), seachEdit.getText().toString(), null);
            return;
        }
        finish();
    }

    private void back(String str) {
        List<String> list = new ArrayList<>();
        String[] channerls = str.split(">");
        for (String s : channerls) {
            list.add(s);
        }
        objectBean.setChannel(list);
        Intent intent = new Intent();
        intent.putExtra("objectBean", objectBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getColorsSuccess(List<String> data) {
        //选择颜色接口，SelectChannelActivity不用管
    }

    @Override
    public void getChannelSuccess(List<ChannelBean> data) {
        mList.clear();
        mList.addAll(data);
        adapter.notifyDataSetChanged();
        serchListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getFirstCategoryListSuccess(List<BaseBean> data) {
        //分类接口，SelectChannelActivity不用管
    }

    @Override
    public void getCategoryDetailsSuccess(List<ChannelBean> data) {

    }

    @Override
    public void getChannelListSuccess(List<ChannelBean> data) {
        searchList.clear();
        ChannelBean bean = new ChannelBean();
        bean.setName("自定义");
        searchList.add(bean);
        searchList.addAll(data);
        if (!TextUtils.isEmpty(seachEdit.getText())) {
            adapter.setmList(searchList);
            adapter.notifyDataSetChanged();
            serchListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getSearchSortSuccess(List<ChannelBean> data) {

    }

    @Override
    public void addChannelSuccess(ChannelBean data) {
        back(data.getString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        seachEdit.removeTextChangedListener(this);
        if (TextUtils.isEmpty(adapter.getmList().get(position).get_id())) {//自定义
            guishuTxt.setText(adapter.getmList().get(position).getName());
        } else {
            seachEdit.setText(adapter.getmList().get(position).getName());
            guishuTxt.setText(adapter.getmList().get(position).getParentsString());
            selectChannel = guishuTxt.getText().toString() + ">" + seachEdit.getText().toString();
        }
        guishuListView.resetData();
        seachEdit.addTextChangedListener(this);
        adapter.notifyDataSetChanged();
        serchListView.setVisibility(View.GONE);
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


    public static void start(Context mContext, ObjectBean objectBean) {
        Intent intent = new Intent(mContext, SelectChannelActivity.class);
        intent.putExtra("objectBean", objectBean);
        ((Activity) mContext).startActivityForResult(intent, AddGoodsPropertyActivity.REQUEST_CODE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(seachEdit.getText())) {
            clear.setVisibility(View.GONE);
        } else {
            clear.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(seachEdit.getText())) {
            adapter.setmList(mList);
            adapter.notifyDataSetChanged();
            serchListView.setVisibility(View.VISIBLE);
        } else {
            getPresenter().getChannelList(App.getUser(mContext).getId(), seachEdit.getText().toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
