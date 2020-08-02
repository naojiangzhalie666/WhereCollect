package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.util.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/12.
 */

public class AddSharePersonOldListAdapter extends RecyclerView.Adapter<AddSharePersonOldListAdapter.ViewHolder> {
    private Context mContext;
    private List<SharedPersonBean> datas;
    private MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AddSharePersonOldListAdapter(Context mContext, List<SharedPersonBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_share_person_oldlist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SharedPersonBean bean = datas.get(position);
        ImageLoader.loadCircle(mContext, holder.userIconIv, bean.getAvatar(), R.drawable.ic_user_error);
        holder.userNameTv.setText(bean.getNickname());
        holder.userUsidTv.setText("ID: " + bean.getUsid());
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.add_share_person_oldlist_user_icon_iv)
        ImageView userIconIv;
        @BindView(R.id.add_share_person_oldlist_user_name_tv)
        TextView userNameTv;
        @BindView(R.id.add_share_person_oldlist_user_usid_tv)
        TextView userUsidTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }
}
