package com.gongwu.wherecollect.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.DetailedGoodsBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListBoxesBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.util.DateUtil;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.QRCode;

import java.util.ArrayList;
import java.util.List;

public class DetailedListView extends LinearLayout {

    private Context mContext;
    private ImageView furnitureImgIv;
    private ImageView codeImgView;
    private TextView furnitureNameTv;
    private TextView goodsCountTv;
    private TextView detailed_time_tv;
    private TextView detailed_number_tv;
    private LinearLayout detailedViewLayout;

    public DetailedListView(Context context) {
        this(context, null);
    }

    public DetailedListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailedListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_detailed_list, this);
        furnitureImgIv = view.findViewById(R.id.furniture_img_iv);
        furnitureNameTv = view.findViewById(R.id.furniture_name_tv);
        goodsCountTv = view.findViewById(R.id.furniture_count_tv);
        detailedViewLayout = view.findViewById(R.id.detailed_view_layout);
        detailed_time_tv = view.findViewById(R.id.detailed_time_tv);
        detailed_number_tv = view.findViewById(R.id.detailed_number_tv);
        codeImgView = view.findViewById(R.id.code_img_iv);
    }

    //一行5个
    private final double row_count = 5.0;
    //一个页面最多6行
    private int maxLine = 6;
    private int initLine = 0;

    /**
     * @param qrcodeString 二维码
     */
    public DetailedListBean initData(DetailedListBean bean, String qrcodeString, RoomFurnitureResponse structure, int indexof, int initCount, List<String> gcCodes, List<String> boxCodes) {
        initLine = 0;
        initQRCode(qrcodeString);
        ImageLoader.load(mContext, furnitureImgIv, bean.getFurniture_img());
        furnitureNameTv.setText(bean.getFurniture_name() + "的物品清单");
        goodsCountTv.setText(bean.getObj_count() + "个物品");
        detailed_time_tv.setText(DateUtil.getTime());
        detailed_number_tv.setText(indexof + "/" + initCount);
        for (int i = 0; i < bean.getObjects().size(); i++) {
            DetailedListGoodsBean listGoodsBean = bean.getObjects().get(i);
            //删掉没有物品的隔层数据
            if (listGoodsBean.getObj_count() <= 0 ||
                    ((listGoodsBean.getObjs() == null || listGoodsBean.getObjs().size() == 0) &&
                            ((listGoodsBean.getBoxes() == null || listGoodsBean.getBoxes().size() == 0)))) {
                bean.getObjects().remove(i);
                i--;
                continue;
            }
            //隔层总物品数量(盒子里的+不在盒子里的)
            //除5看能显示几行
            int childGoodsCount = 0;
            if (listGoodsBean.getObjs() != null && listGoodsBean.getObjs().size() > 0) {
                childGoodsCount += listGoodsBean.getObjs().size();
            }
            if (listGoodsBean.getBoxes() != null && listGoodsBean.getBoxes().size() > 0) {
                List<DetailedListBoxesBean> boxesBeans = listGoodsBean.getBoxes();
                for (int y = 0; y < boxesBeans.size(); y++) {
                    DetailedListBoxesBean boxesBean = boxesBeans.get(y);
                    if (boxesBean.getObjs() != null && boxesBean.getObjs().size() > 0) {
                        boxesBean.getObjs().get(0).setBoxType(true);
                        boxesBean.getObjs().get(0).setBoxName(boxesBean.getBox_name());
                        childGoodsCount += boxesBean.getObjs().size();
                    }
                }
            }
            int line = (int) Math.ceil(childGoodsCount / row_count);
            if (initLine >= 5) {
                break;
            }

            if (listGoodsBean.getObjs() != null && listGoodsBean.getObjs().size() > 0) {
                //添加type标记
                listGoodsBean.getObjs().get(0).setShowGCType(true);
            }
            //超过显示数量
            if (initLine + line > maxLine) {
                int surplus = maxLine - initLine;
                int count = (int) (surplus * row_count);
                DetailedListGoodsBean newBean = new DetailedListGoodsBean();
                newBean.setLayer_code(listGoodsBean.getLayer_code());
                newBean.setLayer_name(listGoodsBean.getLayer_name());
                newBean.setObj_count(listGoodsBean.getObj_count());
                newBean.setPosition(listGoodsBean.getPosition());
                newBean.setRatio(listGoodsBean.getRatio());
                newBean.setScale(listGoodsBean.getScale());
                List<DetailedGoodsBean> objs = new ArrayList<>();
                List<DetailedListBoxesBean> boxes = new ArrayList<>();
                //判断隔层物品是否超过显示最大数量,如果超过直接分割隔层物品,没超过再判断收纳盒内物品数量
                if (listGoodsBean.getObjs() != null && listGoodsBean.getObjs().size() >= count) {
                    for (int a = 0; a < count; a++) {
                        objs.add(listGoodsBean.getObjs().get(a));
                    }
                    listGoodsBean.getObjs().subList(0, count).clear();
                } else {
                    if (listGoodsBean.getObjs() != null) {
                        objs.addAll(listGoodsBean.getObjs());
                        listGoodsBean.setObjs(null);
                    }
                    //还差多少
                    count = count - objs.size();
                    if (listGoodsBean.getBoxes() != null && listGoodsBean.getBoxes().size() > 0) {
                        //收纳盒集合
                        List<DetailedListBoxesBean> boxesBeans = listGoodsBean.getBoxes();
                        for (int a = 0; a < boxesBeans.size(); a++) {
                            //收纳盒
                            DetailedListBoxesBean boxesBean = boxesBeans.get(a);
                            //单个收纳盒中物品数量
                            if (boxesBean.getObjs() != null && boxesBean.getObjs().size() > 0) {
                                if (count >= boxesBean.getObjs().size()) {
                                    boxes.add(boxesBean);
                                    boxesBeans.remove(a);
                                    //添加完一个收纳盒后 还差多少
                                    count = count - boxesBean.getObjs().size();
                                    if (count == 0) {
                                        break;
                                    }
                                    a--;
                                } else {
                                    DetailedListBoxesBean newBoxesBean = new DetailedListBoxesBean();
                                    newBoxesBean.setBox_code(boxesBean.getBox_code());
                                    newBoxesBean.setBox_name(boxesBean.getBox_name());
                                    List<DetailedGoodsBean> boxGoods = new ArrayList<>();
                                    for (int b = 0; b < count; b++) {
                                        boxGoods.add(boxesBean.getObjs().get(a));
                                    }
                                    newBoxesBean.setObjs(boxGoods);
                                    //移除数据
                                    boxesBean.getObjs().subList(0, count).clear();
                                    boxes.add(newBoxesBean);
                                    break;
                                }
                            }
                        }
                    }
                }
                newBean.setObjs(objs);
                newBean.setBoxes(boxes);
                if (newBean.getBoxes() != null && newBean.getBoxes().size() > 0) {
                    for (int y = 0; y < newBean.getBoxes().size(); y++) {
                        if (boxCodes.contains(newBean.getBoxes().get(y).getBox_code())) {
                            if (newBean.getBoxes().get(y).getObjs() != null && newBean.getBoxes().get(y).getObjs().size() > 0) {
                                newBean.getBoxes().get(y).getObjs().get(0).setBoxType(false);
                            }
                        } else {
                            boxCodes.add(newBean.getBoxes().get(y).getBox_code());
                        }
                    }
                }
                DetailedView detailedView = new DetailedView(mContext);
                detailedView.initData(newBean, structure, gcCodes.contains(listGoodsBean.getLayer_code()));
                if (!gcCodes.contains(listGoodsBean.getLayer_code())) {
                    gcCodes.add(listGoodsBean.getLayer_code());
                }
                detailedViewLayout.addView(detailedView);
                break;
            } else {
                initLine += line;
                if (listGoodsBean.getBoxes() != null && listGoodsBean.getBoxes().size() > 0) {
                    for (int y = 0; y < listGoodsBean.getBoxes().size(); y++) {
                        if (boxCodes.contains(listGoodsBean.getBoxes().get(y).getBox_code())) {
                            if (listGoodsBean.getBoxes().get(y).getObjs() != null && listGoodsBean.getBoxes().get(y).getObjs().size() > 0) {
                                listGoodsBean.getBoxes().get(y).getObjs().get(0).setBoxType(false);
                            }
                        } else {
                            boxCodes.add(listGoodsBean.getBoxes().get(y).getBox_code());
                        }
                    }
                }
                DetailedView detailedView = new DetailedView(mContext);
                detailedView.initData(listGoodsBean, structure, gcCodes.contains(listGoodsBean.getLayer_code()));
                detailedViewLayout.addView(detailedView);
                if (!gcCodes.contains(listGoodsBean.getLayer_code())) {
                    gcCodes.add(listGoodsBean.getLayer_code());
                }
                bean.getObjects().remove(i);
            }
            i--;
        }
        if (bean.getObjects() != null && bean.getObjects().size() > 0) {
            return bean;
        } else {
            return null;
        }
    }

    private void initQRCode(String qrcodeString) {
        Glide.with(getContext()).load(R.drawable.icon_app_img).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap bitmap = QRCode.createQRCodeWithLogo(qrcodeString, 800, resource
                );
//        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)
                codeImgView.setImageBitmap(bitmap);
            }
        });
    }
}
