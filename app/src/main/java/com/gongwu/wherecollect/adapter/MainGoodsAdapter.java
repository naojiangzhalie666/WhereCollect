package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsDetailsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:主页物品查看列表
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class MainGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEAD_TYPE = 0;
    private final int GOODS_TYPE = 1;

    private Context context;
    private List<ObjectBean> mlist;
    private boolean darklayer;

    public MainGoodsAdapter(Context context, List<ObjectBean> list, boolean darklayer) {
        this.context = context;
        this.mlist = list;
        this.darklayer = darklayer;
    }

    /**
     * 拼接位置
     */
    public String getLoction(ObjectBean bean) {
        if (StringUtils.isEmpty(bean.getLocations())) {
            return "未归位";
        }
        Collections.sort(bean.getLocations(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.getListSize(bean.getLocations()); i++) {
            sb.append(bean.getLocations().get(i).getName());
            if (i != bean.getLocations().size() - 1) {
                sb.append("/");
            }
        }
        return sb.length() == 0 ? "未归位" : sb.toString();
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
            if (mlist.get(position).isHead()) {
                return HEAD_TYPE;
            } else {
                return GOODS_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (HEAD_TYPE == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_look_goods_list_layout, parent, false);
            return new HeadViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_goods_layout, parent, false);
            return new CustomViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headHolder = (HeadViewHolder) holder;
            ObjectBean headBean = mlist.get(i);
            if (headBean.isHead()) {
                headHolder.toalTv.setText(String.valueOf(headBean.getTotal()));
                headHolder.noLocationTv.setText(String.valueOf(headBean.getNoLocation()));
            } else {
                headHolder.toalTv.setText("");
                headHolder.noLocationTv.setText("");
            }
            headHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(i, view);
                    }
                }
            });
        } else if (holder instanceof CustomViewHolder) {
            CustomViewHolder goodsHolder = (CustomViewHolder) holder;
            goodsHolder.swipeMenuLayout.setIos(false);//设置是否开启IOS阻塞式交互
            goodsHolder.swipeMenuLayout.setLeftSwipe(true);//true往左滑动,false为往右侧滑动
            goodsHolder.swipeMenuLayout.setSwipeEnable(true);//设置侧滑功能开关
            ObjectBean bean = mlist.get(i);
            goodsHolder.mImgView.name.setText(null);
            goodsHolder.mImgView.head.setBackground(null);
            goodsHolder.mImgView.head.setImageDrawable(null);
            if (bean.getObject_url().contains("http")) {
                goodsHolder.mImgView.setImg(bean.getObject_url(), 3);
            } else {
                int resId = Color.parseColor(bean.getObject_url());
                goodsHolder.mImgView.setResourceColor(bean.getName(), resId, 3);
            }
            goodsHolder.nameTv.setText(bean.getName());
            goodsHolder.locationNameTv.setText(getLoction(bean));
            if (darklayer) {
                goodsHolder.topTv.setVisibility(View.GONE);
                goodsHolder.unlockTv.setVisibility(View.VISIBLE);
            } else {
                goodsHolder.topTv.setVisibility(View.VISIBLE);
                goodsHolder.unlockTv.setVisibility(View.GONE);
            }
            if ("未归位".equals(goodsHolder.locationNameTv.getText().toString())) {
                goodsHolder.addLocationTv.setVisibility(View.VISIBLE);
                goodsHolder.locationTv.setVisibility(View.GONE);
            } else {
                goodsHolder.addLocationTv.setVisibility(View.GONE);
                goodsHolder.locationTv.setVisibility(View.VISIBLE);
            }
            goodsHolder.deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        if (goodsHolder.swipeMenuLayout != null)
                            goodsHolder.swipeMenuLayout.smoothClose();
                        onItemClickListener.onDeleteClick(i, goodsHolder.itemView);
                    }
                }
            });
            goodsHolder.addLocationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onAddLocationClick(i, goodsHolder.itemView);
                    }
                }
            });
            goodsHolder.locationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        if (goodsHolder.swipeMenuLayout != null)
                            goodsHolder.swipeMenuLayout.smoothClose();
                        onItemClickListener.onLocationClick(i, goodsHolder.itemView);
                    }
                }
            });
            goodsHolder.topTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        if (goodsHolder.swipeMenuLayout != null)
                            goodsHolder.swipeMenuLayout.smoothClose();
                        onItemClickListener.onTopClick(i, goodsHolder.itemView);
                    }
                }
            });
            goodsHolder.unlockTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        if (goodsHolder.swipeMenuLayout != null)
                            goodsHolder.swipeMenuLayout.smoothClose();
                        onItemClickListener.onUnlickClick(i, goodsHolder.itemView);
                    }
                }
            });
            goodsHolder.itemGoodsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(i, view);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_number_tv)
        TextView toalTv;
        @BindView(R.id.goods_not_location_tv)
        TextView noLocationTv;

        public HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_content_item)
        SwipeMenuLayout swipeMenuLayout;
        @BindView(R.id.main_goods_img_view)
        GoodsImageView mImgView;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.location_tv)
        TextView locationNameTv;
        @BindView(R.id.item_goods_delete_tv)
        TextView deleteTv;
        @BindView(R.id.item_goods_location_add_tv)
        TextView addLocationTv;
        @BindView(R.id.item_goods_location_tv)
        TextView locationTv;
        @BindView(R.id.item_goods_top_tv)
        TextView topTv;
        @BindView(R.id.item_goods_unlock_tv)
        TextView unlockTv;
        @BindView(R.id.item_goods_rl)
        View itemGoodsView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(int positions, View view);

        public void onDeleteClick(int positions, View view);

        public void onLocationClick(int positions, View view);

        public void onAddLocationClick(int positions, View view);

        public void onTopClick(int positions, View view);

        public void onUnlickClick(int positions, View view);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
