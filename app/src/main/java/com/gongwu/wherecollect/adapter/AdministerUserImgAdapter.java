package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.SharedUserBean;
import com.gongwu.wherecollect.util.ImageLoader;
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
public class AdministerUserImgAdapter extends RecyclerView.Adapter<AdministerUserImgAdapter.CustomViewHolder> {


    private Context mContext;
    private List<SharedUserBean> mlist;

    public AdministerUserImgAdapter(Context context, List<SharedUserBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_administer_user_img_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        SharedUserBean bean = mlist.get(position);
        ImageLoader.loadCircle(mContext, holder.mImageView, bean.getAvatar(), R.drawable.ic_user_error);
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


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.administer_user_img_iv)
        ImageView mImageView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
