package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mChenys on 2017/2/15.
 */
public class FamilyPopupListAdapter extends RecyclerView.Adapter<FamilyPopupListAdapter.MyViewHolder> {

    private List<FamilyBean> mlist;
    private Context mContext;

    public FamilyPopupListAdapter(Context context, List<FamilyBean> mlist) {
        this.mlist = mlist;
        this.mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_family_popup_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyPopupListAdapter.MyViewHolder holder, int position) {
        FamilyBean familyBean = mlist.get(position);
        if (familyBean.isSystemEdit()) {
            holder.itemIMGiv.setImageDrawable(mContext.getDrawable(R.drawable.icon_edit_family));
            holder.nameTv.setText(R.string.edit_family);
        } else {
            holder.itemIMGiv.setImageDrawable(mContext.getDrawable(familyBean.isBeShared() ? R.drawable.ic_list_shared : R.drawable.icon_family));
            holder.nameTv.setText(familyBean.getName());
        }

        holder.splitView.setVisibility((position == mlist.size() - 1) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        //注意:这里最少有一个,因为有多了一个添加按钮
        return StringUtils.getListSize(mlist);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_family_iv)
        ImageView itemIMGiv;
        @BindView(R.id.item_family_name)
        TextView nameTv;
        @BindView(R.id.split_view)
        View splitView;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemsClick(getLayoutPosition(), v);
            }
        }
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemsClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}