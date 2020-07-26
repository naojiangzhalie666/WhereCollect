package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
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
public class FurnitureTemplateAdapter extends RecyclerView.Adapter<FurnitureTemplateAdapter.CustomViewHolder> {


    private Context mContext;
    private List<FurnitureBean> mlist;

    public FurnitureTemplateAdapter(Context context, List<FurnitureBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_furniture_template_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        FurnitureBean bean = mlist.get(position);
        holder.templateName.setText(bean.getName());
        ImageLoader.load(mContext, holder.templateImage, bean.getImage_url(), R.drawable.ic_img_error);
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


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.template_name_tv)
        TextView templateName;
        @BindView(R.id.template_image)
        ImageView templateImage;


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
