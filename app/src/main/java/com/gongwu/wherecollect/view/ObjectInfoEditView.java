package com.gongwu.wherecollect.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.object.SelectChannelActivity;
import com.gongwu.wherecollect.object.SelectColorActivity;
import com.gongwu.wherecollect.object.SelectSeasonActivity;
import com.gongwu.wherecollect.object.SelectSortActivity;
import com.gongwu.wherecollect.object.SelectSortChildActivity;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Function:
 * Date: 2017/9/13
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class ObjectInfoEditView extends LinearLayout {

    @BindView(R.id.classify_tv)
    TextView classifyTv;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.qita_tv)
    EditText qitaTv;
    @BindView(R.id.price_edit)
    EditText priceEdit;
    @BindView(R.id.goods_count_edit)
    EditText goodsCountEdit;
    @BindView(R.id.purchase_time_tv)
    TextView purchaseTimeTv;
    @BindView(R.id.expiry_time_tv)
    TextView expiryTimeTv;
    @BindView(R.id.season_tv)
    TextView seasonTv;
    @BindView(R.id.channel_tv)
    TextView channelTv;
    @BindView(R.id.color_tv)
    TextView colorTv;

    private Context mContext;
    private ObjectBean bean;

    public ObjectInfoEditView(Context context) {
        this(context, null);
    }

    public ObjectInfoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate(context, R.layout.layout_goodsinfo_edit, this);
        ButterKnife.bind(this);
        initView();
    }

    public void init(ObjectBean bean) {
        this.bean = bean;
        updataView();
    }

    /**
     * 刷新界面
     */
    public void updataView() {
        setClassify();//设置分类
        setGoodsCount();//设置数量
        setPurchaseTime();//设置购买时间
        setExpirytime();//设置到期时间
        setColors();//设置颜色
        setSeason();//设置季节
        setChannel();//设置购货渠道
        setStar();//设置星级
        setQita();//其他
        setPrice();//设置价格
    }

    /**
     * 设置价格
     */
    private void setPrice() {
        //后台赋值是乱的 列表跟 家具里面的物品 价格参数不一样
        if (!bean.getPrice().equals("0") && !bean.getPrice().equals("0.0")) {
            priceEdit.setText(bean.getPrice());
        }
    }

    /**
     * 设置购买时间
     */
    private void setPurchaseTime() {
        if (!TextUtils.isEmpty(bean.getBuy_date())) {
            setValueText(purchaseTimeTv, bean.getBuy_date());
        }
    }

    /**
     * 设置过期时间
     */
    private void setExpirytime() {
        if (!TextUtils.isEmpty(bean.getExpire_date())) {
            setValueText(expiryTimeTv, bean.getExpire_date());
        }
    }

    /**
     * 设置物品数量
     */
    private void setGoodsCount() {
        if (bean.getCount() > 0) {
            goodsCountEdit.setText(bean.getCount() + "");
            goodsCountEdit.setSelection(goodsCountEdit.getText().toString().length());
        }
    }

    /**
     * 其他
     */
    private void setQita() {
        if (!TextUtils.isEmpty(bean.getDetail())) {
            qitaTv.setText(bean.getDetail());
        }
    }

    /**
     * 设置星级
     */
    private void setStar() {
        ratingStar.setStar(bean.getStar());
    }

    /**
     * 设置分类
     */
    private void setClassify() {
        if (bean.getCategories() == null || bean.getCategories().size() == 0) {
            setHintText(classifyTv, R.string.hint_classify_tv);
            return;
        }
        Collections.sort(bean.getCategories(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bean.getCategories().size(); i++) {
            if (i == AppConstant.DEFAULT_INDEX_OF) {
                continue;
            }
            sb.append(bean.getCategories().get(i).getName()).append("/");
        }
        if (TextUtils.isEmpty(sb.toString())) return;
        setValueText(classifyTv, sb.delete(sb.length() - 1, sb.length()).toString());
    }

    /**
     * 设置颜色
     */
    private void setColors() {
        if (!TextUtils.isEmpty(bean.getColorStr())) {
            setValueText(colorTv, bean.getColorStr());
        } else {
            setHintText(colorTv, R.string.hint_color_tv);
        }
    }

    /**
     * 设置季节
     */
    private void setSeason() {
        if (!TextUtils.isEmpty(bean.getSeason())) {
            setValueText(seasonTv, bean.getSeason());
        } else {
            setHintText(seasonTv, R.string.hint_season_tv);
        }
    }

    /**
     * 设置渠道
     */
    private void setChannel() {
        if (!TextUtils.isEmpty(bean.getChannelStr())) {
            setValueText(channelTv, bean.getChannelStr());
        } else {
            setHintText(channelTv, R.string.hint_channel_tv);
        }
    }

    private void setValueText(TextView textView, String value) {
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.color333));
        textView.setText(value);
    }

    private void setHintText(TextView textView, int textId) {
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorccc));
        textView.setText(textId);
    }

    @OnClick({R.id.classify_layout, R.id.color_layout, R.id.season_layout, R.id.channel_layout,
            R.id.purchase_time_layout, R.id.expiry_time_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.classify_layout:
                if (bean.getCategories() != null && bean.getCategories().size() > 0) {
                    SelectSortChildActivity.start(getContext(), bean, true);
                } else {
                    Toast.makeText(getContext(), "请先添加分类", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.color_layout:
                SelectColorActivity.start(getContext(), bean);
                break;
            case R.id.season_layout:
                SelectSeasonActivity.start(getContext(), bean);
                break;
            case R.id.channel_layout:
                SelectChannelActivity.start(getContext(), bean);
                break;
            case R.id.purchase_time_layout:
                String start = "";
                if (TextUtils.isEmpty(bean.getBuy_date())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    start = formatter.format(curDate);
                } else {
                    start = bean.getBuy_date();
                }
                DateBirthdayDialog dialog = new DateBirthdayDialog(getContext(), start) {
                    @Override
                    public void result(final int year, final int month, final int day) {
                        String bd = year + "-" + StringUtils.formatIntTime(month) + "-" +
                                StringUtils.formatIntTime(day);
                        bean.setBuy_date(bd);
                        setValueText(purchaseTimeTv, bd);
                    }

                    @Override
                    public void detele() {
                        setHintText(purchaseTimeTv, R.string.hint_purchase_time_tv);
                        bean.setBuy_date("");
                    }
                };
                dialog.setCancelBtnText(TextUtils.isEmpty(bean.getBuy_date()));
                dialog.show();
                break;
            case R.id.expiry_time_layout:
                String end = "";
                if (TextUtils.isEmpty(bean.getExpire_date())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    end = formatter.format(curDate);
                } else {
                    end = bean.getExpire_date();
                }
                DateBirthdayDialog expiryDialog = new DateBirthdayDialog(getContext(), end) {
                    @Override
                    public void result(final int year, final int month, final int day) {
                        String bd = year + "-" + StringUtils.formatIntTime(month) + "-" +
                                StringUtils.formatIntTime(day);
                        expiryTimeTv.setText(bd);
                        bean.setExpire_date(bd);
                    }

                    @Override
                    public void detele() {
                        setHintText(expiryTimeTv, R.string.hint_expiry_time_tv);
                        bean.setExpire_date("");
                    }
                };
                expiryDialog.setCancelBtnText(TextUtils.isEmpty(bean.getExpire_date()));
                expiryDialog.show();
                break;
        }
    }

    private void initView() {
        ratingStar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                bean.setStar((int) RatingCount);
            }
        });
        //默认两位小数
        priceEdit.setFilters(new InputFilter[]{new MoneyValueFilter()});
        priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(priceEdit.getText())) {
                    bean.setPrice(priceEdit.getText().toString());
                } else {
                    bean.setPrice(0 + "");
                }
            }
        });
        qitaTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                bean.setDetail(qitaTv.getText().toString());
            }
        });
        goodsCountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(goodsCountEdit.getText().toString())) {
                    bean.setCount(Integer.parseInt(goodsCountEdit.getText().toString()));
                } else {
                    bean.setCount(0);
                }
            }
        });
    }

}
