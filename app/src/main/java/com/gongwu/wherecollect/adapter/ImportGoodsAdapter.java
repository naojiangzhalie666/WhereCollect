package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class ImportGoodsAdapter extends RecyclerView.Adapter<ImportGoodsAdapter.CustomViewHolder> {
    public MyOnItemClickListener onItemClickListener;
    Context context;
    List<ObjectBean> mlist;

    public ImportGoodsAdapter(Context context, List<ObjectBean> list) {
        this.context = context;
        this.mlist = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_import_goods, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ObjectBean tempBean = mlist.get(position);
        holder.imgLayout.setBackground(null);
        holder.mImgView.name.setText(null);
        holder.mImgView.head.setBackground(null);
        holder.mImgView.head.setImageDrawable(null);
        if (tempBean.getLevel() == AppConstant.LEVEL_BOX) {
            holder.mImgView.head.setImageDrawable(context.getDrawable(R.drawable.icon_template_box));
        } else {
            holder.imgLayout.setBackground(context.getDrawable(R.drawable.select_furniture_look_item_bg));
            if (tempBean.getObject_url().contains("http")) {
                holder.mImgView.setImg(tempBean.getObject_url(), 3);
            } else {
                int resId = Color.parseColor(tempBean.getObject_url());
                holder.mImgView.setResourceColor(tempBean.getName(), resId, 3);
            }
        }
        holder.name.setText(tempBean.getName());
        holder.imgLayout.setSelected(tempBean.isSelect());
        holder.name.setSelected(tempBean.isSelect());
    }

    @Override
    public int getItemCount() {
        return StringUtils.getListSize(mlist);
    }


    /**
     * 可以返回不同类型加载不同布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_view)
        GoodsImageView mImgView;
        @BindView(R.id.object_name_tv)
        TextView name;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.img_layout)
        RelativeLayout imgLayout;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

}
