package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.util.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 共享人列表 adapter
 */

public class SharePersonListAdapter extends RecyclerView.Adapter<SharePersonListAdapter.ViewHolder> {

    private Context mContext;
    private List<SharedPersonBean> datas;

    public SharePersonListAdapter(Context mContext, List<SharedPersonBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_share_person_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedPersonBean bean = datas.get(position);
        ImageLoader.loadCircle(mContext, holder.userIconIv, bean.getAvatar(), R.drawable.ic_user_error);
        holder.userNameTv.setText(bean.getNickname());
        String s = "";
        if (bean.getShared_locations() != null && bean.getShared_locations().size() > 0) {
            for (int i = 0; i < bean.getShared_locations().size(); i++) {
                SharedLocationBean locationBean = bean.getShared_locations().get(i);
                if (locationBean != null && !TextUtils.isEmpty(locationBean.getName())) {
                    s += locationBean.getName() + " ";
                }
            }
        }
        holder.userSpaceTv.setText(s);
        if (!bean.isValid()) {
            holder.isValidView.setVisibility(View.VISIBLE);
        } else {
            holder.isValidView.setVisibility(View.GONE);
        }
        holder.closeIv.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.share_person_list_user_icon_iv)
        ImageView userIconIv;
        @BindView(R.id.share_person_list_user_name_tv)
        TextView userNameTv;
        @BindView(R.id.share_person_list_user_space_tv)
        TextView userSpaceTv;
        @BindView(R.id.close_share_person_iv)
        ImageView closeIv;
        @BindView(R.id.share_person_valid_layout)
        View isValidView;

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
