package com.gongwu.wherecollect.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.GlideRoundTransform;
import com.gongwu.wherecollect.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 2016/9/15
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class GoodsImageView extends FrameLayout {
    @BindView(R.id.head)
    public ImageView head;
    @BindView(R.id.name)
    public TextView name;
    Context context;

    private Bitmap bitmap;
    private boolean isInitBitmap;
    private GlideListener listener;
    private final int substringlength = 4;

    public GoodsImageView(Context context) {
        this(context, null);
    }

    public GoodsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View v = View.inflate(context, R.layout.layout_goods_image_view, null);
        this.addView(v);
        ButterKnife.bind(this, v);
    }

    public void setTextSize(int size) {
        name.setTextSize(size);
    }

    public void setImageResource(int id) {
        name.setVisibility(GONE);
        head.setImageResource(id);
        Glide.with(context)
                .load(id)
                .asBitmap()
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        head.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    /**
     * 设置用户
     *
     * @param userId
     * @param nickName
     * @param headUrl
     */
    public void setHead(final String userId, String nickName, String headUrl) {
        name.setVisibility(VISIBLE);
        nickName = TextUtils.isEmpty(nickName) ? userId : nickName;
        name.setText(getEndNick(nickName));
        name.setTag(userId);
        head.setImageResource(StringUtils.getResId(Integer.parseInt(userId)));
        Glide.with(context)
                .load(headUrl)
                .asBitmap()
                .placeholder(StringUtils.getResId(Integer.parseInt(userId)))
                .dontAnimate()
                .diskCacheStrategy
                        (DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new BitmapImageViewTarget(head) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        if (name.getTag().toString().equals(userId)) {
                            head.setImageBitmap(resource);
                            name.setVisibility(GONE);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        try {
                            if (name.getTag().toString().equals(userId)) {
                                head.setImageResource(StringUtils.getResId(Integer.parseInt(userId)));
                                name.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    public void setHead(final String userId, String nickName, String headUrl, int radius) {
        name.setVisibility(VISIBLE);
        nickName = TextUtils.isEmpty(nickName) ? userId : nickName;
        name.setText(getEndNick(nickName));
        name.setTag(userId);
        head.setImageResource(StringUtils.getResId(Integer.parseInt(userId)));
        Glide.with(context)
                .load(headUrl)
                .asBitmap()
                .placeholder(StringUtils.getResId(Integer.parseInt(userId)))
                .dontAnimate()
                .diskCacheStrategy
                        (DiskCacheStrategy.ALL)
                .dontAnimate()
                .transform(new CenterCrop(context), new GlideRoundTransform(context, StringUtils.convertDipToPixels(getContext(), radius)))
                .into(new BitmapImageViewTarget(head) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        if (name.getTag().toString().equals(userId)) {
                            head.setImageBitmap(resource);
                            name.setVisibility(GONE);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        try {
                            if (name.getTag().toString().equals(userId)) {
                                head.setImageResource(StringUtils.getResId(Integer.parseInt(userId)));
                                name.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    public void setCircle(ObjectBean bean) {
        if (bean.getObject_url().contains("http")) {
            loadCircle(bean.getObject_url());
        } else {
            int resId = Color.parseColor(bean.getObject_url());
            setResourceCircle(bean.getName(), resId);
        }
    }

    public void loadCircle(String url) {
        name.setTag(url);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy
                        (DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_img_error)
                .dontAnimate()
                .error(R.drawable.ic_user_error)
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (name.getTag().toString().equals(url)) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(),
                                            resource);
                            circularBitmapDrawable.setCircular(true);
                            head.setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });
    }

    public void setImg(String headUrl, int radius) {
        name.setTag(headUrl);
        Glide.with(context)
                .load(headUrl)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_img_error)
                .dontAnimate()
                .transform(new CenterCrop(context), new GlideRoundTransform(context, radius))
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (name.getTag().toString().equals(headUrl)) {
                            head.setImageBitmap(resource);
                            name.setVisibility(GONE);
                        }
                    }
                });
    }

    public void setImg(String headUrl, int radius, boolean save) {
        isInitBitmap = false;
        name.setTag(headUrl);
        Glide.with(context)
                .load(headUrl)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_img_error)
                .dontAnimate()
                .transform(new CenterCrop(context), new GlideRoundTransform(context, radius))
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (name.getTag().toString().equals(headUrl)) {
                            head.setImageBitmap(resource);
                            name.setVisibility(GONE);
                            if (save) {
                                isInitBitmap = true;
                                bitmap = resource;
                            }
                        }
                    }
                });
    }

    public Bitmap getBitmap() {
        if (isInitBitmap && bitmap != null) {
            return bitmap;
        }
        return null;
    }

    public void setImg(String headUrl) {
        name.setTag(headUrl);
        Glide.with(context)
                .load(headUrl)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_img_error)
                .dontAnimate()
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (name.getTag().toString().equals(headUrl)) {
                            head.setImageBitmap(resource);
                            name.setVisibility(GONE);
                        }
                    }
                });
    }

    public void setResourceColor(String nickName, int resId, int radius) {
        head.setImageDrawable(null);
        name.setVisibility(VISIBLE);
        name.setText(getEndNick(nickName));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(StringUtils.convertDipToPixels(getContext(), radius));
        drawable.setColor(resId);
        head.setBackground(drawable);
    }

    public void setResourceCircle(String nickName, int resId) {
        name.setVisibility(VISIBLE);
        name.setText(getEndNick(nickName));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(resId);
        head.setBackground(drawable);
    }

    /**
     * 获取昵称最后两位
     *
     * @param nick
     * @return
     */
    private String getEndNick(String nick) {
        if (TextUtils.isEmpty(nick)) {
            return "";
        }
        if (nick.length() >= 5) {
            return nick.substring(0, substringlength);
        } else {
            return nick;
        }
    }

    public void setGlideListener(GlideListener listener) {
        this.listener = listener;
    }


    public static interface GlideListener {
        public void onSucces(Bitmap resource);

        public void onError();
    }
}
