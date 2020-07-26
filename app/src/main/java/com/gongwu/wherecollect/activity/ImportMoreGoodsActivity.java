package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.ImportMoreGoodsAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IImportMoreGoodsContract;
import com.gongwu.wherecollect.contract.presenter.ImportMoreGoodsPresenter;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportMoreGoodsActivity extends BaseMvpActivity<ImportMoreGoodsActivity, ImportMoreGoodsPresenter> implements IImportMoreGoodsContract.IImportMoreGoodsView {
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_black_commit_tv)
    TextView commitTv;
    @BindView(R.id.import_goods_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.import_goods_list)
    RecyclerView mRecyclerView;

    private ImportMoreGoodsAdapter mAdapter;
    private List<ObjectBean> mlist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_more_goods;
    }

    @Override
    protected void initViews() {
        titleTv.setText(R.string.import_goods);
        backBtn.setImageDrawable(getDrawable(R.drawable.icon_card_act_finish));
        commitTv.setVisibility(View.VISIBLE);
        mlist = new ArrayList<>();
        mAdapter = new ImportMoreGoodsAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                getPresenter().getImportGoodsList(App.getUser(mContext).getId(), getIntent().getStringExtra("location_code"));
            }
        });
        mRefreshLayout.autoRefresh();
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                mlist.get(positions).setSelect(!mlist.get(positions).isSelect());
                mAdapter.notifyItemChanged(positions);
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.title_black_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_black_commit_tv:
                commitClick();
                break;
            default:
                break;
        }
    }

    private void commitClick() {
        StringBuilder sb = new StringBuilder();
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                sb.append(bean.get_id()).append(",");
            }
        }
        if (sb.length() != 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        if (TextUtils.isEmpty(sb.toString())) {
            Toast.makeText(mContext, "请勾选要导入的物品", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("location_codes", sb.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected ImportMoreGoodsPresenter createPresenter() {
        return ImportMoreGoodsPresenter.getInstance();
    }

    @Override
    public void getImportGoodsListSuccess(ImportGoodsBean bean) {
        mlist.clear();
        mlist.addAll(bean.getItems());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {
        refreshLayoutFinished();
    }

    @Override
    public void onError(String result) {

    }

    public static void start(Context mContext, String location_code) {
        Intent intent = new Intent(mContext, ImportMoreGoodsActivity.class);
        intent.putExtra("location_code", location_code);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE_OTHER);
    }

    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }
}
