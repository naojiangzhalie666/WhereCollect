package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.DetailedGoodsBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

import java.util.ArrayList;
import java.util.Collection;
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
public class DetailedViewAdapter extends RecyclerView.Adapter<DetailedViewAdapter.CustomViewHolder> {
    private Context mContext;
    private List<DetailedGoodsBean> mlist;
    private boolean isShowTableView;

    public DetailedViewAdapter(Context context, List<DetailedGoodsBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    public void showGCTableView(boolean isShowTableView) {
        this.isShowTableView = isShowTableView;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailed_view_layout, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        DetailedGoodsBean bean = mlist.get(position);
        holder.mImgView.name.setText(null);
        holder.mImgView.head.setBackground(null);
        holder.mImgView.head.setImageDrawable(null);
        if (bean.getImg().contains("http")) {
            holder.mImgView.setImg(bean.getImg(), 0);
        } else {
            int resId = Color.parseColor(bean.getImg());
            holder.mImgView.setResourceColor(bean.getName(), resId, 0);
        }
        holder.detailedNameTv.setText(bean.getName());
        holder.typeNameView.setVisibility(View.GONE);
        holder.boxType.setVisibility(View.GONE);
        if (!isShowTableView) {
            holder.typeNameView.setVisibility(bean.isShowGCType() ? View.VISIBLE : View.GONE);
            holder.typeNameView.setText("隔层内");
        }
        if (bean.isBoxType()) {
            holder.typeNameView.setVisibility(bean.isBoxType() ? View.VISIBLE : View.GONE);
            holder.boxType.setVisibility(bean.isBoxType() ? View.VISIBLE : View.GONE);
            holder.typeNameView.setText(bean.getBoxName());
        }
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detailed_img_iv)
        GoodsImageView mImgView;
        @BindView(R.id.detailed_name_tv)
        TextView detailedNameTv;
        @BindView(R.id.detailed_view_type_box)
        ImageView boxType;
        @BindView(R.id.detailed_view_type_name)
        TextView typeNameView;//隔层type

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setTag(this);
        }

    }

    //设置边距
    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int normal;
        private final int margin;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.top = normal;
            outRect.bottom = normal;
            if (parent.getChildAdapterPosition(view) % 3 == 0) {
                outRect.right = normal;
                outRect.left = margin;
            } else if (parent.getChildAdapterPosition(view) % 3 == 1) {
                outRect.right = margin;
                outRect.left = margin;
            } else if (parent.getChildAdapterPosition(view) % 3 == 2) {
                outRect.right = normal;
                outRect.left = margin;
            }

        }

        public SpaceItemDecoration(int normal, int margin) {
            this.normal = normal;
            this.margin = margin;
        }
    }

}
