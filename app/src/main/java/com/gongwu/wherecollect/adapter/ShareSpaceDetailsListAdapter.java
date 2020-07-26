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
 * 共享控件详情列表 adapter
 */
public class ShareSpaceDetailsListAdapter extends RecyclerView.Adapter<ShareSpaceDetailsListAdapter.ViewHolder> {

    private Context mContext;
    private List<SharedPersonBean> datas;
    private MyOnItemClickListener onItemClickListener;
    private String content;
    private SharedPersonBean manager;
    private String loginUserId;

    public ShareSpaceDetailsListAdapter(Context mContext, List<SharedPersonBean> datas, String loginUserId) {
        this.mContext = mContext;
        this.datas = datas;
        this.loginUserId = loginUserId;
    }

    public void setManager(SharedPersonBean manager) {
        this.manager = manager;
        notifyDataSetChanged();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_share_space_details_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedPersonBean bean = datas.get(position);
        ImageLoader.loadCircle(mContext, holder.userIconIv, bean.getAvatar(), R.drawable.icon_app);
        holder.userNameTv.setText(bean.getNickname());
        holder.userSpaceTv.setText(content);
        holder.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeClick(position);
            }
        });
        if (bean.isValid()) {
            holder.closeIv.setVisibility(View.VISIBLE);
        } else {
            holder.closeIv.setVisibility(View.GONE);
        }
        if (manager != null && manager.getId().equals(bean.getId())) {
            holder.lockIv.setVisibility(View.VISIBLE);
            if (!bean.getId().equals(loginUserId)) {
                holder.closeIv.setVisibility(View.GONE);
            }
        } else {
            holder.lockIv.setVisibility(View.GONE);
        }
    }

    public void closeClick(int position) {

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.share_person_details_user_icon_iv)
        ImageView userIconIv;
        @BindView(R.id.share_person_details_user_name_tv)
        TextView userNameTv;
        @BindView(R.id.share_person_details_user_space_tv)
        TextView userSpaceTv;
        @BindView(R.id.close_share_space_iv)
        ImageView closeIv;
        @BindView(R.id.space_details_lock_iv)
        ImageView lockIv;

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
