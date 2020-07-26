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
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelationGoodsAdapter extends RecyclerView.Adapter<RelationGoodsAdapter.ViewHolder> {

    private Context mContext;
    private List<ObjectBean> mData;

    public RelationGoodsAdapter(Context mContext, List<ObjectBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_relation_goods_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ObjectBean bean = mData.get(i);
        if (!TextUtils.isEmpty(bean.getObject_url()) && bean.getObject_url().contains("http")) {
            holder.image.setImageDrawable(null);
            holder.image.setBackgroundResource(0);
            ImageLoader.load(mContext, holder.image, bean.getObject_url());
            holder.imgTv.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(bean.getObject_url()) && !bean.getObject_url().contains("/")) {
            holder.image.setImageDrawable(null);
            holder.image.setBackgroundResource(0);
            holder.image.setBackgroundColor(Color.parseColor(bean.getObject_url()));
            holder.imgTv.setVisibility(View.VISIBLE);
            holder.imgTv.setText(bean.getName());
        } else {
            holder.image.setImageDrawable(null);
            holder.image.setBackgroundResource(0);
            holder.image.setBackgroundColor(mContext.getResources().getColor(R.color.goods_color_1));
            holder.imgTv.setVisibility(View.VISIBLE);
            holder.imgTv.setText(bean.getName());
        }
        holder.mGoodsNameTv.setText(bean.getName());
        holder.mGoodsLocationTv.setText(StringUtils.getGoodsLoction(bean));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_relation_goods_iv)
        ImageView image;
        @BindView(R.id.no_url_img_tv)
        TextView imgTv;
        @BindView(R.id.item_relation_goods_name_tv)
        TextView mGoodsNameTv;
        @BindView(R.id.item_relation_goods_location_tv)
        TextView mGoodsLocationTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    public MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
