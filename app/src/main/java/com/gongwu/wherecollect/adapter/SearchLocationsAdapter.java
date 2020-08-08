package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
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
public class SearchLocationsAdapter extends RecyclerView.Adapter<SearchLocationsAdapter.CustomViewHolder> {
    private Context context;
    private List<ObjectBean> mlist;

    public SearchLocationsAdapter(Context context, List<ObjectBean> list) {
        this.context = context;
        this.mlist = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_location_child_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        ObjectBean bean = mlist.get(position);
        holder.nameTv.setText(bean.getName());
        holder.splitView.setVisibility((position != mlist.size() - 1) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.split_view)
        View splitView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
        }

    }

}
