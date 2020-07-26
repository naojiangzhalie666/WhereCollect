package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.furniture.CustomTableRowLayout;

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
public class LayerTemplateAdapter extends RecyclerView.Adapter<LayerTemplateAdapter.CustomViewHolder> {


    private Context mContext;
    private List<FurnitureBean> mlist;

    public LayerTemplateAdapter(Context context, List<FurnitureBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer_template_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        FurnitureBean mFurnitureBean = mlist.get(position);
        holder.rowLayout.setEnabled(false);
        holder.rowLayout.setNotChildViewClick(true);
        holder.rowLayout.init(mFurnitureBean.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng);
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
         void onItemClick(FurnitureBean bean);
    }

    public void setOnItemClickListener(OnItemChildClickListener listener) {
        this.listener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.layer_template_item_view)
        CustomTableRowLayout rowLayout;
        @BindView(R.id.linearLayout)
        View linearLayout;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(mlist.get(getLayoutPosition()));
            }
        }
    }

}
