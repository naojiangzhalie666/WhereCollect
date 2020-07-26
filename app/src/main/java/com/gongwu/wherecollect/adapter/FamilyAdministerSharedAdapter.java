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
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.util.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 共享人列表 adapter
 */

public class FamilyAdministerSharedAdapter extends RecyclerView.Adapter<FamilyAdministerSharedAdapter.ViewHolder> {

    private Context mContext;
    private List<SharedPersonBean> mlist;

    public FamilyAdministerSharedAdapter(Context mContext, List<SharedPersonBean> datas) {
        this.mContext = mContext;
        this.mlist = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_family_administer_shared_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SharedPersonBean bean = mlist.get(position);
        ImageLoader.loadCircle(mContext, holder.userIconIv, bean.getAvatar(), R.drawable.icon_app);
        holder.userNameTv.setText(bean.getNickname());
        String s = "";
        if (bean.getSharedRoom() != null && bean.getSharedRoom().size() > 0) {
            for (int i = 0; i < bean.getSharedRoom().size(); i++) {
                SharedLocationBean locationBean = bean.getSharedRoom().get(i);
                if (locationBean != null && !TextUtils.isEmpty(locationBean.getName())) {
                    s += locationBean.getName() + " ";
                }
            }
        }
        holder.userSpaceTv.setText(s);
        holder.isValidView.setVisibility(bean.isSharing() ? View.GONE : View.VISIBLE);
        holder.closeIv.setVisibility(bean.isSharing() ? View.VISIBLE : View.GONE);
        holder.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.closeClick(position, view);
                }
            }
        });
        holder.splitView.setVisibility((position != mlist.size() - 1) ? View.VISIBLE : View.GONE);
    }


    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
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
        TextView isValidView;
        @BindView(R.id.split_view)
        View splitView;

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
