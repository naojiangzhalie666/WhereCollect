package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ImageLoader;
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
public class EditMoreGoodsAdapter extends RecyclerView.Adapter<EditMoreGoodsAdapter.CustomViewHolder> {
    Context context;
    List<ObjectBean> mlist;

    public EditMoreGoodsAdapter(Context context, List<ObjectBean> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_edit_more_goods_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        ObjectBean bean = mlist.get(i);
        holder.mImgView.name.setText(null);
        holder.mImgView.head.setBackground(null);
        holder.mImgView.head.setImageDrawable(null);
        holder.notCategoriesType.setVisibility(View.GONE);
        holder.notLocationType.setVisibility(View.INVISIBLE);
        if (bean.getObject_url().contains("http")) {
            holder.mImgView.setImg(bean.getObject_url(), 3);
        } else {
            int resId = Color.parseColor(bean.getObject_url());
            holder.mImgView.setResourceColor(bean.getName(), resId, 3);
        }
        holder.nameTv.setText(bean.getName());
        if (StringUtils.isEmpty(bean.getLocations())) {
            holder.notLocationType.setVisibility(View.VISIBLE);
        } else {
            holder.locationTv.setText(getLoction(bean));
        }
        if (StringUtils.isEmpty(bean.getCategories())) {
            holder.notCategoriesType.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setChecked(bean.isSelect());
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.main_goods_img_view)
        GoodsImageView mImgView;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.location_tv)
        TextView locationTv;
        @BindView(R.id.not_categories_type)
        TextView notCategoriesType;
        @BindView(R.id.not_location_type)
        TextView notLocationType;
        @BindView(R.id.check_box)
        CheckBox mCheckBox;

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

    public String getSelectGoodsIds() {
        StringBuilder sb = new StringBuilder();
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                sb.append(bean.get_id()).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

}
