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
import com.gongwu.wherecollect.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mChenys on 2017/2/15.
 */
public class EditRoomListAdapter extends RecyclerView.Adapter<EditRoomListAdapter.MyViewHolder> {
    private List<RoomBean> mData;
    private Context context;
    private boolean isEdit;
    private boolean isMoveEdit;

    public EditRoomListAdapter(Context context, List<RoomBean> mData) {
        this.mData = mData;
        this.context = context;
    }

    public void setEditType(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public void setMoveEditType(boolean isEdit) {
        this.isMoveEdit = isEdit;
        notifyDataSetChanged();
    }

    public boolean getMoveEditType() {
        return isMoveEdit;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_edit_room_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EditRoomListAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getName());
        holder.splitView.setVisibility((mData.size() - 1 != position) ? View.VISIBLE : View.GONE);
        holder.mCheckBox.setVisibility(isMoveEdit ? View.VISIBLE : View.GONE);
        holder.moveView.setVisibility((isMoveEdit || isEdit) ? View.GONE : View.VISIBLE);
        holder.delView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        holder.editView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        holder.delView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDelClick(position);
                }
            }
        });
        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditNameClick(position);
                }
            }
        });
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
        @BindView(R.id.edit_room_move)
        ImageView moveView;
        @BindView(R.id.edit_room_del)
        ImageView delView;
        @BindView(R.id.edit_room_iv)
        ImageView editView;
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

        void onEditNameClick(int position);

        void onDelClick(int position);

        void onItemsClick(int position, View v);
    }

    public void setEditClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}