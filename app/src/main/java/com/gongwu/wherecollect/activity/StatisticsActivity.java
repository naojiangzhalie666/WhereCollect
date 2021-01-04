package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IStatisticsContract;
import com.gongwu.wherecollect.contract.presenter.StatisticsPresenter;
import com.gongwu.wherecollect.net.entity.SerchBean;
import com.gongwu.wherecollect.net.entity.response.StatisticsBean;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 统计
 */
public class StatisticsActivity extends BaseMvpActivity<StatisticsActivity, StatisticsPresenter> implements IStatisticsContract.IStatisticsView {

    private static final String TAG = "StatisticsActivity";
    //全部
    public static final String TYPE_ALL = "all";
    //衣装打扮
    public static final String TYPE_CLOTHES = "clothes";
    //除了衣装打扮
    public static final String TYPE_OTHER = "other";

    private String family_code, code, type;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.return_chart)
    PieChart mReturnChart;
    @BindView(R.id.sort_chart)
    PieChart mSortChart;
    @BindView(R.id.price_chart)
    PieChart mPriceChart;
    @BindView(R.id.time_chart)
    HorizontalBarChart mTimeChart;
    @BindView(R.id.color_chart)
    HorizontalBarChart mColorChart;
    @BindView(R.id.season_chart)
    HorizontalBarChart mSeasonChart;

    @BindView(R.id.return_chart_empty_tv)
    TextView mReturnChartEmptyTv;
    @BindView(R.id.sort_chart_empty_tv)
    TextView mSortChartEmptyTv;
    @BindView(R.id.price_chart_empty_tv)
    TextView mPriceChartEmptyTv;
    @BindView(R.id.time_chart_empty_tv)
    TextView mTimeChartEmptyTv;
    @BindView(R.id.color_chart_empty_tv)
    TextView mColorChartEmptyTv;
    @BindView(R.id.season_chart_empty_tv)
    TextView mSeasonChartEmptyTv;


    @BindView(R.id.return_chart_layout)
    View chartLayout;
    @BindView(R.id.sort_chart_layout)
    View sortLayout;
    @BindView(R.id.price_chart_layout)
    View priceLayout;
    @BindView(R.id.time_chart_layout)
    View timeLayout;
    @BindView(R.id.color_chart_layout)
    View colorLayout;
    @BindView(R.id.season_chart_layout)
    View seasonLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void initViews() {
        titleTv.setText("统计");
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        family_code = getIntent().getStringExtra("family_code");
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        initData();
    }

    private void initData() {
        if (type.equals(TYPE_ALL)) {
            initAllData(App.getUser(mContext).getId(), family_code, code);
        } else if (type.equals(TYPE_CLOTHES)) {
            initClothesData(App.getUser(mContext).getId(), family_code, code);
        } else {
            initOtherData(App.getUser(mContext).getId(), family_code, code);
        }
    }

    @Override
    protected StatisticsPresenter createPresenter() {
        return StatisticsPresenter.getInstance();
    }

    private void initAllData(String uid, String family_code, String code) {
        chartLayout.setVisibility(View.VISIBLE);
        sortLayout.setVisibility(View.VISIBLE);
        getPresenter().getGoodsReturnDetails(uid, family_code, code);
        getPresenter().getGoodsSortDetails(uid, family_code);
    }

    private void initClothesData(String uid, String family_code, String code) {
        chartLayout.setVisibility(View.VISIBLE);
        priceLayout.setVisibility(View.VISIBLE);
        timeLayout.setVisibility(View.VISIBLE);
        colorLayout.setVisibility(View.VISIBLE);
        seasonLayout.setVisibility(View.VISIBLE);
        getPresenter().getGoodsReturnDetails(uid, family_code, code);
        getPresenter().getGoodsPriceDetails(uid, family_code, code);
        getPresenter().getGoodsTimeDetails(uid, family_code, code);
        getPresenter().getGoodsColorsDetails(uid, family_code);
        getPresenter().getGoodsSeasonDetails(uid, family_code);
    }

    private void initOtherData(String uid, String family_code, String code) {
        chartLayout.setVisibility(View.VISIBLE);
        priceLayout.setVisibility(View.VISIBLE);
        timeLayout.setVisibility(View.VISIBLE);
        getPresenter().getGoodsReturnDetails(uid, family_code, code);
        getPresenter().getGoodsPriceDetails(uid, family_code, code);
        getPresenter().getGoodsTimeDetails(uid, family_code, code);
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 归位统计
     */
    @Override
    public void getGoodsReturnDetailsSuccess(List<StatisticsBean> bean) {
        getPresenter().setPieChart(mReturnChart, bean, mReturnChartEmptyTv);
    }

    /**
     * 分类统计
     */
    @Override
    public void getGoodsSortDetailsSuccess(List<StatisticsBean> bean) {
        getPresenter().setPieChart(mSortChart, bean, mSortChartEmptyTv);
    }

    /**
     * 颜色统计
     */
    @Override
    public void getGoodsColorsDetailsSuccess(List<StatisticsBean> bean) {
        getPresenter().setHorizontalBarChart(mColorChart, bean, mColorChartEmptyTv);
    }

    /**
     * 季节统计
     */
    @Override
    public void getGoodsSeasonDetailsSuccess(List<StatisticsBean> bean) {
        getPresenter().setHorizontalBarChart(mSeasonChart, bean, mSeasonChartEmptyTv);
    }

    /**
     * 价格统计
     */
    @Override
    public void getGoodsPriceDetailsSuccess(List<StatisticsBean> bean) {
        getPresenter().setPieChart(mPriceChart, bean, mPriceChartEmptyTv);
    }

    /**
     * 购买时间统计(季度)
     */
    @Override
    public void getGoodsTimeDetailsSuccess(List<StatisticsBean> bean) {
        getPresenter().setHorizontalBarChart(mTimeChart, bean, mTimeChartEmptyTv);
    }

    public static void start(Context context, String family_code, String code, String type) {
        Lg.getInstance().d(TAG, "family_code:" + family_code + ",code:" + code);
        Intent intent = new Intent(context, StatisticsActivity.class);
        if (intent != null) {
            intent.putExtra("family_code", family_code);
            intent.putExtra("code", code);
            intent.putExtra("type", type);
        }
        context.startActivity(intent);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {

    }
}
