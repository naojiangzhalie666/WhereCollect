package com.gongwu.wherecollect.FragmentMain;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.AddRemindActivity;
import com.gongwu.wherecollect.adapter.OnRemindItemClickListener;
import com.gongwu.wherecollect.adapter.RemindListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IRemindContract;
import com.gongwu.wherecollect.contract.presenter.RemindPresenter;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.util.iconNum.SendIconNumUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页-提醒fragment
 */
public class RemindFragment extends BaseFragment<RemindPresenter> implements IRemindContract.IRemindView {

    private static final String CODE_UNFINISH = "0";
    private static final String CODE_FINISHED = "1";
    private static final int START_CODE = 0x981;

    @BindView(R.id.remind_unfinish_layout)
    LinearLayout unfinishLayout;
    @BindView(R.id.remind_finished_layout)
    LinearLayout finishedLayout;
    @BindView(R.id.remind_unfinish_title_layout)
    RelativeLayout unfinishTitleLayout;
    @BindView(R.id.remind_finished_title_layout)
    RelativeLayout finishedTitleLayout;
    @BindView(R.id.remind_unfinish_recycler_view)
    RecyclerView unfinishListView;
    @BindView(R.id.remind_finished_recycler_view)
    RecyclerView finishedListView;
    @BindView(R.id.remind_unfinish_num_text_view)
    TextView unfinishNumTv;
    @BindView(R.id.remind_finished_num_text_view)
    TextView finishedNumTv;
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.remind_un_list_layout)
    RelativeLayout unListLayout;
    @BindView(R.id.remind_list_layout)
    RelativeLayout listLayout;
    @BindView(R.id.empty_un_iv)
    ImageView emptyUnIv;
    @BindView(R.id.empty_iv)
    ImageView emptyIv;
    @BindView(R.id.title_commit_white_tv)
    TextView addRemindView;
    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.back_btn)
    View backBtn;

    private boolean init, loading;
    private String done = CODE_UNFINISH;
    private int page = AppConstant.DEFAULT_PAGE;

    private RemindListAdapter mUnAdapter;
    private RemindListAdapter mAdapter;

    private List<RemindBean> mUnData = new ArrayList<>();
    private List<RemindBean> mData = new ArrayList<>();

    private RemindFragment() {
    }

    public static RemindFragment getInstance() {
        return new RemindFragment();
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remind, container, false);
    }

    @Override
    public RemindPresenter initPresenter() {
        return RemindPresenter.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isAdded()) return;
        if (!init) {

            initUI();
            initEvent();
            init = true;
        }
        initStatusBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRefreshLayout != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    private void initUI() {
        mTitleView.setText(R.string.title_fg_remind);
        backBtn.setVisibility(View.GONE);
        addRemindView.setVisibility(View.VISIBLE);
        unfinishListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        finishedListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mUnAdapter = new RemindListAdapter(getContext(), mUnData);
        mAdapter = new RemindListAdapter(getContext(), mData);
        unfinishListView.setAdapter(mUnAdapter);
        finishedListView.setAdapter(mAdapter);
    }

    private void initEvent() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                page = AppConstant.DEFAULT_PAGE;
                initData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout mRefreshLayout) {
                page++;
                initData();
            }
        });
        mUnAdapter.setOnItemClickListener(new OnRemindItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), AddRemindActivity.class);
                intent.putExtra("remind_bean",  mUnData.get(position));
                startActivityForResult(intent, START_CODE);
            }

            @Override
            public void onItemDeleteClick(int position, View view) {
//                deleteRemind(mUnData.get(position));
            }

            @Override
            public void onItemEditFinishedClick(int position, View view) {
//                setRemindDone(mUnData.get(position));
            }
        });
        mAdapter.setOnItemClickListener(new OnRemindItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), AddRemindActivity.class);
                intent.putExtra("remind_bean",  mData.get(position));
                startActivityForResult(intent, START_CODE);
            }

            @Override
            public void onItemDeleteClick(int position, View view) {
//                deleteRemind(mData.get(position));
            }

            @Override
            public void onItemEditFinishedClick(int position, View view) {
                //完成的list 没有标记完成
            }
        });
    }

    private void initData() {
        getPresenter().getRemindList(App.getUser(mContext).getId(), done, page);
    }

    private void initStatusBar() {
        StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.activity_bg));
        StatusBarUtil.setLightStatusBar(getActivity(), true);
    }

    @OnClick({R.id.title_commit_white_tv, R.id.remind_unfinish_title_layout, R.id.remind_finished_title_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_commit_white_tv:
                AddRemindActivity.start(getContext());
                break;
            case R.id.remind_unfinish_title_layout:
                initViewAndData(false);
                break;
            case R.id.remind_finished_title_layout:
                initViewAndData(true);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化布局和请求数据
     */
    private void initViewAndData(boolean finished) {
        //防止用户反复点击未完成 已完成按钮
        if (loading) return;
        if (finished && done == CODE_FINISHED) return;
        if (!finished && done == CODE_UNFINISH) return;
        unListLayout.setVisibility(finished ? View.GONE : View.VISIBLE);
        listLayout.setVisibility(finished ? View.VISIBLE : View.GONE);
        if (!finished) {//未完成
            LinearLayout.LayoutParams unlp = (LinearLayout.LayoutParams) unfinishLayout.getLayoutParams();
            unlp.height = 0;
            unlp.weight = 1;
            unfinishLayout.setLayoutParams(unlp);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) finishedLayout.getLayoutParams();
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
            finishedLayout.setLayoutParams(lp);
            done = CODE_UNFINISH;
            if (mUnData.size() == 0) emptyUnIv.setVisibility(View.VISIBLE);
        } else {//已完成
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) unfinishLayout.getLayoutParams();
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
            unfinishLayout.setLayoutParams(lp);
            LinearLayout.LayoutParams unlp = (LinearLayout.LayoutParams) finishedLayout.getLayoutParams();
            unlp.height = 0;
            unlp.weight = 1;
            finishedLayout.setLayoutParams(unlp);
            done = CODE_FINISHED;
            if (mData.size() == 0) emptyIv.setVisibility(View.VISIBLE);
        }
        if (mRefreshLayout != null) {
            page = AppConstant.DEFAULT_PAGE;
            mRefreshLayout.autoRefresh();
        }
    }


    @Override
    public void getRemindListSuccess(RemindListBean remindListBean) {
        refreshLayoutFinished();
        //清空数据
        if (page == AppConstant.DEFAULT_PAGE) {
            if (CODE_UNFINISH.equals(done)) {
                mUnData.clear();
            } else {
                mData.clear();
            }
        }
        if (remindListBean != null) {
            unfinishNumTv.setText(remindListBean.getUnDoneCountString());
            finishedNumTv.setText(remindListBean.getDoneCountString());
            //数据填充
            if (remindListBean.getReminds() != null && remindListBean.getReminds().size() > 0) {
                if (CODE_UNFINISH.equals(done)) {
                    mUnData.addAll(remindListBean.getReminds());
                } else {
                    mData.addAll(remindListBean.getReminds());
                }
            } else {
                if (page > AppConstant.DEFAULT_PAGE) {
                    page--;
                }
                ToastUtil.show(getContext(), getString(R.string.no_more_data), Toast.LENGTH_SHORT);
            }
        }
        if (CODE_UNFINISH.equals(done)) {
            setMainActTabRedNum();
            mUnAdapter.notifyDataSetChanged();
            emptyUnIv.setVisibility(mUnData.size() == 0 ? View.VISIBLE : View.GONE);
        } else {
            mAdapter.notifyDataSetChanged();
            emptyIv.setVisibility(mData.size() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void setMainActTabRedNum() {
        if (mUnData.size() > 0) {
            int num = 0;
            for (RemindBean bean : mUnData) {
                if (bean.isTimeout()) {
                    num++;
                }
            }
//            EventBus.getDefault().post(new EventBusMsg.RefreshRemindRedNum(num > 0));
            SendIconNumUtil.sendIconNumNotification(num, (Application) getContext().getApplicationContext());
        }
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {
        refreshLayoutFinished();
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    private void refreshLayoutFinished() {
        loading = false;
        mRefreshLayout.finishRefresh(true);
        mRefreshLayout.finishLoadMore(true);
    }

}
