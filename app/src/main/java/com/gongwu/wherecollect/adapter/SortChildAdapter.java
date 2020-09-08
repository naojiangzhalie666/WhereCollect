package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
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
public class SortChildAdapter extends RecyclerView.Adapter<SortChildAdapter.CustomViewHolder> {
    MyOnItemClickListener onItemClickListener;
    Context context;
    List<ChannelBean> mlist;
    private boolean isChild;

    public SortChildAdapter(Context context, List<ChannelBean> list) {
        this.context = context;
        this.mlist = list;
    }

    public void setAdapterType(boolean isChild) {
        this.isChild = isChild;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_sort_child_view, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.relationLayout.setVisibility(View.GONE);
        holder.relationLayout.removeAllViews();
        if (isChild) {
            holder.sortTv.setText(mlist.get(position).getName());
            if (mlist.get(position).getParents() != null && mlist.get(position).getParents().size() > 0) {
                holder.relationLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < mlist.get(position).getParents().size(); i++) {
                    if (i == AppConstant.DEFAULT_INDEX_OF) continue;
                    TextView text = (TextView) View.inflate(context, R.layout.sort_child_textview, null);
                    text.setText(mlist.get(position).getParents().get(i).getName());
                    holder.relationLayout.addView(text);
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) text.getLayoutParams();
                    lp.setMarginEnd(10);
                    text.setLayoutParams(lp);
                }
            }
        } else {
            if (mlist.get(position).getParents() != null && mlist.get(position).getParents().size() > 0) {
                holder.sortTv.setText(mlist.get(position).getParents().get(AppConstant.DEFAULT_INDEX_OF).getName());
                holder.relationLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < mlist.get(position).getParents().size(); i++) {
                    TextView text = (TextView) View.inflate(context, R.layout.sort_child_textview, null);
                    if (i == mlist.get(position).getParents().size() - 1) {
                        text.setText(mlist.get(position).getName());
                    } else {
                        text.setText(mlist.get(position).getParents().get(i + 1).getName());
                    }
                    holder.relationLayout.addView(text);
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) text.getLayoutParams();
                    lp.setMarginEnd(10);
                    text.setLayoutParams(lp);
                }
            } else {
                holder.sortTv.setText(mlist.get(position).getName());
            }
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

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.child_sort_name_tv)
        TextView sortTv;
        @BindView(R.id.child_relation_name_layout)
        LinearLayout relationLayout;

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
