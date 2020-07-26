package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gongwu.wherecollect.LocationLook.TabLocationView;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.fragment.SharedPersonFragment;
import com.gongwu.wherecollect.fragment.SharedSpaceFragment;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.Lg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareListActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "ShareListActivity";

    private final int START_CODE = 0x189;

    @BindView(R.id.my_share_title_view)
    TabLocationView mTabView;
    @BindView(R.id.my_share_view_page)
    ViewPager mViewPager;
    @BindView(R.id.add_my_share_tv)
    TextView addShareBtn;

    private List<BaseFragment> fragments = null;
    private CustomPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_list;
    }

    @Override
    protected void initViews() {
        fragments = new ArrayList<>();
        List<ObjectBean> beanList = new ArrayList<>();
        fragments.add(new SharedPersonFragment());
        fragments.add(new SharedSpaceFragment());
        ObjectBean tabOne = new ObjectBean();
        tabOne.setName("共享人");
        ObjectBean tabTwo = new ObjectBean();
        tabTwo.setName("共享空间");
        beanList.add(tabOne);
        beanList.add(tabTwo);
        mTabView.init(beanList);
        mAdapter = new CustomPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mTabView.setSelection(AppConstant.DEFAULT_INDEX_OF);
        initEvent();
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(this);
        mTabView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(mTabView.getSelection());
            }
        });
    }


    @OnClick({R.id.share_back_btn, R.id.add_my_share_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_back_btn:
                finish();
                break;
            case R.id.add_my_share_tv:
                AddSharedPersonActivity.start(mContext, START_CODE);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CODE && resultCode == Activity.RESULT_OK) {
            fragments.get(0).refreshFragment();
            fragments.get(1).refreshFragment();
        }
//        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
//            fragments.get(0).refreshFragment();
//        }
//        if (requestCode == 104 && resultCode == Activity.RESULT_OK) {
//            fragments.get(1).refreshFragment();
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTabView.setSelection(position);
        mTabView.adapter.notifyDataSetChanged();
        isShowAddShareBtn(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void isShowAddShareBtn(int position) {
        switch (position) {
            case 0:
                addShareBtn.setVisibility(View.VISIBLE);
                break;
            case 1:
                addShareBtn.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class CustomPagerAdapter extends FragmentPagerAdapter {

        private List<BaseFragment> mFragments;

        public CustomPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
            super(fm);
            this.mFragments = fragments;
            fm.beginTransaction().commitAllowingStateLoss();
        }

        @Override
        public BaseFragment getItem(int position) {
            return this.mFragments.get(position);
        }

        @Override
        public int getCount() {
            return this.mFragments.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    protected void initPresenter() {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ShareListActivity.class);
        context.startActivity(intent);
    }
}
