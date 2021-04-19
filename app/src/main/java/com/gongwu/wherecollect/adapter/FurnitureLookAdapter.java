package com.gongwu.wherecollect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;

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
public class FurnitureLookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int GOODS_TYPE = 0;
    private final int BOX_TYPE = 1;

    public MyOnItemClickListener onItemClickListener;
    Context context;
    List<ObjectBean> mlist;

    public FurnitureLookAdapter(Context context, List<ObjectBean> list) {
        this.context = context;
        this.mlist = list;
    }

    /**
     * 可以返回不同类型加载不同布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mlist != null && mlist.size() > 0) {
            if (mlist.get(position).getLevel() == AppConstant.LEVEL_BOX) {
                return BOX_TYPE;
            } else {
                return GOODS_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (BOX_TYPE == viewType) {
            view = View.inflate(context, R.layout.item_furniture_box_recyclerview, null);
            return new BoxViewHolder(view);
        } else {
            view = View.inflate(context, R.layout.item_furniture_goods_recyclerview, null);
            return new CustomViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ObjectBean tempBean = mlist.get(position);
        if (viewHolder instanceof CustomViewHolder) {
            CustomViewHolder holder = (CustomViewHolder) viewHolder;
            holder.imgLayout.setBackground(null);
            initImgView(holder.mImgView);
            holder.itemView.setAlpha(1.0f);
            if (tempBean.getLevel() == AppConstant.LEVEL_BOX) {
                holder.mImgView.head.setImageDrawable(context.getDrawable(R.drawable.icon_template_box));
                if (MainActivity.moveBoxBean != null && MainActivity.moveBoxBean.getCode().equals(tempBean.getCode())) {
                    holder.itemView.setAlpha(0.5f);
                }
            } else {
                holder.imgLayout.setBackground(context.getDrawable(R.drawable.select_furniture_look_item_bg));
                initImgViewData(holder.mImgView, tempBean, 3);
            }
            holder.name.setText(tempBean.getName());
            holder.imgLayout.setSelected(tempBean.isSelect());
            holder.name.setSelected(tempBean.isSelect());
        } else {
            BoxViewHolder holder = (BoxViewHolder) viewHolder;
            holder.boxName.setText(tempBean.getName());
            if (TextUtils.isEmpty(tempBean.getImage_url()) || "null".equals(tempBean.getImage_url())) {
                holder.mBoxIv.head.setImageDrawable(context.getDrawable(R.drawable.icon_template_box));
            } else {
                holder.mBoxIv.setImg(tempBean.getImage_url());
            }
            initImgView(holder.mBoxIvOne);
            initImgView(holder.mBoxIvTwo);
            initImgView(holder.mBoxIvThree);
            initImgView(holder.mBoxIvFour);
            if (tempBean.getGoodsByBox() != null && tempBean.getGoodsByBox().size() > 0) {
                for (int i = 0; i < tempBean.getGoodsByBox().size(); i++) {
                    ObjectBean goods = tempBean.getGoodsByBox().get(i);
                    switch (i) {
                        case 0:
                            initImgViewData(holder.mBoxIvOne, goods, 0);
                            break;
                        case 1:
                            initImgViewData(holder.mBoxIvTwo, goods, 0);
                            break;
                        case 2:
                            initImgViewData(holder.mBoxIvThree, goods, 0);
                            break;
                        case 3:
                            initImgViewData(holder.mBoxIvFour, goods, 0);
                            break;
                    }
                }
            }
        }
    }

    private void initImgViewData(GoodsImageView view, ObjectBean bean, int radius) {
        if (bean.getObject_url().contains("http")) {
            view.setImg(bean.getObject_url(), radius);
        } else {
            int resId = Color.parseColor(bean.getObject_url());
            view.setResourceColor(bean.getName(), resId, radius);
        }
    }

    private void initImgView(GoodsImageView view) {
        view.name.setText(null);
        view.name.setTextSize(10);
        view.head.setBackground(null);
        view.head.setImageDrawable(null);
    }

    @Override
    public int getItemCount() {
        return StringUtils.getListSize(mlist);
    }


    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_view)
        GoodsImageView mImgView;
        @BindView(R.id.object_name_tv)
        TextView name;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.img_layout)
        RelativeLayout imgLayout;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    public class BoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.furniture_box_iv)
        GoodsImageView mBoxIv;
        @BindView(R.id.box_name_tv)
        TextView boxName;
        @BindView(R.id.img_box_view_one)
        GoodsImageView mBoxIvOne;
        @BindView(R.id.img_box_view_two)
        GoodsImageView mBoxIvTwo;
        @BindView(R.id.img_box_view_three)
        GoodsImageView mBoxIvThree;
        @BindView(R.id.img_box_view_four)
        GoodsImageView mBoxIvFour;

        public BoxViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    public ObjectBean getSelectGoods() {
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                return bean;
            }
        }
        return null;
    }


    public String getSelectGoodsIds() {
        StringBuilder sb = new StringBuilder();
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                sb.append(bean.get_id()).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public List<String> getSelectGoodsIdsToList() {
        List<String> ids = new ArrayList<>();
        for (ObjectBean bean : mlist) {
            if (bean.isSelect()) {
                ids.add(bean.get_id());
            }
        }
        return ids;
    }

    public void delSelectGoods(List<ObjectBean> mData) {
        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i).isSelect()) {
                String id = mlist.get(i).get_id();
                for (int j = 0; j < mData.size(); j++) {
                    if (id.equals(mData.get(j).get_id())) {
                        mData.remove(j);
                        break;
                    }
                }
                mlist.remove(i);
                i--;
            }
        }
        notifyDataSetChanged();
    }
}
