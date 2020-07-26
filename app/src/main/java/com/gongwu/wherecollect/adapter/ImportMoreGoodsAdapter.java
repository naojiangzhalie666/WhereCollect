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
import com.gongwu.wherecollect.contract.AppConstant;
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
public class ImportMoreGoodsAdapter extends RecyclerView.Adapter<ImportMoreGoodsAdapter.CustomViewHolder> {
    private Context mContext;
    private List<ObjectBean> mlist;

    public ImportMoreGoodsAdapter(Context context, List<ObjectBean> list) {
        this.mContext = context;
        this.mlist = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_import_more_goods_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
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
        holder.name.setText(bean.getName());
        holder.mCheckBox.setChecked(bean.isSelect());
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.import_img_view)
        GoodsImageView mImgView;
        @BindView(R.id.import_name_tv)
        TextView name;
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
}
