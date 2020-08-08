package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

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
public class SearchGoodsAdapter extends RecyclerView.Adapter<SearchGoodsAdapter.CustomViewHolder> {
    Context context;
    List<ObjectBean> mlist;

    public SearchGoodsAdapter(Context context, List<ObjectBean> list) {
        this.context = context;
        this.mlist = list;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_goods_child_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        ObjectBean bean = mlist.get(position);
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
        holder.locationTv.setText(getLoction(bean));
        holder.splitView.setVisibility((position != mlist.size() - 1) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.search_goods_img_view)
        GoodsImageView mImgView;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.location_tv)
        TextView locationTv;
        @BindView(R.id.split_view)
        View splitView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    public MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
