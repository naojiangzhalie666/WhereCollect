package com.gongwu.wherecollect.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IEditRemindContract;
import com.gongwu.wherecollect.contract.presenter.EditRemindPresenter;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RemindDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.DateUtil;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.EditTextWatcher;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.Loading;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AddRemindActivity extends BaseMvpActivity<AddRemindActivity, EditRemindPresenter> implements IEditRemindContract.IEditRemindView {

    private static final int START_CODE = 0x523;
    private static final int START_REMARKS_CODE = 0x423;

    private static final int CHECK_TRUE_CODE = 1;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_white_tv)
    TextView addRemindFinishedTv;
    @BindView(R.id.add_remind_et)
    EditText mEditText;
    @BindView(R.id.remind_first_switch)
    Switch mFirstSwitch;
    @BindView(R.id.remind_overdue_time_switch)
    Switch mOverdueTimeSwitch;
    @BindView(R.id.remind_time_tv)
    TextView selectTimeTv;
    @BindView(R.id.remind_remarks_content_tx)
    TextView remarksTv;
    @BindView(R.id.remind_goods_layout)
    RelativeLayout addRemindGoodsLayout;
    @BindView(R.id.remind_goods_details_layout)
    RelativeLayout remindGoodsDetailsLayout;
    @BindView(R.id.goods_name_tv)
    TextView goodsNameTv;
    @BindView(R.id.goods_classify_tv)
    TextView goodsClassifyTv;
    @BindView(R.id.goods_location_tv)
    TextView goodsLocationTv;
    @BindView(R.id.edit_remind_detail_layout)
    LinearLayout editDetailLayout;
    @BindView(R.id.edit_remind_submit_tv)
    TextView editSubmitTv;
    @BindView(R.id.goods_location_btn)
    ImageView locationIv;
    @BindView(R.id.remind_goods_img_view)
    GoodsImageView mGoodsImageView;

    private Loading loading;
    private long selectTime = 0;
    private boolean edit = false;

    private ObjectBean selectGoods;
    private RemindDetailsBean detailsBean;
    private ObjectBean locationBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_remind;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        titleTv.setText(R.string.add_remind_title_text);
        remindGoodsDetailsLayout.setVisibility(View.GONE);
        addRemindFinishedTv.setText(R.string.finished_text);
        addRemindFinishedTv.setVisibility(View.VISIBLE);
        mOverdueTimeSwitch.setChecked(true);
        initData();
    }

    private void initData() {
        RemindBean remindBean = (RemindBean) getIntent().getSerializableExtra("remind_bean");
        if (remindBean != null) {
            titleTv.setText(R.string.remind_details_title_text);
            addRemindFinishedTv.setVisibility(View.GONE);
            getPresenter().getRemindDetails(App.getUser(mContext).getId(), remindBean.get_id(), remindBean.getAssociated_object_id());
        } else {
            initEvent();
        }
        selectGoods = (ObjectBean) getIntent().getSerializableExtra("selectGoods");
        if (selectGoods != null) {
            setSelectGoods(selectGoods);
        }
    }

    private void initEvent() {
        //优先处理
        mFirstSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StringUtils.hideKeyboard(mEditText);
                editSubmitBtEnable();
            }
        });
        //过期提醒
        mOverdueTimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StringUtils.hideKeyboard(mEditText);
                editSubmitBtEnable();
            }
        });
        mEditText.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                editSubmitBtEnable();
            }
        });
    }

    private void editSubmitBtEnable() {
        if (!edit && detailsBean != null) {
            edit = true;
            //隐藏编辑操作按钮
            editDetailLayout.setVisibility(View.GONE);
            //显示编辑完成按钮
            addRemindFinishedTv.setVisibility(View.VISIBLE);
            //隐藏位置按钮
            locationIv.setVisibility(View.GONE);
        }
    }

    @Override
    protected EditRemindPresenter createPresenter() {
        return EditRemindPresenter.getInstance();
    }

    @OnClick({R.id.back_btn, R.id.remind_goods_layout, R.id.remind_time_layout,
            R.id.title_commit_white_tv, R.id.remind_remarks_layout,
            R.id.edit_remind_delete_tv, R.id.edit_remind_submit_tv,
            R.id.goods_location_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.remind_goods_layout://关联物品
                startActivityForResult(new Intent(this, RelationGoodsListActivity.class), START_CODE);
                break;
            case R.id.remind_time_layout://提醒时间
                StringUtils.hideKeyboard(mEditText);
                showDateDialog();
                break;
            case R.id.title_commit_white_tv://完成
                submitRemindHttpPost();
                break;
            case R.id.remind_remarks_layout://说明备注
                Intent intent = new Intent(mContext, RemindRemarksActivity.class);
                if (!TextUtils.isEmpty(remarksTv.getText().toString())) {
                    intent.putExtra("remind_remarks", remarksTv.getText().toString());
                }
                startActivityForResult(intent, START_REMARKS_CODE);
                break;
            case R.id.edit_remind_delete_tv://删除
                if (detailsBean != null) {
                    getPresenter().deteleRemind(App.getUser(mContext).getId(), detailsBean.get_id());
                }
                break;
            case R.id.edit_remind_submit_tv://标记已完成
                if (detailsBean != null) {
                    getPresenter().setRemindDone(App.getUser(mContext).getId(), detailsBean.get_id());
                }
                break;
            default:
                break;
        }
    }

    private void submitRemindHttpPost() {
        if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
            ToastUtil.show(mContext, getResources().getString(R.string.add_remind_title_hint), Toast.LENGTH_SHORT);
            return;
        }
        if (selectTime == 0) {
            ToastUtil.show(mContext, getResources().getString(R.string.add_remind_time_hint), Toast.LENGTH_SHORT);
            return;
        } else if (selectTime < System.currentTimeMillis()) {
            ToastUtil.show(mContext, getResources().getString(R.string.add_remind_time_hint_two), Toast.LENGTH_SHORT);
            return;
        }
        String description = remarksTv.getText().toString().trim();
        if (TextUtils.isEmpty(description) && selectGoods != null) {
            description = selectGoods.getName();
        }
        if (detailsBean != null) {
            getPresenter().updateRemind(App.getUser(mContext).getId(), mEditText.getText().toString().trim(),
                    description, selectTime + "",
                    mFirstSwitch.isChecked() ? "1" : "0", mOverdueTimeSwitch.isChecked() ? "1" : "0",
                    selectGoods != null ? selectGoods.getId() : "", selectGoods != null ? selectGoods.getObject_url() : "",
                    AppConstant.DEVICE_TOKEN, detailsBean.get_id());
        } else {
            getPresenter().addRemind(App.getUser(mContext).getId(), mEditText.getText().toString().trim(),
                    description, selectTime + "",
                    mFirstSwitch.isChecked() ? "1" : "0", mOverdueTimeSwitch.isChecked() ? "1" : "0",
                    selectGoods != null ? selectGoods.getId() : "", selectGoods != null ? selectGoods.getObject_url() : "",
                    AppConstant.DEVICE_TOKEN);
        }

    }

    /**
     * 时间选择dialog
     */
    private void showDateDialog() {
        Calendar selectDate = Calendar.getInstance();
        selectDate.setTime(new Date(selectTime == 0 ? System.currentTimeMillis() : selectTime));
        Calendar startDate = Calendar.getInstance();
        startDate.set(DateUtil.getNowYear(), DateUtil.getNowMonthNum(), DateUtil.getNowDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                editSubmitBtEnable();
//                selectTime = date.getTime();
                //去掉分
                selectTime = date.getTime() / 1000 / (60 * 60) * (60 * 60) * 1000;
                selectTimeTv.setText(DateUtil.dateToString(new Date(selectTime), DateUtil.DatePattern.ONLY_MINUTE));
            }
        }).setType(new boolean[]{true, true, true, true, false, false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .isCyclic(false)
                .setDate(selectDate)
                .setRangDate(startDate, null)
                .setLabel("年", "月", "日", "时", "", "")
                .build();
        pvTime.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CODE && resultCode == RESULT_OK) {
            ObjectBean objectBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (objectBean != null) {
                selectGoods = objectBean;
                editSubmitBtEnable();
                setSelectGoods(selectGoods);
                //新建不显示跳转位置按钮
                locationIv.setVisibility(View.GONE);
            }
        }
        if (requestCode == START_REMARKS_CODE && resultCode == RESULT_OK) {
            remarksTv.setText(data.getStringExtra("remind_remarks"));
            if (detailsBean != null && !remarksTv.getText().toString().trim().equals(detailsBean.getDescription())) {
                editSubmitBtEnable();
            }
        }
    }

    public static void start(Context mContext, ObjectBean selectGoods) {
        Intent intent = new Intent(mContext, AddRemindActivity.class);
        if (selectGoods != null) {
            intent.putExtra("selectGoods", selectGoods);
        }
        mContext.startActivity(intent);
    }

    @Override
    public void addRemindSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            finish();
        }
    }

    @Override
    public void updateRemindSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            finish();
        }
    }

    @Override
    public void deteleRemindSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            finish();
        }
    }

    @Override
    public void setRemindDoneSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            finish();
        }
    }

    @Override
    public void getRemindDetailsSuccess(RemindDetailsBean data) {
        editDetailLayout.setVisibility(View.VISIBLE);
        if (((RemindBean) getIntent().getSerializableExtra("remind_bean")).getDone() == 1) {
            editSubmitTv.setVisibility(View.GONE);
        }
        detailsBean = data;
        //设置物品
        if (detailsBean.getAssociated_object() != null) {
            setSelectGoods(detailsBean.getAssociated_object());
            selectGoods = detailsBean.getAssociated_object();
        }
        //标题
        mEditText.setText(detailsBean.getTitle());
        //时间
        if (detailsBean.getTips_time() != 0) {
            selectTimeTv.setText(DateUtil.dateToString(new Date(detailsBean.getTips_time()), DateUtil.DatePattern.ONLY_MINUTE));
            selectTime = detailsBean.getTips_time();
        }
        //优化
        mFirstSwitch.setChecked(detailsBean.getFirst() == CHECK_TRUE_CODE);
        //重复提醒
        mOverdueTimeSwitch.setChecked(detailsBean.getRepeat() == CHECK_TRUE_CODE);
        //备注
        remarksTv.setText(detailsBean.getDescription());
        initEvent();
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
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化关联物品数据
     */
    private void setSelectGoods(ObjectBean selectGoods) {
        //关联物品跳转按钮
        addRemindGoodsLayout.setVisibility(View.GONE);
        //显示物品布局
        remindGoodsDetailsLayout.setVisibility(View.VISIBLE);
        //名称
        goodsNameTv.setText(String.format(getString(R.string.remind_goods_name_text), selectGoods.getName()));
        //位置
        String location = StringUtils.getGoodsLoction(selectGoods);
        goodsLocationTv.setText(String.format(getString(R.string.remind_goods_location_text), location));
        //分类
        goodsClassifyTv.setText(String.format(getString(R.string.remind_goods_classify_text), StringUtils.getGoodsClassify(selectGoods)));
        //判断图片类型 是网络图片还是默认颜色
        if (selectGoods.getObject_url().contains("#")) {
            int resId = Color.parseColor(selectGoods.getObject_url());
            mGoodsImageView.setResourceColor(selectGoods.getName(), resId, 3);
        } else {
            mGoodsImageView.setImg(selectGoods.getObject_url(), 3);
        }
        //标题
        if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
            mEditText.setText(selectGoods.getName());
        }
        //位置跳转按钮显示
        if (!TextUtils.isEmpty(location) && !getString(R.string.not_location_goods).equals(location)) {
            locationBean = selectGoods;
            locationIv.setVisibility(View.VISIBLE);
        } else {
            locationIv.setVisibility(View.GONE);
        }
    }
}
