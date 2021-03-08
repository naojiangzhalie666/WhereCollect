package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.LayerTemplateListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.ILayerTemplateContract;
import com.gongwu.wherecollect.contract.presenter.LayerTemplatePresenter;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateListBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.Loading;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 推荐家具层级界面
 */
public class LayerTemplateActivity extends BaseMvpActivity<LayerTemplateActivity, LayerTemplatePresenter> implements ILayerTemplateContract.ILayerTemplateView {

    private static final String TAG = "LayerTemplateActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.layer_template_list_view)
    RecyclerView mRecyclerView;

    private String familyCode;
    private FurnitureBean furnitureBean;
    private List<LayerTemplateListBean> mlist;
    private LayerTemplateListAdapter mAdapter;

    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layer_template;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.maincolor));
        titleTv.setText("推荐");
        mlist = new ArrayList<>();
        mAdapter = new LayerTemplateListAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        getPresenter().getTemplateLayerList(App.getUser(mContext).getId(), null);
        mAdapter.setOnItemClickListener(new LayerTemplateListAdapter.OnItemChildClickListener() {
            @Override
            public void onItemClick(FurnitureBean bean) {
                getPresenter().updataFurniture(App.getUser(mContext).getId(), familyCode, furnitureBean.getCode(), JsonUtils.jsonFromObject(bean.getLayers()));
            }
        });

        furnitureBean = (FurnitureBean) getIntent().getSerializableExtra("furnitureBean");
        familyCode = getIntent().getStringExtra("familyCode");
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    @Override
    protected LayerTemplatePresenter createPresenter() {
        return LayerTemplatePresenter.getInstance();
    }

    public void myItemClick(View view) {
        // 获取itemView的位置
        int position = mRecyclerView.getChildAdapterPosition(view);
        Toast.makeText(mContext, "点击了 ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTemplateLayerListSuccess(LayerTemplateBean bean) {
        mlist.clear();
        if (bean.getSuggestTemp() != null && bean.getSuggestTemp().size() > 0) {
            LayerTemplateListBean listBean = new LayerTemplateListBean();
            listBean.setTempList(bean.getSuggestTemp());
            listBean.setName("推荐模板");
            mlist.add(listBean);
        }
        if (bean.getAllTemp() != null && bean.getAllTemp().size() > 0) {
            LayerTemplateListBean listBean = new LayerTemplateListBean();
            listBean.setTempList(bean.getAllTemp());
            listBean.setName("全部模板");
            mlist.add(listBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updataFurnitureSuccess(FurnitureBean bean) {
        if (bean != null) {
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            finish();
        }
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(loading, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public static void start(Context mContext, FurnitureBean furnitureBean, String familyCode) {
        Intent intent = new Intent(mContext, LayerTemplateActivity.class);
        intent.putExtra("furnitureBean", furnitureBean);
        intent.putExtra("familyCode", familyCode);
        mContext.startActivity(intent);
    }
}
