package com.gongwu.wherecollect.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.LayerTemplateActivity;
import com.gongwu.wherecollect.adapter.FamilyRoomAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.contract.IFamilyContract;
import com.gongwu.wherecollect.contract.presenter.FamilyPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.view.EmptyView;
import com.gongwu.wherecollect.view.Loading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * HomeFragment房间布局
 */
public class FamilyRoomFragment extends BaseFragment<FamilyPresenter> implements IFamilyContract.IFamilyView {

    private static final String TAG = "FamilyRoomFragment";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    private String familyCode;
    private RoomBean roomBean;
    private boolean init;
    private Loading loading;
    /**
     * 是否初始化布局
     */
    private boolean isViewInitiated;
    /**
     * 当前界面是否可见
     */
    private boolean isVisibleToUser;
    private boolean initData;
    private boolean isEdit;

    private List<FurnitureBean> mlist;
    private FamilyRoomAdapter mAdapter;


    private FamilyRoomFragment() {
    }

    public static FamilyRoomFragment getInstance(RoomBean roomBean, String familyCode, boolean isEdit) {
        FamilyRoomFragment fm = new FamilyRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("room", roomBean);
        bundle.putSerializable("familyCode", familyCode);
        bundle.putSerializable("isEdit", isEdit);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        isCanLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 是否对用户可见
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            isCanLoadData();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBundle != null) {
            roomBean = (RoomBean) mBundle.getSerializable("room");
            familyCode = mBundle.getString("familyCode");
            isEdit = mBundle.getBoolean("isEdit", false);
        }
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_romm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!init) {
            initUI();
            init = true;
        }
    }

    @Override
    public FamilyPresenter initPresenter() {
        return FamilyPresenter.getInstance();
    }


    private void initUI() {
        mlist = new ArrayList<>();
        mAdapter = new FamilyRoomAdapter(mContext, mlist);
        mAdapter.setEdit(isEdit);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mAdapter);
        mEmptyView.setErrorMsg("");
        mEmptyView.setEmptyImg(mContext, R.drawable.ic_room_empty_furniture);
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (isEdit) {
                    if (listener != null) {
                        listener.onSelectItemClick(roomBean, mAdapter.getSelectLists());
                    }
                } else {
                    FurnitureBean bean = mlist.get(positions);
                    if (bean.getLayers() == null || bean.getLayers().size() == 0) {
                        LayerTemplateActivity.start(mContext, bean, familyCode);
                    } else {
                        FurnitureLookActivity.start(mContext, familyCode, bean, null, roomBean);
                    }
                }
            }
        });
    }

    /*
     * 执行数据加载： 条件是view初始化完成并且对用户可见
     */
    private void isCanLoadData() {
        if (isViewInitiated && isVisibleToUser && !initData) {
            getPresenter().getFurnitureList(App.getUser(mContext).getId(), roomBean.getCode(), familyCode);
            // 加载过数据后，将isViewInitiated和isVisibleToUser设置成false，防止重复加载数据
            isViewInitiated = false;
            isVisibleToUser = false;
            initData = true;
        }
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
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getFurnitureListSuccess(List<FurnitureBean> data) {
        if (mRecyclerView != null && mAdapter != null) {
            mlist.clear();
            if (data != null && data.size() > 0) {
                Collections.sort(data, new Comparator<FurnitureBean>() {
                    @Override
                    public int compare(FurnitureBean lhs, FurnitureBean rhs) {
                        return rhs.getWeight() - lhs.getWeight();
                    }
                });
            }
            mlist.addAll(data);
            mEmptyView.setVisibility(mlist.size() > 0 ? View.GONE : View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    private SelectItemClickListener listener;

    public void setSelectItemClickListener(SelectItemClickListener listener) {
        this.listener = listener;
    }

    public interface SelectItemClickListener {
        void onSelectItemClick(RoomBean roomBean, List<FurnitureBean> mlist);
    }

    @Override
    public void clearSelect() {
        if (mAdapter != null) {
            mAdapter.clearSelect();
        }
    }

    @Override
    public void refreshFragment() {
        getPresenter().getFurnitureList(App.getUser(mContext).getId(), roomBean.getCode(), familyCode);
    }
}
