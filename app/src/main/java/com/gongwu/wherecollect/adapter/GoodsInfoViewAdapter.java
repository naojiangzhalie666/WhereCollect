package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.GoodsInfoBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.PopupMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsInfoViewAdapter extends RecyclerView.Adapter<GoodsInfoViewAdapter.CustomViewHolder> {

    private Context mContext;
    private List<GoodsInfoBean> mDatas;

    public GoodsInfoViewAdapter(Context mContext, List<GoodsInfoBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_info_view_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        GoodsInfoBean bean = mDatas.get(position);
        holder.goodsTypeView.setText(StringUtils.getGoodsTypeString(bean.getType()));
        holder.goodsValueView.setVisibility(View.GONE);
        holder.infoStar.setVisibility(View.GONE);
        holder.timeLayout.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(null);
        switch (bean.getType()) {
            case StringUtils.TYPE_GOODS_COUNT://数量
            case StringUtils.TYPE_GOODS_PRICE://价格
                holder.goodsValueView.setText(bean.getValue());
                holder.goodsValueView.setVisibility(View.VISIBLE);
                break;
            case StringUtils.TYPE_GOODS_SEASON://季节
            case StringUtils.TYPE_GOODS_COLOR://颜色
            case StringUtils.TYPE_GOODS_CHANNEL://渠道
            case StringUtils.TYPE_GOODS_CLASSIFY://分类
            case StringUtils.TYPE_GOODS_NOTE://备注
            case StringUtils.TYPE_GOODS_BELONGER://归属人
                String value = bean.getValue();
                if (StringUtils.TYPE_GOODS_NOTE == bean.getType()) {
                    if (bean.getValue().contains("\n")) {
                        value = bean.getValue().split("\n")[0];
                    }
                }
                holder.goodsValueView.setText(value);
                holder.goodsValueView.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMessage popup = new PopupMessage(mContext);
                        popup.setPopupGravity(Gravity.CENTER);
                        popup.showPopupWindow();
                        popup.initData(bean.getValue());
                    }
                });
                break;
            case StringUtils.TYPE_GOODS_STAR://评级
                holder.infoStar.setVisibility(View.VISIBLE);
                holder.infoStar.setRating(Integer.parseInt(bean.getValue()));
                break;
            case StringUtils.TYPE_GOODS_PURCHASE_TIME://购买时间
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.yearTimeTv.setBackground(mContext.getDrawable(R.drawable.shape_goods_info_start_tiem_top_bg));
                String[] time = bean.getValue().split("-");
                holder.yearTimeTv.setText(time[0]);
                holder.timeTv.setText(new StringBuilder(time[1]).append("/").append(time[2]).toString());
                break;
            case StringUtils.TYPE_GOODS_EXPIRY_TIME://到期时间
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.yearTimeTv.setBackground(mContext.getDrawable(R.drawable.shape_goods_info_end_tiem_top_bg));
                String[] time1 = bean.getValue().split("-");
                holder.yearTimeTv.setText(time1[0]);
                holder.timeTv.setText(new StringBuilder(time1[1]).append("/").append(time1[2]).toString());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_info_tv)
        TextView goodsValueView;
        @BindView(R.id.goods_type_tv)
        TextView goodsTypeView;
        @BindView(R.id.goods_info_rb)
        RatingBar infoStar;
        @BindView(R.id.info_start_time_layout)
        LinearLayout timeLayout;
        @BindView(R.id.info_year_time_tv)
        TextView yearTimeTv;
        @BindView(R.id.info_time_tv)
        TextView timeTv;

        public CustomViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
        }
    }
}
