package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;
import com.gongwu.wherecollect.net.entity.response.ArticleChildBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.SerchListDetailsBean;
import com.gongwu.wherecollect.object.GoodsDetailsActivity;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.FlowViewGroup;

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
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.CustomViewHolder> {

    private Context mContext;
    private List<ArticleBean> mList;

    public ArticleListAdapter(Context context, List<ArticleBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ArticleBean bean = mList.get(position);
        holder.typeTv.setText(bean.getType());
        holder.mList.clear();
        holder.mList.addAll(bean.getArticles());
        holder.mAdapter = new ArticleAdapter(mContext, holder.mList);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        holder.mRecyclerView.setAdapter(holder.mAdapter);
    }

    @Override
    public int getItemCount() {
        return StringUtils.getListSize(mList);
    }

    public OnItemChildClickListener listener;

    public interface OnItemChildClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemChildClickListener listener) {
        this.listener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_srticle_type_tv)
        TextView typeTv;
        @BindView(R.id.item_srticle_list_view)
        RecyclerView mRecyclerView;

        private ArticleAdapter mAdapter;
        private List<ArticleChildBean> mList = new ArrayList<>();

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getLayoutPosition());
            }
        }
    }

}
