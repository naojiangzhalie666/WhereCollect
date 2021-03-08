package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IEditFurniturePatternContract;
import com.gongwu.wherecollect.contract.presenter.EditFurniturePatternPresenter;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.furniture.ChildView;
import com.gongwu.wherecollect.view.furniture.CustomTableRowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class EditFurniturePatternActivity extends BaseMvpActivity<EditFurniturePatternActivity, EditFurniturePatternPresenter> implements IEditFurniturePatternContract.IEditFurniturePatternView {

    @BindView(R.id.edit_furniture_pattern_layout)
    CustomTableRowLayout tableRowLayout;
    @BindView(R.id.edit_revoke_tv)
    TextView revokeView;
    @BindView(R.id.edit_recovery_tv)
    TextView recoveryView;
    @BindView(R.id.edit_h_split_tv)
    TextView hSplitView;
    @BindView(R.id.edit_v_split_tv)
    TextView vSplitView;
    @BindView(R.id.edit_combine_tv)
    TextView combineView;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_white_tv)
    TextView commitView;

    private Loading loading;
    private String familyCode;
    private FurnitureBean mFurnitureBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_furniture_pattern;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.maincolor));
        StatusBarUtil.setLightStatusBar(this, false);
        commitView.setVisibility(View.VISIBLE);
        familyCode = getIntent().getStringExtra("family_code");
        mFurnitureBean = (FurnitureBean) getIntent().getSerializableExtra("furnitureBean");
        titleTv.setText(TextUtils.isEmpty(mFurnitureBean.getName()) ? "" : mFurnitureBean.getName());
        if (mFurnitureBean != null && mFurnitureBean.getLayers() != null && mFurnitureBean.getLayers().size() > 0) {
            tableRowLayout.init(mFurnitureBean.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng1);
        }

        tableRowLayout.setOnItemClickListener(new CustomTableRowLayout.OnItemClickListener() {
            @Override
            public void itemClick(ChildView view) {
                view.setEditable(!view.isEdit());
                boolean[] status = tableRowLayout.getSelectState();
                setUiStatus(status);
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.edit_revoke_tv, R.id.edit_recovery_tv, R.id.edit_h_split_tv, R.id.edit_v_split_tv, R.id.edit_combine_tv, R.id.title_commit_white_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.edit_revoke_tv:
                tableRowLayout.last();
                break;
            case R.id.edit_recovery_tv:
                tableRowLayout.next();
                break;
            case R.id.edit_h_split_tv:
                tableRowLayout.getSelectBeans().get(0).getCode();
                tableRowLayout.splitUpAndDown();
                setUiStatus(tableRowLayout.getSelectState());
                break;
            case R.id.edit_v_split_tv:
                tableRowLayout.getSelectBeans().get(0).getCode();
                tableRowLayout.splitLeftAndRight();
                setUiStatus(tableRowLayout.getSelectState());
                break;
            case R.id.edit_combine_tv:
                StringBuilder sb = new StringBuilder();
                for (RoomFurnitureBean bean : tableRowLayout.getSelectBeans()) {
                    sb.append(bean.getCode()).append(",");
                }
                sb.delete(sb.length() - 1, sb.length());
                tableRowLayout.merge();
                setUiStatus(tableRowLayout.getSelectState());
                break;
            case R.id.title_commit_white_tv:
                commit();
                break;
        }
        revokeView.setEnabled(tableRowLayout.isCanLast());
        recoveryView.setEnabled(tableRowLayout.isCanNext());
    }

    private void commit() {
        List<Map> layers = new ArrayList<>();
        for (int i = 0; i < StringUtils.getListSize(mFurnitureBean.getLayers()); i++) {
            Map<String, Object> layer = new HashMap<>();
            layer.put("name", mFurnitureBean.getLayers().get(i).getName());
            layer.put("position", mFurnitureBean.getLayers().get(i).getPosition());
            layer.put("scale", mFurnitureBean.getLayers().get(i).getScale());
            layer.put("layer_codes", mFurnitureBean.getLayers().get(i).getCode());
            layers.add(layer);
        }
        getPresenter().updataFurniture(App.getUser(mContext).getId(), familyCode, mFurnitureBean.getCode(), JsonUtils.jsonFromObject(layers), tableRowLayout.getShape());
    }

    /**
     * 设置个按钮状态
     *
     * @param status
     */
    private void setUiStatus(boolean[] status) {
        hSplitView.setEnabled(status[0]);
        vSplitView.setEnabled(status[1]);
        combineView.setEnabled(status[2]);
    }

    @Override
    protected EditFurniturePatternPresenter createPresenter() {
        return EditFurniturePatternPresenter.getInstance();
    }

    public static void start(Context mContext, FurnitureBean furnitureBean, String familyCode) {
        Intent intent = new Intent(mContext, EditFurniturePatternActivity.class);
        intent.putExtra("furnitureBean", furnitureBean);
        intent.putExtra("family_code", familyCode);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    public void updataFurnitureSuccess(FurnitureBean data) {
        if (data != null) {
            Intent intent = new Intent();
            intent.putExtra("furnitureBean", data);
            setResult(RESULT_OK, intent);
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
}
