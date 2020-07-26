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
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.object.AddMoreGoodsActivity;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

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
public class AddMoreGoodsAdapter extends RecyclerView.Adapter<AddMoreGoodsAdapter.CustomViewHolder> {
    private Context mContext;
    private List<ObjectBean> mlist;

    public AddMoreGoodsAdapter(Context context, List<ObjectBean> list) {
        this.mContext = context;
        this.mlist = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_more_goods_view, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        ObjectBean bean = mlist.get(i);
        holder.goodsTv.setText("");
        holder.mImgView.head.setImageDrawable(null);
        if (AddMoreGoodsActivity.ADD_GOODS_CODE == bean.get__v()) {
            holder.mImgView.head.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_add_goods));
            holder.mImgView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_white_r10dp));
        } else {
            if (bean.getObject_url().contains("#")) {
                int resId = Color.parseColor(bean.getObject_url());
                holder.mImgView.setResourceColor(bean.getName(), resId, 10);
            } else {
                holder.mImgView.setImg(bean.getObject_url(), 10);
            }
            holder.goodsTv.setText(bean.getName());


        }

    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.add_img_view)
        GoodsImageView mImgView;
        @BindView(R.id.add_more_goods_name_tv)
        TextView goodsTv;

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
