package com.gongwu.wherecollect.contract.presenter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IStatisticsContract;
import com.gongwu.wherecollect.contract.model.StatisticsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.StatisticsBean;

import java.util.ArrayList;
import java.util.List;

public class StatisticsPresenter extends BasePresenter<IStatisticsContract.IStatisticsView> implements IStatisticsContract.IStatisticsPresenter {

    private IStatisticsContract.IStatisticsModel mModel;

    private StatisticsPresenter() {
        mModel = new StatisticsModel();
    }

    public static StatisticsPresenter getInstance() {
        return new StatisticsPresenter();
    }

    /**
     * 归位统计
     */
    @Override
    public void getGoodsReturnDetails(String uid, String family_code, String code) {
        mModel.getGoodsReturnDetails(uid, family_code, code, new RequestCallback<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsReturnDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    /**
     * 分类统计
     */
    @Override
    public void getGoodsSortDetails(String uid, String family_code) {
        mModel.getGoodsSortDetails(uid, family_code, new RequestCallback<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsSortDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    /**
     * 颜色统计
     */
    @Override
    public void getGoodsColorsDetails(String uid, String family_code) {
        mModel.getGoodsColorsDetails(uid, family_code, new RequestCallback<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsColorsDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    /**
     * 季节统计
     */
    @Override
    public void getGoodsSeasonDetails(String uid, String family_code) {
        mModel.getGoodsSeasonDetails(uid, family_code, new RequestCallback<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsSeasonDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    /**
     * 价格统计
     */
    @Override
    public void getGoodsPriceDetails(String uid, String family_code, String code) {
        mModel.getGoodsPriceDetails(uid, family_code, code, new RequestCallback<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsPriceDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    /**
     * 购买时间统计(季度)
     */
    @Override
    public void getGoodsTimeDetails(String uid, String family_code, String code) {
        mModel.getGoodsTimeDetails(uid, family_code, code, new RequestCallback<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsTimeDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    public void setPieChart(PieChart chart, List<StatisticsBean> bean, TextView emptyView) {
        if (bean != null && bean.size() > 0) {
            boolean isShowChart = true;
            for (StatisticsBean statisticsBean : bean) {
                if (statisticsBean.getCount() > 0) {
                    isShowChart = false;
                    break;
                }
            }
            if (isShowChart) {
                chart.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                return;
            }
            chart.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            chart.setUsePercentValues(false);
            chart.getDescription().setEnabled(false);
            chart.setExtraOffsets(5, 10, 5, 5);
            chart.setDragDecelerationFrictionCoef(0.95f);
            chart.setHoleRadius(0f);
            chart.setTransparentCircleRadius(0f);
            chart.setRotationAngle(0);
            chart.animateY(1400, Easing.EaseInOutQuad);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < bean.size(); i++) {
                if (bean.get(i).getCount() > 0) {
                    entries.add(new PieEntry(bean.get(i).getCount(),
                            bean.get(i).getName()));
                }
            }
            PieDataSet dataSet = new PieDataSet(entries, null);

            dataSet.setDrawIcons(false);
            dataSet.setValueFormatter(new PercentFormatter());
            dataSet.setSliceSpace(1f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);
            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);

            dataSet.setValueLinePart1OffsetPercentage(80f);
            dataSet.setValueLinePart1Length(0.8f);
            dataSet.setValueLinePart2Length(1.6f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            //dataSet.setSelectionShift(0f);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            chart.setData(data);
            // undo all highlights
            chart.highlightValues(null);
            chart.invalidate();
        } else {
            chart.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void setHorizontalBarChart(HorizontalBarChart chart, List<StatisticsBean> bean, TextView emptyView) {
        if (bean != null && bean.size() > 0) {
            boolean isShowChart = true;
            for (StatisticsBean statisticsBean : bean) {
                if (statisticsBean.getCount() > 0) {
                    isShowChart = false;
                    break;
                }
            }

            if (isShowChart) {
                chart.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                return;
            }
            chart.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            //填补空数据,让图表好看点
            if (bean.size() < 3) {
                for (int i = 0; i < (3 - bean.size()); i++) {
                    bean.add(new StatisticsBean("", 0));
                }
            }
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(true);
            chart.getDescription().setEnabled(false);
            chart.setPinchZoom(false);
            chart.setDrawGridBackground(false);
            XAxis xl = chart.getXAxis();
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setDrawAxisLine(true);
            xl.setDrawGridLines(false);
            xl.setGranularity(10f);
            xl.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int index = (int) (value / 10);
                    if (index < bean.size()) {
                        return bean.get(index).getName();
                    } else {
                        return "";
                    }
                }
            });

            YAxis yl = chart.getAxisLeft();
            yl.setDrawAxisLine(false);
            yl.setDrawGridLines(true);
            yl.setTextColor(Color.TRANSPARENT);
            yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

            YAxis yr = chart.getAxisRight();
            yr.setDrawAxisLine(false);
            yr.setDrawGridLines(false);
            yr.setTextColor(Color.TRANSPARENT);
            yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//        yr.setInverted(true);
            chart.setFitBars(true);
            chart.animateY(2500);

            Legend l = chart.getLegend();
            l.setEnabled(false);

//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);

            float barWidth = 6f;
            float spaceForBar = 10f;

            if (bean.size() == 1) {
                barWidth = 0.5f;
            } else if (bean.size() == 2) {
                barWidth = 3f;
            }
            ArrayList<BarEntry> values = new ArrayList<>();

            for (int i = 0; i < bean.size(); i++) {
                values.add(new BarEntry(i * spaceForBar, bean.get(i).getCount()));
            }

            BarDataSet set1;

            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values, null);
                set1.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        if (value == 0) {
                            return "";
                        }
                        return String.valueOf((int) value);
                    }
                });
                set1.setDrawIcons(false);
                set1.setDrawValues(true);
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setBarWidth(barWidth);
                chart.setData(data);
            }
        } else {
            chart.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}
