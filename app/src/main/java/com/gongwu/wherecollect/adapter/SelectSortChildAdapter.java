package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.util.StringUtils;

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
public class SelectSortChildAdapter extends RecyclerView.Adapter<SelectSortChildAdapter.CustomViewHolder> {
    OnItemClickListener onItemClickListener;
    Context mContext;
    List<BaseBean> mlist;
    BaseBean selectBaseBean;

    public SelectSortChildAdapter(Context context, List<BaseBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    public void setSelectBaseBean(BaseBean baseBean) {
        this.selectBaseBean = baseBean;
        notifyDataSetChanged();
    }

    public void setSelectBaseBeanNotRefresh(BaseBean baseBean) {
        this.selectBaseBean = baseBean;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_select_sort_child_view, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        BaseBean baseBean = mlist.get(position);
        holder.sortNameTv.setText(baseBean.getName());
        holder.sortNameTv.setTextColor(ContextCompat.getColor(mContext, R.color.color555));
        holder.sortTypeView.setOnClickListener(null);
        if (selectBaseBean != null) {
            if (!TextUtils.isEmpty(baseBean.get_id())) {
                if (baseBean.get_id().equals(selectBaseBean.get_id())) {
                    holder.sortNameTv.setTextColor(ContextCompat.getColor(mContext, R.color.maincolor));
                }
            }
        }
        if (TextUtils.isEmpty(baseBean.get_id()) && baseBean.getName().equals("新增")) {
            holder.sortTypeView.setVisibility(View.VISIBLE);
            holder.sortTypeView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_add_sort_child));
            holder.sortNameTv.setTextColor(ContextCompat.getColor(mContext, R.color.maincolor));
        } else {
            holder.sortTypeView.setVisibility(View.INVISIBLE);
        }
        if (baseBean.isUser()) {
            holder.sortTypeView.setVisibility(View.VISIBLE);
            holder.sortTypeView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_delete_sort_child));
            holder.sortTypeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemDeleteClick(position, view);
                    }
                }
            });
        }
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

    @Override
    public void onViewAttachedToWindow(CustomViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.sort_child_type_iv)
        ImageView sortTypeView;
        @BindView(R.id.sort_child_name_tv)
        TextView sortNameTv;

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

    public interface OnItemClickListener {
        void onItemClick(int positions, View view);

        void onItemDeleteClick(int positions, View view);
    }
}
