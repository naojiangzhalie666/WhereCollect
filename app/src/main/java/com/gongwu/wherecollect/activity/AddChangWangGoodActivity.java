package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.SwipeCardViewAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IAddChangWangGoodsContract;
import com.gongwu.wherecollect.contract.presenter.AddChangWangGoodsPresenter;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.swipecardview.SwipeFlingAdapterView;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.Loading;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddChangWangGoodActivity extends BaseMvpActivity<AddChangWangGoodActivity, AddChangWangGoodsPresenter> implements IAddChangWangGoodsContract.IAddChangWangGoodsView, SwipeFlingAdapterView.onFlingListener {

    private static final String TAG = "AddChangWangGoodActivit";

    private static final String HAVA_GOOD = "add";//有
    private static final String NOT_HAVA_GOOD = "view";//没有
    private static final String REGRETS_GOOD = "delete";//反悔
    private static final String BLANK_GOOD = "none";//未选择

    SwipeFlingAdapterView mSwipeView;
    @BindView(R.id.chang_wang_layout)
    RelativeLayout contentLayout;
    @BindView(R.id.image_back)
    ImageButton backView;
    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.goods_type)
    TextView goods_type;
    @BindView(R.id.progressbar_text_view)
    TextView pbarTextView;
    @BindView(R.id.no_good_view)
    TextView no_good_view;
    @BindView(R.id.back_good_view)
    TextView back_good_view;
    @BindView(R.id.yes_good_view)
    TextView yes_good_view;
    @BindView(R.id.progressbar_view)
    ProgressBar mProgressBar;
    @BindView(R.id.cardView_other)
    CardView cardViewOther;
    @BindView(R.id.cardView)
    CardView cardView;


    private List<ObjectBean> changWangList = new ArrayList<>();
    private List<ObjectBean> selectedList = new ArrayList<>();
    private Map<String, ObjectBean> addGoodList = new LinkedHashMap<>();
    private SwipeCardViewAdapter mAdapter;
    private Loading loading;

    private ObjectBean selectGoods;
    private String selectOption;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_chang_wang_good;
    }

    @Override
    protected void initViews() {
        initView();
        initSwipeView();
        getCangWangDetailList();
    }

    @Override
    protected AddChangWangGoodsPresenter createPresenter() {
        return AddChangWangGoodsPresenter.getInstance();
    }

    private void initView() {
        titleView.setText("有没有");
        String goodType = getIntent().getStringExtra("goodType");
        goods_type.setText(goodType);
        backView.setImageDrawable(getResources().getDrawable(R.drawable.icon_card_act_finish));
        mAdapter = new SwipeCardViewAdapter(this, changWangList);
        cardView.setRadius(24);//设置图片圆角的半径大小
        cardView.setCardElevation(8);//设置阴影部分大小
        cardView.setContentPadding(10, 10, 10, 10);//设置图片距离阴影大小
        cardViewOther.setRadius(24);//设置图片圆角的半径大小
        cardViewOther.setCardElevation(8);//设置阴影部分大小
        cardViewOther.setContentPadding(10, 10, 10, 10);//设置图片距离阴影大小
        if (goodType.contains("热门")) cardViewOther.setVisibility(View.GONE);
    }

    private void initSwipeView() {
        if (mSwipeView != null) {
            contentLayout.removeView(mSwipeView);
        }
        mSwipeView = new SwipeFlingAdapterView(mContext);
        mSwipeView.setMaxVisible(4);
        mSwipeView.setMinStackInAdapter(4);
        contentLayout.addView(mSwipeView);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSwipeView.getLayoutParams();
        int topDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        lp.setMargins(0, topDip, 0, 0);
        lp.height = 1600;
        mSwipeView.setLayoutParams(lp);
        mSwipeView.setAdapter(mAdapter);
        mSwipeView.setIsNeedSwipe(true);
        mSwipeView.setFlingListener(this);
    }

    @OnClick({R.id.image_back, R.id.no_good_view, R.id.back_good_view, R.id.yes_good_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.no_good_view:
                mSwipeView.swipeLeft();
                break;
            case R.id.back_good_view:
                if (selectedList.size() > 0) {
                    ObjectBean regrets = selectedList.get(selectedList.size() - 1);
                    regrets.setOpt(BLANK_GOOD);
                    setCangWangDetail(regrets, REGRETS_GOOD);
                } else {
                    ToastUtil.show(this, "暂无物品反悔", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.yes_good_view:
                mSwipeView.swipeRight();
                break;
            default:
                break;
        }
    }

    @Override
    public void removeFirstObjectInAdapter() {
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
        if (dataObject != null) {
            ObjectBean objectBean = (ObjectBean) dataObject;
            objectBean.setOpt(NOT_HAVA_GOOD);
            setCangWangDetail(objectBean, objectBean.getOpt());
        }
    }

    @Override
    public void onRightCardExit(Object dataObject) {
        if (dataObject != null) {
            ObjectBean objectBean = (ObjectBean) dataObject;
            objectBean.setOpt(HAVA_GOOD);
            Lg.getInstance().e(TAG, "onRightCardExit:" + objectBean.getName());
            setCangWangDetail(objectBean, objectBean.getOpt());
        }
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        Lg.getInstance().e("tag", itemsInAdapter + "," + itemsInAdapter);
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {

    }

    //获取常忘物品详情list
    private void getCangWangDetailList() {
        getPresenter().getChangWangGoodsList(App.getUser(mContext).getId(), getIntent().getStringExtra("code"));
    }

    //设置常忘物品有没有
    private void setCangWangDetail(ObjectBean object, String option) {
        selectGoods = object;
        selectOption = option;
        setAllBtnEnable(false);
        mSwipeView.setIsNeedSwipe(false);
        getPresenter().setChangWangDetail(App.getUser(mContext).getId(), object.get_id(), option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        contentLayout.removeView(mSwipeView);
        mSwipeView = null;
    }


    public static void start(Context context, String changWangName, String changWangCode) {
        Intent intent = new Intent(context, AddChangWangGoodActivity.class);
        if (intent != null) {
            intent.putExtra("goodType", changWangName);
            intent.putExtra("code", changWangCode);
        }
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setAllBtnEnable(boolean enable) {
        no_good_view.setEnabled(enable);
        back_good_view.setEnabled(enable);
        yes_good_view.setEnabled(enable);
    }

    @Override
    public void getChangWangGoodsListSuccess(ChangWangListBean objectBeans) {
        changWangList.clear();
        if (objectBeans != null && objectBeans.getObjects().size() > 0) {
            for (ObjectBean objectBean : objectBeans.getObjects()) {
                if (BLANK_GOOD.equals(objectBean.getOpt())) {
                    changWangList.add(objectBean);
                } else {
                    selectedList.add(objectBean);
                }
            }
        }
        mProgressBar.setMax(objectBeans.getObjects().size());
        mProgressBar.setProgress(selectedList.size() + 1);
        pbarTextView.setText(mProgressBar.getProgress() + "/" + mProgressBar.getMax());
        mAdapter.notifyDataSetChanged();
        if (changWangList.size() == 0) {
            finish();
        }
    }

    @Override
    public void setChangWangDetailSuccess(ChangWangDetailBean data) {
        try {
            mSwipeView.setIsNeedSwipe(true);
            Lg.getInstance().e(TAG, "setChangWangDetailSuccess:" + selectGoods.getName());
            //添加
            if (HAVA_GOOD.equals(selectOption)) {
                ObjectBean newBean = new ObjectBean();
                newBean.set_id(selectGoods.get_id());
                newBean.setName(selectGoods.getName());
                newBean.setObject_url(selectGoods.getObject_url());
                newBean.setUpdated_at(selectGoods.getUpdated_at());
                newBean.setCreated_at(selectGoods.getCreated_at());
                newBean.setOpt(HAVA_GOOD);
                addGoodList.put(newBean.getId(), newBean);
                if (!TextUtils.isEmpty(data.getId())) {
                    addGoodList.get(selectGoods.getId()).set_id(data.getId());
                }
            }
            //反悔
            if (REGRETS_GOOD.equals(selectOption)) {
                if (addGoodList.containsKey(selectGoods.getId())) {
                    addGoodList.remove(selectGoods.getId());
                }
                changWangList.add(0, selectGoods);
                selectedList.remove(selectGoods);
                mProgressBar.setProgress(mProgressBar.getMax() - changWangList.size() + 1);
                pbarTextView.setText(mProgressBar.getProgress() + "/" + mProgressBar.getMax());
                initSwipeView();
            }
            //接口成功还没有删除做了标记的物品,反悔的物品不删除
            if (!REGRETS_GOOD.equals(selectOption) && changWangList.size() > 0) {
                mProgressBar.setProgress(mProgressBar.getProgress() + 1);
                pbarTextView.setText(mProgressBar.getProgress() + "/" + mProgressBar.getMax());
                selectedList.add(changWangList.get(0));
                //删除标记物品
                mAdapter.remove(0);
            }
            //删除做了标记的物品后
            if (changWangList.size() == 0) {
                if (addGoodList.size() > 0) {
//                    ImportSelectFurnitureActivity.start(context, new ArrayList<>(addGoodList.values()));
                }
//                EventBus.getDefault().post(new EventBusMsg.updateShareMsg());
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        selectOption = null;
        selectGoods = null;
        setAllBtnEnable(true);
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {
        setAllBtnEnable(true);
    }
}
