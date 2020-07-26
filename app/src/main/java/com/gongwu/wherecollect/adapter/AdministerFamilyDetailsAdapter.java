package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
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
public class AdministerFamilyDetailsAdapter extends RecyclerView.Adapter<AdministerFamilyDetailsAdapter.CustomViewHolder> {


    private Context mContext;
    private List<RoomBean> mlist;

    public AdministerFamilyDetailsAdapter(Context context, List<RoomBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_administer_family_details_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        RoomBean bean = mlist.get(position);
        holder.nameFamilyTv.setText(bean.getName());
        holder.numberTv.setText(String.format(mContext.getString(R.string.furniture_number), String.valueOf(bean.getFurnitureNum())));
        holder.sharedView.setVisibility(bean.isBeShared() ? View.VISIBLE : View.INVISIBLE);
        holder.splitView.setVisibility((position != mlist.size() - 1) ? View.VISIBLE : View.GONE);
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

    public OnItemChildClickListener listener;

    public interface OnItemChildClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemChildClickListener listener) {
        this.listener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_family_name)
        TextView nameFamilyTv;
        @BindView(R.id.furniture_number_tv)
        TextView numberTv;
        @BindView(R.id.split_view)
        View splitView;
        @BindView(R.id.room_is_shared)
        TextView sharedView;


        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

}
