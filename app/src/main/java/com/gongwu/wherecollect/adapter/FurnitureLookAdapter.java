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
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

import java.util.ArrayList;
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
public class FurnitureLookAdapter extends RecyclerView.Adapter<FurnitureLookAdapter.CustomViewHolder> {
    public MyOnItemClickListener onItemClickListener;
    Context context;
    List<ObjectBean> mlist;

    public FurnitureLookAdapter(Context context, List<ObjectBean> list) {
        this.context = context;
        this.mlist = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_furniture_goods_recyclerview, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        //111
        ObjectBean tempBean = mlist.get(position);
        holder.imgLayout.setBackground(null);
        holder.mImgView.name.setText(null);
        holder.mImgView.head.setBackground(null);
        holder.mImgView.head.setImageDrawable(null);
        holder.itemView.setAlpha(1.0f);
        if (tempBean.getLevel() == AppConstant.LEVEL_BOX) {
            holder.mImgView.head.setImageDrawable(context.getDrawable(R.drawable.icon_template_box));
            if (MainActivity.moveBoxBean != null && MainActivity.moveBoxBean.getCode().equals(tempBean.getCode())) {
                holder.itemView.setAlpha(0.5f);
            }
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

    public ObjectBean getSelectGoods() {
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                return bean;
            }
        }
        return null;
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

    public List<String> getSelectGoodsIdsToList() {
        List<String> ids = new ArrayList<>();
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                ids.add(bean.get_id());
            }
        }
        return ids;
    }

    public void delSelectGoods(List<ObjectBean> mData) {
        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i).isSelect()) {
                String id = mlist.get(i).get_id();
                for (int j = 0; j < mData.size(); j++) {
                    if (id.equals(mData.get(j).get_id())) {
                        mData.remove(j);
                        break;
                    }
                }
                mlist.remove(i);
                i--;
            }
        }
        notifyDataSetChanged();
    }
}
