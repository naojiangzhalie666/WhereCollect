package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
 */
public class BigImageAdapter extends BannerAdapter<ObjectBean, BigImageAdapter.BannerViewHolder> {
    private Context mContext;

    public BigImageAdapter(Context mContext, List<ObjectBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.mContext = mContext;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_big_img_view, parent, false);
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindView(BannerViewHolder holder, ObjectBean data, int position, int size) {
        if (data.getObject_url().contains("#")) {
            int resId = Color.parseColor(data.getObject_url());
            holder.mImgView.setResourceColor(data.getName(), resId, 0);
        } else {
            holder.mImgView.setImg(data.getObject_url(), 0);
        }
        holder.mImgNameTv.setText(data.getName());
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_big_img_view)
        GoodsImageView mImgView;
        @BindView(R.id.item_big_img_tv)
        TextView mImgNameTv;

        public BannerViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
