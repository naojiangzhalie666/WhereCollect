package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.AddressTextAdapter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.BaseBean;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

/**
 * Created by zhaojin on 15/11/16.
 */
public class SortChildDialog {
    /**
     * 最大字体
     */
    private static final int maxsize = 24;
    /**
     * 最小字体
     */
    private static final int minsize = 14;

    private int currentIndex;
    private Context mContext;
    private WheelView mOneListView;
    private WheelView mTwoListView;
    private WheelView mThreeListView;
    private TextView addChildView;
    private AddressTextAdapter mOneAdapter;
    private AddressTextAdapter mTwoAdapter;
    private AddressTextAdapter mThreeAdapter;
    private Dialog dialog;

    public SortChildDialog(Context context) {
        this.mContext = context;
        dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.dialog_sort_child_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        initView(view);
        dialog.setContentView(view);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.linearLayout).startAnimation(ani);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void initView(View view) {
        mOneListView = view.findViewById(R.id.sort_one_wheel_view);
        mTwoListView = view.findViewById(R.id.sort_two_wheel_view);
        mThreeListView = view.findViewById(R.id.sort_three_wheel_view);
        addChildView = view.findViewById(R.id.dialog_add_sort_child_tv);
        mOneListView.setVisibleItems(5);
        mTwoListView.setVisibleItems(5);
        mThreeListView.setVisibleItems(5);
        onClick(view);
        addChangingListener();
        addScrollingListener();

    }

    public void initData(List<BaseBean> datas) {
        updateOneList(mOneListView, datas);
    }

    public void initTwoData(List<BaseBean> datas) {
        updateTwoList(mTwoListView, datas);
    }

    public void initThreeData(List<BaseBean> datas) {
        updateThreeList(mThreeListView, datas);
    }

    private void updateOneList(WheelView city, List<BaseBean> cities) {
        mOneAdapter = new AddressTextAdapter(mContext, cities, currentIndex, maxsize, minsize);
        city.setViewAdapter(mOneAdapter);
        city.setCurrentItem(0);
    }

    private void updateTwoList(WheelView city, List<BaseBean> cities) {
        mTwoAdapter = new AddressTextAdapter(mContext, cities, currentIndex, maxsize, minsize);
        city.setViewAdapter(mTwoAdapter);
        if (cities != null && cities.size() > 0) {
            city.setCurrentItem(0);
        }
    }

    private void updateThreeList(WheelView city, List<BaseBean> cities) {
        mThreeAdapter = new AddressTextAdapter(mContext, cities, currentIndex, maxsize, minsize);
        city.setViewAdapter(mThreeAdapter);
        if (cities != null && cities.size() > 0) {
            city.setCurrentItem(0);
        }
    }

    public boolean isShow() {
        if (dialog == null) {
            return false;
        }
        return dialog.isShowing();
    }

    private void onClick(View view) {
        view.findViewById(R.id.dialog_add_sort_child_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSortChildClick();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.dialog_submit_sort_child_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClick(mOneListView.getCurrentItem(), mTwoListView.getCurrentItem(), mThreeListView.getCurrentItem());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 设置字体大小
     */
    private void setItemTextSize(String currentItemText, AddressTextAdapter addressTextAdapter) {
        //获取所有的View
        ArrayList<View> arrayLists = addressTextAdapter.getTextViews();

        int size = arrayLists.size();
        //当前条目的内容
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textView = (TextView) arrayLists.get(i);
            currentText = textView.getText().toString().trim();

            if (currentItemText.equals(currentText)) {
//                textview.setTextSize(maxsize);
                textView.setTextColor(Color.parseColor("#333333"));
            } else {
//                textview.setTextSize(minsize);
                textView.setTextColor(Color.parseColor("#A0A0A0"));
            }
        }
    }

    private void addScrollingListener() {
        mOneListView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                //省滚动完成后切换市
                updateTwoData(mOneListView.getCurrentItem());
                //设置省的字体
                String proviceItemText = mOneAdapter.getName(wheel.getCurrentItem());
                setItemTextSize(proviceItemText, mOneAdapter);
            }
        });
        mTwoListView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                //省滚动完成后切换市
                updateThreeData(mTwoListView.getCurrentItem());
                //设置省的字体
                String proviceItemText = mOneAdapter.getName(wheel.getCurrentItem());
                setItemTextSize(proviceItemText, mOneAdapter);
            }
        });
        mThreeListView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String areaItemText = mThreeAdapter.getName(wheel.getCurrentItem());
                setItemTextSize(areaItemText, mThreeAdapter);
            }
        });
    }

    private void addChangingListener() {
        if (mOneListView != null) {
            mOneListView.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    String areaItemText = mOneAdapter.getName(wheel.getCurrentItem());
                    setItemTextSize(areaItemText, mOneAdapter);
                }
            });
        }
        if (mTwoListView != null) {
            mTwoListView.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    String areaItemText = mTwoAdapter.getName(wheel.getCurrentItem());
                    setItemTextSize(areaItemText, mTwoAdapter);
                }
            });
        }
        if (mThreeListView != null) {
            mThreeListView.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    String areaItemText = mThreeAdapter.getName(wheel.getCurrentItem());
                    setItemTextSize(areaItemText, mThreeAdapter);
                }
            });
        }
    }

    public void updateTwoData(int currentItem) {

    }

    public void updateThreeData(int currentItem) {

    }

    public void addSortChildClick() {

    }

    public void submitClick(int oneCurrentIndex, int twoCurrentIndex, int threeCurrentIndex) {

    }

    public void setType(String type) {
        if (AppConstant.BUY_TYPE.equals(type)) {
            addChildView.setText("添加渠道");
        } else {
            addChildView.setText("添加分类");
        }
    }
}
