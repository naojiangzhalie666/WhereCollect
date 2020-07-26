package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mChenys on 2017/2/15.
 */
public class AddFamilyToSelectRoomsAdapter extends RecyclerView.Adapter<AddFamilyToSelectRoomsAdapter.MyViewHolder> {

    private List<RoomFurnitureBean> mData;
    private Context context;

    public AddFamilyToSelectRoomsAdapter(Context context, List<RoomFurnitureBean> mData) {
        this.mData = mData;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_add_family_toselect_rooms, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddFamilyToSelectRoomsAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getName());
        holder.name.setTextColor(context.getResources().getColor((mData.size() - 1 != position) ? R.color.color333 : R.color.color999));
        holder.splitView.setVisibility((mData.size() - 1 != position) ? View.VISIBLE : View.GONE);
        holder.mCheckBox.setVisibility((mData.size() - 1 != position) ? View.VISIBLE : View.GONE);
        holder.imageview.setVisibility((mData.size() - 1 != position) ? View.GONE : View.VISIBLE);
        holder.mCheckBox.setChecked(mData.get(position).isSelect());
    }

    @Override
    public int getItemCount() {
        //注意:这里最少有一个,因为有多了一个添加按钮
        return StringUtils.getListSize(mData);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.split_view)
        View splitView;
        @BindView(R.id.imageview)
        ImageView imageview;
        @BindView(R.id.edit_check_box)
        CheckBox mCheckBox;

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

    public void setEditClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}