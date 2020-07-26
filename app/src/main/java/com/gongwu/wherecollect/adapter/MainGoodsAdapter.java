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
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsDetailsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;

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
    Context context;
    List<ObjectBean> mlist;

    public MainGoodsAdapter(Context context, List<ObjectBean> list) {
        this.context = context;
        this.mlist = list;
    }

    /**
     * 拼接位置
     *
     * @return
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
        ObjectBean bean = mlist.get(i);
        holder.image.refreshDrawableState();
        holder.image.setImageDrawable(null);
        holder.image.setBackgroundResource(0);
        if (!TextUtils.isEmpty(bean.getObject_url()) && bean.getObject_url().contains("http")) {
            ImageLoader.load(context, holder.image, 3, bean.getObject_url(), R.drawable.ic_img_error);
            holder.imgTv.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(bean.getObject_url()) && !bean.getObject_url().contains("/")) {
            holder.image.setBackgroundColor(Color.parseColor(bean.getObject_url()));
            int resId = Color.parseColor(bean.getObject_url());
            ImageLoader.load(context, holder.image, 3, resId);
            holder.imgTv.setVisibility(View.VISIBLE);
            holder.imgTv.setText(bean.getName());
        } else {
            int resId = context.getResources().getColor(R.color.goods_color_1);
            ImageLoader.load(context, holder.image, 3, resId);
            holder.imgTv.setVisibility(View.VISIBLE);
            holder.imgTv.setText(bean.getName());
        }

        holder.nameTv.setText(bean.getName());
        holder.locationTv.setText(getLoction(bean));
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.location_tv)
        TextView locationTv;
        @BindView(R.id.no_url_img_tv)
        TextView imgTv;

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
