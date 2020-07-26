package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:主页物品查看列表
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class FamilyRoomAdapter extends RecyclerView.Adapter<FamilyRoomAdapter.CustomViewHolder> {
    private Context mContext;
    private List<FurnitureBean> mlist;
    private Map<Integer, FurnitureBean> selectMap = new TreeMap<>();

    private boolean isEdit;

    public FamilyRoomAdapter(Context context, List<FurnitureBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public List<FurnitureBean> getSelectLists() {
        List<FurnitureBean> mlist = new ArrayList<>();
        Collection<FurnitureBean> values = selectMap.values();    //获取Map集合的value集合
        for (FurnitureBean object : values) {
            mlist.add(object);
        }
        return mlist;
    }

    public void clearSelect() {
        selectMap.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_family_room_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FurnitureBean bean = mlist.get(position);
        ImageLoader.load(mContext, holder.mImgView, bean.getImage_url());
        holder.roomNameTv.setText(bean.getName());
        holder.objectCountTv.setText(StringUtils.toString(mContext, R.string.room_object_count, bean.getObject_count()));
        holder.rightSplitView.setVisibility((position + 1) % 3 == 0 ? View.GONE : View.VISIBLE);
        holder.emptyLayersView.setVisibility((!isEdit && (bean.getLayers() == null || bean.getLayers().size() == 0)) ? View.VISIBLE : View.GONE);
        holder.mCheckBox.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        holder.mCheckBox.setChecked(selectMap.containsKey(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMap.containsKey(position)) {
                    holder.mCheckBox.setChecked(false);
                    selectMap.remove(position);
                } else {
                    holder.mCheckBox.setChecked(true);
                    selectMap.put(position, bean);
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, holder.itemView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.room_img_iv)
        ImageView mImgView;
        @BindView(R.id.room_name_tv)
        TextView roomNameTv;
        @BindView(R.id.object_count_tv)
        TextView objectCountTv;
        @BindView(R.id.right_split_view)
        View rightSplitView;
        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.empty_layers_view)
        ImageView emptyLayersView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
        }


    }

    public MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
