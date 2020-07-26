package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.view.PileAvertView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 共享空间列表adapter
 */

public class ShareSpaceListAdapter extends RecyclerView.Adapter<ShareSpaceListAdapter.ViewHolder> {

    private Context mContext;
    private List<SharedLocationBean> datas;

    public ShareSpaceListAdapter(Context mContext, List<SharedLocationBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_share_location_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedLocationBean bean = datas.get(position);
        holder.spaceNameTv.setText(bean.getName());
        holder.mPileAvertView.setAvertImages(bean.getShared_users());
        holder.clseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.closeClick(position, view);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.share_space_list_name_tv)
        TextView spaceNameTv;
        @BindView(R.id.pile_avert_view)
        PileAvertView mPileAvertView;
        @BindView(R.id.close_share_space_view)
        ImageView clseIV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemsClick(getLayoutPosition(), view);
            }
        }
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {


        void closeClick(int position, View v);

        void onItemsClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
