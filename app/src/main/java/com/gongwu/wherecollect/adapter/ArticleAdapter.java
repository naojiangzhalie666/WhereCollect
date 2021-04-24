package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;
import com.gongwu.wherecollect.net.entity.response.ArticleChildBean;
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
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.CustomViewHolder> {

    private Context mContext;
    private List<ArticleChildBean> mList;

    public ArticleAdapter(Context context, List<ArticleChildBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_child_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ArticleChildBean bean = mList.get(position);
        holder.nameTv.setText(bean.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.start(mContext, bean.getTitle(), new StringBuilder(Config.BASE_URL).append("article?identifier=").append(bean.getIdentifier()).toString(), 50);
            }
        });
        holder.splitView.setVisibility(position == (mList.size() - 1) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return StringUtils.getListSize(mList);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_srticle_name_tv)
        TextView nameTv;
        @BindView(R.id.split_view)
        View splitView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

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
}
