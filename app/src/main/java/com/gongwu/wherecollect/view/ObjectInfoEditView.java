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
    ObjectBean bean;
    @BindView(R.id.yanse_flow)
    FlowViewGroup yanseFlow;
    @BindView(R.id.jijie_flow)
    FlowViewGroup jijieFlow;
    @BindView(R.id.qudao_flow)
    FlowViewGroup qudaoFlow;
    @BindView(R.id.fenlei_flow)
    FlowViewGroup fenleiFlow;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.star_layout)
    View starLayout;
    @BindView(R.id.fenlei_layout)
    View fenleiLayout;
    @BindView(R.id.jiage_layout)
    View jiageLayout;
    @BindView(R.id.yanse_layout)
    View yanseLayout;
    @BindView(R.id.jijie_layout)
    View jijieLayout;
    @BindView(R.id.qita_tv)
    EditText qitaTv;
    @BindView(R.id.qita_layout)
    View qitaLayout;
    @BindView(R.id.qudao_layout)
    View qudaoLayout;
    @BindView(R.id.jiage_edit)
    EditText jiageEdit;
    @BindView(R.id.add_goods_count_layout)
    View addGoodsCountLaytou;
    @BindView(R.id.goods_count_edit)
    EditText goodsCountEdit;
    @BindView(R.id.purchase_time_tv)
    TextView purchaseTimeTv;
    @BindView(R.id.expiry_time_tv)
    TextView expiryTimeTv;
    @BindView(R.id.expiry_time_layout)
    LinearLayout expiryTimeLayout;

    private ChangeListener changeListener;

    public ObjectInfoEditView(Context context) {
        this(context, null);
    }

    public ObjectInfoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_goodsinfo_edit, this);
        ButterKnife.bind(this);
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void init(ObjectBean bean) {
        this.bean = bean;
        updataView();
        initView();
    }

    private void initView() {
        ratingStar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                bean.setStar((int) RatingCount);
                if (changeListener != null) {
                    changeListener.change();
                }
            }
        });
        //默认两位小数
        jiageEdit.setFilters(new InputFilter[]{new MoneyValueFilter()});
        jiageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(jiageEdit.getText())) {
                    bean.setPrice(jiageEdit.getText().toString());
                    if (changeListener != null) {
                        changeListener.change();
                    }
                } else {
                    bean.setPrice(0 + "");
                    if (changeListener != null) {
                        changeListener.change();
                    }
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
                if (changeListener != null) {
                    changeListener.change();
                }
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
                if (changeListener != null) {
                    changeListener.change();
                }
            }
        });
    }

    /**
     * 刷新界面
     */
    public void updataView() {
        setFenlei();//设置分类
        setGoodsCount();//设置数量
        setPurchaseTime();//设置购买时间
        setExpirytime();//设置到期时间
        setColors();//设置颜色
        setJijie();//设置季节
        setQudao();//设置购货渠道
        setStar();//设置星级
        setQita();//其他
        setjjiage();//设置价格
    }

    private void setjjiage() {
        //后台赋值是乱的 列表跟 家具里面的物品 价格参数不一样
        if (bean.getPrice().equals("0") || bean.getPrice().equals("0.0")) {
            jiageEdit.setText("");
        } else {
            jiageEdit.setText(bean.getPrice());
        }
    }

    private void setPurchaseTime() {
        if (!TextUtils.isEmpty(bean.getBuy_date())) {
            purchaseTimeTv.setText(bean.getBuy_date());
        }
    }

    private void setExpirytime() {
        if (!TextUtils.isEmpty(bean.getExpire_date())) {
            expiryTimeTv.setText(bean.getExpire_date());
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
    private void setFenlei() {
        fenleiFlow.removeAllViews();
        if (bean.getCategories() == null) {
            return;
        }
        Collections.sort(bean.getCategories(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        for (int i = 0; i < StringUtils.getListSize(bean.getCategories()); i++) {
            if (i == AppConstant.DEFAULT_INDEX_OF) {
                continue;
            }
            TextView text = (TextView) View.inflate(getContext(), R.layout.flow_textview, null);
            fenleiFlow.addView(text);
            MarginLayoutParams lp = (MarginLayoutParams) text.getLayoutParams();
            lp.bottomMargin = 5;
            lp.topMargin = 5;
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            text.setLayoutParams(lp);
            text.setText(bean.getCategories().get(i).getName());
            text.setBackgroundResource(R.drawable.shape_goods_property_bg);
        }
    }

    /**
     * 设置颜色
     */
    private void setColors() {
        yanseFlow.removeAllViews();
        if (TextUtils.isEmpty(bean.getColor()))
            return;
        String[] colors = bean.getColor().split("、");
        for (int i = 0; i < colors.length; i++) {
            TextView text = (TextView) View.inflate(getContext(), R.layout.flow_textview, null);
            yanseFlow.addView(text);
            MarginLayoutParams lp = (MarginLayoutParams) text.getLayoutParams();
            lp.bottomMargin = 5;
            lp.topMargin = 5;
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            text.setLayoutParams(lp);
            text.setText(colors[i]);
            text.setBackgroundResource(R.drawable.shape_goods_property_bg);
        }
    }

    /**
     * 设置季节
     */
    private void setJijie() {
        jijieFlow.removeAllViews();
        if (TextUtils.isEmpty(bean.getSeason()))
            return;
        String[] seasons = bean.getSeason().split("、");
        for (int i = 0; i < seasons.length; i++) {
            TextView text = (TextView) View.inflate(getContext(), R.layout.flow_textview, null);
            jijieFlow.addView(text);
            MarginLayoutParams lp = (MarginLayoutParams) text.getLayoutParams();
            lp.bottomMargin = 5;
            lp.topMargin = 5;
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            text.setLayoutParams(lp);
            text.setText(seasons[i]);
            text.setBackgroundResource(R.drawable.shape_goods_property_bg);
        }
    }

    /**
     * 设置季节
     */
    private void setQudao() {
        qudaoFlow.removeAllViews();
        if (TextUtils.isEmpty(bean.getChannel()))
            return;
        String[] channel = bean.getChannel().split(">");
        for (int i = 0; i < channel.length; i++) {
            TextView text = (TextView) View.inflate(getContext(), R.layout.flow_textview, null);
            qudaoFlow.addView(text);
            MarginLayoutParams lp = (MarginLayoutParams) text.getLayoutParams();
            lp.bottomMargin = 5;
            lp.topMargin = 5;
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            text.setLayoutParams(lp);
            text.setText(channel[i]);
            text.setBackgroundResource(R.drawable.shape_goods_property_bg);
        }
    }

    @OnClick({R.id.fenlei_layout, R.id.yanse_layout, R.id.jijie_layout, R.id.qudao_layout, R.id.qita_layout,
            R.id.purchase_time_layout, R.id.expiry_time_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fenlei_layout:
                if (bean.getCategories() != null && bean.getCategories().size() > 0) {
                    SelectSortChildActivity.start(getContext(), bean);
                } else {
                    Toast.makeText(getContext(),"请先添加分类", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.yanse_layout:
                SelectColorActivity.start(getContext(), bean);
                break;
            case R.id.jijie_layout:
                SelectSeasonActivity.start(getContext(), bean);
                break;
            case R.id.qudao_layout:
                SelectChannelActivity.start(getContext(), bean);
                break;
            case R.id.qita_layout:
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
                        purchaseTimeTv.setText(bd);
                        bean.setBuy_date(bd);
                        if (changeListener != null) {
                            changeListener.change();
                        }
                    }

                    @Override
                    public void detele() {
                        purchaseTimeTv.setText("");
                        bean.setBuy_date("");
                        if (changeListener != null) {
                            changeListener.change();
                        }
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
                        if (changeListener != null) {
                            changeListener.change();
                        }
                    }

                    @Override
                    public void detele() {
                        expiryTimeTv.setText("");
                        bean.setExpire_date("");
                        if (changeListener != null) {
                            changeListener.change();
                        }
                    }
                };
                expiryDialog.setCancelBtnText(TextUtils.isEmpty(bean.getExpire_date()));
                expiryDialog.show();
                break;
        }
    }

    public static interface ChangeListener {
        public void change();
    }

    public void hintMoreGoodsLayout() {
        starLayout.setVisibility(View.GONE);
        expiryTimeLayout.setVisibility(View.GONE);
        addGoodsCountLaytou.setVisibility(View.GONE);
        qitaLayout.setVisibility(View.GONE);
        jiageLayout.setVisibility(View.GONE);
    }
}
