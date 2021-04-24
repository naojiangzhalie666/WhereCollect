package com.gongwu.wherecollect.object;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.SelectSeasonAdapter;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置物品季节
 */
public class SelectSeasonActivity extends BaseActivity implements MyOnItemClickListener {
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_commit_tv)
    TextView commitTv;
    @BindView(R.id.select_season_list_view)
    RecyclerView mRecyclerView;

    private List<String> mlist = new ArrayList<>();
    private List<String> selectList = new ArrayList<>();
    private SelectSeasonAdapter mAdapter;
    private ObjectBean objectBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_season;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mlist.clear();
        selectList.clear();
        mTitleTv.setText("季节");
        commitTv.setVisibility(View.VISIBLE);
        mAdapter = new SelectSeasonAdapter(mContext, mlist, selectList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        initSelect();
    }

    private void initSelect() {
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        mlist.add("春");
        mlist.add("夏");
        mlist.add("秋");
        mlist.add("冬");
        if (TextUtils.isEmpty(objectBean.getSeason()))
            return;
        String[] seasons = objectBean.getSeason().split("、");
        for (int i = 0; i < seasons.length; i++) {
            if (mlist.contains(seasons[i])) {
                selectList.add(seasons[i]);
            }
        }
        mAdapter.notifyDataSetChanged();
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
        StringBuilder sb = new StringBuilder();
        if (selectList.contains("春")) {
            sb.append("春");
        }
        if (selectList.contains("夏")) {
            sb.append("夏");
        }
        if (selectList.contains("秋")) {
            sb.append("秋");
        }
        if (selectList.contains("冬")) {
            sb.append("冬");
        }
        objectBean.setSeason(sb.toString());
        Intent intent = new Intent();
        intent.putExtra("objectBean", objectBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(int positions, View view) {
        if (!selectList.contains(mlist.get(positions))) {
            selectList.add(mlist.get(positions));
        } else {
            selectList.remove(mlist.get(positions));
        }
        mAdapter.notifyDataSetChanged();
    }

    public static void start(Context mContext, ObjectBean objectBean) {
        Intent intent = new Intent(mContext, SelectSeasonActivity.class);
        intent.putExtra("objectBean", objectBean);
        ((Activity) mContext).startActivityForResult(intent, AddGoodsPropertyActivity.REQUEST_CODE);
    }

    @Override
    protected void initPresenter() {

    }

}
