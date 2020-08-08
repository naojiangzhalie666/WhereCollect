package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.SerchBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FamilyListBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.SerchListDetailsBean;
import com.gongwu.wherecollect.object.GoodsDetailsActivity;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int GOODS_TYPE = 0;
    private final int CATEGORIES_TYPE = 1;
    private final int LOCATIONS_TYPE = 2;
    private boolean isShowOpenBtn;
    private boolean isOpen;

    private Context mContext;
    private List<SerchListDetailsBean> mlist;

    public SearchListAdapter(Context context, List<SerchListDetailsBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    public void setShowOpenBtnType(boolean isShowOpenBtn) {
        this.isShowOpenBtn = isShowOpenBtn;
    }

    public void setOpenType(boolean isOpen) {
        this.isOpen = isOpen;
    }


    /**
     * 可以返回不同类型加载不同布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mlist != null && mlist.size() > 0) {
            if (mlist.get(position).getType() == GOODS_TYPE) {
                return GOODS_TYPE;
            } else if (mlist.get(position).getType() == CATEGORIES_TYPE) {
                return CATEGORIES_TYPE;
            } else if (mlist.get(position).getType() == LOCATIONS_TYPE) {
                return LOCATIONS_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (GOODS_TYPE == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_goods_layout, parent, false);
            return new GoodsViewHolder(view);
        } else if (CATEGORIES_TYPE == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_categories_layout, parent, false);
            return new CategoriesViewHolder(view);
        } else if (LOCATIONS_TYPE == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_location_layout, parent, false);
            return new LocationsViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_goods_layout, parent, false);
            return new GoodsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsViewHolder) {
            GoodsViewHolder goodsHolder = (GoodsViewHolder) holder;
            goodsHolder.openTv.setVisibility(isShowOpenBtn ? View.VISIBLE : View.GONE);
            goodsHolder.mlist.clear();
            goodsHolder.mlist.addAll(mlist.get(position).getItems());
            if (goodsHolder.mAdapter == null) {
                //初始化内层adapter时,把对应的position传过去
                goodsHolder.mAdapter = new SearchGoodsAdapter(mContext, goodsHolder.mlist);
                goodsHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                goodsHolder.mRecyclerView.setAdapter(goodsHolder.mAdapter);
            } else {
                goodsHolder.mAdapter.notifyDataSetChanged();
            }
            goodsHolder.mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
                @Override
                public void onItemClick(int positions, View view) {
                    GoodsDetailsActivity.start(mContext, goodsHolder.mlist.get(positions));
                }
            });
            goodsHolder.openTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        isOpen = !isOpen;
                        listener.onOpenBtnClick(isOpen);
                        goodsHolder.openTv.setText(isOpen ? R.string.close : R.string.open);
                    }
                }
            });
            goodsHolder.openTv.setText(isOpen ? R.string.close : R.string.open);
        } else if (holder instanceof CategoriesViewHolder) {
            CategoriesViewHolder categoriesViewHolder = (CategoriesViewHolder) holder;
            categoriesViewHolder.mFlowViewGroup.removeAllViews();
            for (ObjectBean bean : mlist.get(position).getItems()) {
                TextView text = (TextView) View.inflate(mContext, R.layout.flow_textview_layout, null);
                categoriesViewHolder.mFlowViewGroup.addView(text);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) text.getLayoutParams();
                lp.bottomMargin = 5;
                lp.topMargin = 5;
                lp.rightMargin = 10;
                lp.leftMargin = 10;
                text.setLayoutParams(lp);
                text.setText(bean.getName());
                text.setBackgroundResource(R.drawable.shape_white_split_r4dp);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onSearchText(text.getText().toString());
                        }
                    }
                });
            }
        } else if (holder instanceof LocationsViewHolder) {
            LocationsViewHolder lHolder = (LocationsViewHolder) holder;
            lHolder.mlist.clear();
            lHolder.mlist.addAll(mlist.get(position).getItems());
            if (lHolder.mAdapter == null) {
                //初始化内层adapter时,把对应的position传过去
                lHolder.mAdapter = new SearchLocationsAdapter(mContext, lHolder.mlist);
                lHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                lHolder.mRecyclerView.setAdapter(lHolder.mAdapter);
            } else {
                lHolder.mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return StringUtils.getListSize(mlist);
    }

    public OnItemChildClickListener listener;

    public interface OnItemChildClickListener {
        void onItemClick(FamilyBean bean);

        void onOpenBtnClick(boolean isOpen);

        void onSearchText(String keyword);
    }

    public void setOnItemClickListener(OnItemChildClickListener listener) {
        this.listener = listener;
    }


    public class GoodsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_open_tv)
        TextView openTv;
        @BindView(R.id.search_goods_list_view)
        RecyclerView mRecyclerView;

        private SearchGoodsAdapter mAdapter;
        private List<ObjectBean> mlist = new ArrayList<>();

        public GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_flow_view_group)
        FlowViewGroup mFlowViewGroup;

        public CategoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class LocationsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_locations_list_view)
        RecyclerView mRecyclerView;

        private SearchLocationsAdapter mAdapter;
        private List<ObjectBean> mlist = new ArrayList<>();

        public LocationsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
