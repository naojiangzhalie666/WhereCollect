package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.gongwu.wherecollect.ImageSelect.ImageGridActivity;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.CameraMainActivity;
import com.gongwu.wherecollect.activity.WebActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.AesUtil;
import com.gongwu.wherecollect.util.PhotosDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhaojin on 15/11/16.
 */
public class AddGoodsImgDialog {
    public final int REQUST_CAMARE = 0x02;
    public final int REQUST_PHOTOSELECT = 0x032;

    File mOutputFile;
    Activity mContext;
    private boolean isClip = true;
    private int aspectX = 1;
    private int aspectY = 1;
    private boolean isCanChangeAspect = false;
    private String barcodeType;

    public AddGoodsImgDialog(Activity context, ObjectBean mGoodsBean) {
        this.mContext = context;
        String sdPath = App.CACHEPATH;
        File file = new File(sdPath);
        if (!file.exists()) {
            file.mkdir();
        }
        mOutputFile = new File(sdPath, System.currentTimeMillis() + ".jpg");

        boolean hideEnergy = false;
        if (mGoodsBean != null && !TextUtils.isEmpty(mGoodsBean.getObjectUrl())) {
            if (mGoodsBean.getObjectUrl().contains(sdPath) || mGoodsBean.getObjectUrl().contains("http")) {
                hideEnergy = true;
            }
        }

        final Dialog dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.layout_add_goods_img, null);
        view.findViewById(R.id.energy_item_layout).setVisibility(hideEnergy ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.edit_img_tv).setVisibility(hideEnergy ? View.VISIBLE : View.GONE);
        //拍照
        view.findViewById(R.id.add_goods_camare).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ##########拍照##########
                        CameraMainActivity.start(context, false);
                        dialog.dismiss();
                    }
                });
        //相册选择
        view.findViewById(R.id.select_img_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.books_bar_code_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeType = "books";
                Intent intent = new Intent(mContext, CaptureActivity.class);
                intent.putExtra("title", "图书扫条码");
                ((Activity) context).startActivityForResult(intent, AppConstant.START_BAR_CODE);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.select_bar_code_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeType = "goods";
                Intent intent = new Intent(mContext, CaptureActivity.class);
                intent.putExtra("title", "物品扫条码");
                ((Activity) context).startActivityForResult(intent, AppConstant.START_BAR_CODE);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.select_password_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.getTBbarCodeClick();
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.start_energy_act_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.start(mContext, Config.WEB_ENERGY_NAME, Config.WEB_ENERGY_URL, 50);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.edit_img_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.editImg();
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.linearLayout).startAnimation(ani);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void selectImage() {
        // ######### 调到图片选择界面##########
        Intent i = new Intent(mContext, ImageGridActivity.class);
        i.putExtra("max", 1);
        mContext.startActivityForResult(i, REQUST_PHOTOSELECT);
    }

    public boolean isClip() {
        return isClip;
    }

    public void setClip(boolean clip) {
        isClip = clip;
    }

    /**
     * 设置裁剪框宽高比，默认正方形
     *
     * @param aspectX
     * @param aspectY
     */
    public void setAspectXY(int aspectX, int aspectY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
    }

    // 剪切界面
    public void cropBitmap(File imgFile) {
        mOutputFile = new File(App.CACHEPATH, System.currentTimeMillis() + ".jpg");
        try {
            mOutputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UCrop uCrop = UCrop.of(Uri.fromFile(imgFile), Uri.fromFile(mOutputFile));
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.NONE);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mContext, R.color.black));
        options.setToolbarWidgetColor(ActivityCompat.getColor(mContext, R.color.white));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mContext, R.color.black));
        //是否能调整裁剪框
        //        options.setAspectRatioOptions(5,new AspectRatio("1:1",1f,1f),new AspectRatio("1:1",1f,1f));
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options).withMaxResultSize(720, 720);
        //设置裁剪图片的宽高比，比如16：9（设置后就不能选择其他比例了、选择面板就不会出现了）
        uCrop.withAspectRatio(1, 1);
        uCrop.start(((Activity) mContext));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            final Uri resultUri = UCrop.getOutput(data);
//            mOutputFile = new File(getRealPathFromURI(resultUri));
            if (listener != null) {
                listener.getResult(mOutputFile);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        if (requestCode == REQUST_PHOTOSELECT) {
            try {
                List<ImageData> temp = (ArrayList<ImageData>) data.getSerializableExtra("list");
                if (temp != null && temp.size() > 0) {
                    if (isClip) {
                        cropBitmap(new File(temp.get(AppConstant.DEFAULT_INDEX_OF).getBigUri()));
                    } else {
                        mOutputFile = new File(temp.get(AppConstant.DEFAULT_INDEX_OF).getBigUri());
                        if (listener != null) {
                            listener.getResult(mOutputFile);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data != null && requestCode == CameraMainActivity.CAMERA_CODE) {
            String path = data.getStringExtra(CameraMainActivity.CAMERA_TAG);
            if (!TextUtils.isEmpty(path)) {
                mOutputFile = new File(path);
                if (listener != null) {
                    listener.getResult(mOutputFile);
                }
            }
        }
        if (requestCode == AppConstant.START_BAR_CODE && resultCode == CaptureActivity.result) {//扫描的到结果
            String result = data.getStringExtra("result");
            if (listener != null) {
                listener.getBarCodeType(result, barcodeType);
            }
        }
    }

    public void onSave(Bundle outState) {
        outState.putSerializable("file", mOutputFile);
    }

    private String getRealPathFromURI(Uri contentUri) { //传入图片uri地址
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    OnAddGoodsImgDialogListener listener;

    public void setAddGoodsImgDialogListener(OnAddGoodsImgDialogListener listener) {
        this.listener = listener;
    }

    public interface OnAddGoodsImgDialogListener {
        void getTBbarCodeClick();

        void getBarCodeType(String code, String barcodeType);

        void getResult(File file);

        void editImg();
    }
}
