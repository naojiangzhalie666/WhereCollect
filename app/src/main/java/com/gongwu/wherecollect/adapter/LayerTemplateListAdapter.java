package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateListBean;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.ArrayList;
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
public class LayerTemplateListAdapter extends RecyclerView.Adapter<LayerTemplateListAdapter.CustomViewHolder> {


    private Context mContext;
    private List<LayerTemplateListBean> mlist;

    public LayerTemplateListAdapter(Context context, List<LayerTemplateListBean> list) {
        this.mContext = context;
        this.mlist = list;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer_template_list_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.tv_title.setText(mlist.get(position).getName());
        holder.mlist.clear();
        holder.mlist.addAll(mlist.get(position).getTempList());
        /*
         1.把内部RecyclerView的adapter和集合数据通过holder缓存
         2.使内部RecyclerView的adapter提供设置position的方法
         */
        if (holder.mAdapter == null) {
            //初始化内层adapter时,把对应的position传过去
            holder.mAdapter = new LayerTemplateAdapter(mContext, holder.mlist);
            GridLayoutManager layoutManage = new GridLayoutManager(mContext, 4);
            holder.mRecyclerView.setLayoutManager(layoutManage);
//            holder.rvItemItem.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
            holder.mRecyclerView.setAdapter(holder.mAdapter);
            holder.mAdapter.setOnItemClickListener(new LayerTemplateAdapter.OnItemChildClickListener() {
                @Override
                public void onItemClick(FurnitureBean bean) {
                    if (listener != null) {
                        listener.onItemClick(bean);
                    }
                }
            });
        } else {
            holder.mAdapter.notifyDataSetChanged();
        }
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
        public void onItemClick(FurnitureBean bean);
    }

    public void setOnItemClickListener(OnItemChildClickListener listener) {
        this.listener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_type_name)
        TextView tv_title;
        @BindView(R.id.item_list_view)
        RecyclerView mRecyclerView;

        private LayerTemplateAdapter mAdapter;
        private List<FurnitureBean> mlist = new ArrayList<>();

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

}
