package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.AesUtil;
import com.gongwu.wherecollect.util.FileUtil;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mucll on 2018/3/15.
 */

public class AddGoodsDialog extends Dialog {

    @BindView(R.id.add_goods_iv)
    GoodsImageView mImageView;
    @BindView(R.id.goods_name_et)
    EditText goodsNameEdit;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    private Activity mContext;
    private ObjectBean mGoodsBean;
    AddGoodsImgDialog selectImgDialog;
    private int goodsCount;
    private int goodsMaxCount = 10;
    /**
     * 图片原文件 dialog dismiss的时候就会被初始化
     */
    private File imgOldFile;

    public AddGoodsDialog(Activity context, int goodsCount) {
        super(context);
        this.mContext = context;
        this.goodsCount = goodsCount;
    }

    /**
     * 设置数据
     *
     * @param bean
     */
    public void setObjectBean(ObjectBean bean) {
        if (bean != null) {
            this.mGoodsBean = bean;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            });
        } else {
            this.mGoodsBean = new ObjectBean();
            initUi();
        }
    }

    public ObjectBean getGoodsBean() {
        return mGoodsBean;
    }

    private void initUi() {
        mImageView.setImageResource(R.drawable.select_pic);
        goodsNameEdit.setText(null);
    }

    private void initData() {
        if (!TextUtils.isEmpty(mGoodsBean.getName())) {
            goodsNameEdit.setText(mGoodsBean.getName());
            goodsNameEdit.setSelection(mGoodsBean.getName().length());
        }
        if (!TextUtils.isEmpty(mGoodsBean.getObject_url())) {
            if (mGoodsBean.getObject_url().contains("http")) {
                mImageView.setImg(mGoodsBean.getObject_url(), 3, true);
            } else if (mGoodsBean.getObject_url().contains("#")) {
                //给了默认色
                mImageView.setImageResource(R.drawable.select_pic);
            } else {
                imgOldFile = new File(mGoodsBean.getObject_url());
                mImageView.setImg(mGoodsBean.getObject_url(), 3);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_goods_layout);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.add_goods_iv, R.id.code_layout, R.id.cancel_tv, R.id.submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_goods_iv:
                showSelectDialog();
                break;
            case R.id.code_layout:
                break;
            case R.id.cancel_tv:
                if (imgOldFile != null) {
                    imgOldFile = null;
                }
                StringUtils.hideKeyboard(goodsNameEdit);
                cancel();
                dismiss();
                break;
            case R.id.submit_tv:
                if (goodsCount >= goodsMaxCount && !mGoodsBean.isSelect()) {
                    ToastUtil.show(mContext, "一次最多添加9个物品", Toast.LENGTH_SHORT);
                    return;
                }
                //确定
                if (!TextUtils.isEmpty(goodsNameEdit.getText().toString().trim())) {
                    mGoodsBean.setName(goodsNameEdit.getText().toString().trim());
                }
                if (TextUtils.isEmpty(mGoodsBean.getObjectUrl()) && TextUtils.isEmpty(goodsNameEdit.getText().toString().trim())) {
                    ToastUtil.show(mContext, "图片或名称至少选填一项", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(mGoodsBean.getObjectUrl())) {
                    // 随机颜色
                    Random random = new Random();
                    int randomcolor = random.nextInt(AppConstant.COCLOR_COUNT);
                    mGoodsBean.setObject_url(StringUtils.getResCode(randomcolor));
                }
                result(mGoodsBean);
                //初始化imgOldFile
                imgOldFile = null;
                dismiss();
                break;
        }
    }

    /**
     * 图片选择
     */
    private void showSelectDialog() {
        //bean url没数据的时候，编辑选择肯定是隐藏的
        if (TextUtils.isEmpty(mGoodsBean.getObject_url())) {
            imgOldFile = null;
        }
        selectImgDialog = new AddGoodsImgDialog(mContext, mGoodsBean, true);
        selectImgDialog.setAddGoodsImgDialogListener(new AddGoodsImgDialog.OnAddGoodsImgDialogListener() {
            @Override
            public void getTBbarCodeClick() {
                //淘口令
//                initInputDialog();
            }

            @Override
            public void getBarCodeType(String code, String barcodeType) {
                //条形扫码
                scanCode(code, barcodeType);
            }

            @Override
            public void getResult(File file) {
                //结果
                mGoodsBean.setObject_url(file.getAbsolutePath());
                mImageView.setImg(mGoodsBean.getObject_url(), 3);
            }

            @Override
            public void editImg() {
                //编辑
                if (TextUtils.isEmpty(mGoodsBean.getObjectUrl())) return;
                if (mGoodsBean.getObject_url().contains("http")) {
                    if (mImageView.getBitmap() != null && selectImgDialog != null) {
                        selectImgDialog.cropBitmap(FileUtil.getFile(mImageView.getBitmap(), System.currentTimeMillis() + ".jpg"));
                    }
                } else if (mGoodsBean.getObject_url().contains(App.CACHEPATH) && selectImgDialog != null) {
                    //图片有地址 直接传
                    selectImgDialog.cropBitmap(mGoodsBean.getObjectFiles());
                }
            }
        });
    }


    private void setDataUrl(List<File> fileList) {
        List<ObjectBean> beans = new ArrayList<>();
        for (File file : fileList) {
            ObjectBean bean = new ObjectBean();
            bean.setObject_url(file.getPath());
            beans.add(bean);
        }
        results(beans);
        dismiss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (selectImgDialog != null) {
            selectImgDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void show() {
        super.show();
    }

    public void result(ObjectBean bean) {

    }

    public void results(List<ObjectBean> beans) {

    }

    public void cancel() {

    }

    public void scanCode(String code, String barcodeType) {

    }

}
