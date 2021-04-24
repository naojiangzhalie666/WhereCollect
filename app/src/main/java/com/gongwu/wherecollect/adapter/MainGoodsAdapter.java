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
public class MainGoodsAdapter extends RecyclerView.Adapter<MainGoodsAdapter.CustomViewHolder> {
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

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_goods_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        holder.swipeMenuLayout.setIos(false);//设置是否开启IOS阻塞式交互
        holder.swipeMenuLayout.setLeftSwipe(true);//true往左滑动,false为往右侧滑动
        holder.swipeMenuLayout.setSwipeEnable(true);//设置侧滑功能开关
        ObjectBean bean = mlist.get(i);
        holder.mImgView.name.setText(null);
        holder.mImgView.head.setBackground(null);
        holder.mImgView.head.setImageDrawable(null);
        if (bean.getObject_url().contains("http")) {
            holder.mImgView.setImg(bean.getObject_url(), 3);
        } else {
            int resId = Color.parseColor(bean.getObject_url());
            holder.mImgView.setResourceColor(bean.getName(), resId, 3);
        }
        holder.nameTv.setText(bean.getName());
        holder.locationNameTv.setText(getLoction(bean));
        if (darklayer) {
            holder.lockTv.setVisibility(View.GONE);
            holder.unlockTv.setVisibility(View.VISIBLE);
        } else {
            holder.lockTv.setVisibility(View.VISIBLE);
            holder.unlockTv.setVisibility(View.GONE);
        }
        if ("未归位".equals(holder.locationNameTv.getText().toString())) {
            holder.addLocationTv.setVisibility(View.VISIBLE);
            holder.locationTv.setVisibility(View.GONE);
        } else {
            holder.addLocationTv.setVisibility(View.GONE);
            holder.locationTv.setVisibility(View.VISIBLE);
        }
        holder.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    if (holder.swipeMenuLayout != null) holder.swipeMenuLayout.smoothClose();
                    onItemClickListener.onDeleteClick(i, holder.itemView);
                }
            }
        });
        holder.addLocationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onAddLocationClick(i, holder.itemView);
                }
            }
        });
        holder.locationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    if (holder.swipeMenuLayout != null) holder.swipeMenuLayout.smoothClose();
                    onItemClickListener.onLocationClick(i, holder.itemView);
                }
            }
        });
        holder.lockTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    if (holder.swipeMenuLayout != null) holder.swipeMenuLayout.smoothClose();
                    onItemClickListener.onLockClick(i, holder.itemView);
                }
            }
        });
        holder.unlockTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    if (holder.swipeMenuLayout != null) holder.swipeMenuLayout.smoothClose();
                    onItemClickListener.onUnlickClick(i, holder.itemView);
                }
            }
        });
        holder.itemGoodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(i, view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
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
        @BindView(R.id.item_goods_lock_tv)
        TextView lockTv;
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

        public void onLockClick(int positions, View view);

        public void onUnlickClick(int positions, View view);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
