package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:主页物品查看列表
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class DetailedViewAdapter extends RecyclerView.Adapter<DetailedViewAdapter.CustomViewHolder> {
    private Context mContext;
    private List<DetailedListGoodsBean.Goods> mlist;

    public DetailedViewAdapter(Context context, List<DetailedListGoodsBean.Goods> list) {
        this.mContext = context;
        this.mlist = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailed_view_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        DetailedListGoodsBean.Goods bean = mlist.get(position);
        holder.mImgView.name.setText(null);
        holder.mImgView.head.setBackground(null);
        holder.mImgView.head.setImageDrawable(null);
        if (bean.getImg().contains("http")) {
            holder.mImgView.setImg(bean.getImg(), 3);
        } else {
            int resId = Color.parseColor(bean.getImg());
            holder.mImgView.setResourceColor(bean.getName(), resId, 3);
        }
        holder.detailedNameTv.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detailed_img_iv)
        GoodsImageView mImgView;
        @BindView(R.id.detailed_name_tv)
        TextView detailedNameTv;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
        }

    }

}
