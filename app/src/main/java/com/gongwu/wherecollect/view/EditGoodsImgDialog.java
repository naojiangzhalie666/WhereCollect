package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.gongwu.wherecollect.ImageSelect.ImageGridActivity;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.CameraMainActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.PermissionUtil;
import com.gongwu.wherecollect.util.PhotosDialog;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhaojin on 15/11/16.
 */
public class EditGoodsImgDialog {
    public final int REQUST_CAMARE = 0x02;
    public final int REQUST_PHOTOSELECT = 0x03;

    File mOutputFile;
    Activity context;
    private boolean isClip = true;
    private int aspectX = 1;
    private int aspectY = 1;
    private boolean isCanChangeAspect = false;

    public EditGoodsImgDialog(Activity context, ObjectBean objectBean) {
        this.context = context;
        String sdPath = App.CACHEPATH;
        File file = new File(sdPath);
        if (!file.exists()) {
            file.mkdir();
        }
        mOutputFile = new File(sdPath, System.currentTimeMillis() + ".jpg");
        final Dialog dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.layout_edit_goods_img, null);
        if (objectBean.getObject_url().contains("#")) {
            view.findViewById(R.id.look_img).setVisibility(View.GONE);
            view.findViewById(R.id.look_img_split).setVisibility(View.GONE);
            view.findViewById(R.id.edit_img_tv).setVisibility(View.GONE);
            view.findViewById(R.id.edit_img_split).setVisibility(View.GONE);
            view.findViewById(R.id.camare).setBackground(context.getDrawable(R.drawable.select_changheader_top_btn_bg));
        } else {
            view.findViewById(R.id.look_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<ImageData> imageDatas = new ArrayList<>();
                    ImageData imageData = new ImageData();
                    imageData.setUrl(objectBean.getObject_url());
                    imageDatas.add(imageData);
                    PhotosDialog photosDialog = new PhotosDialog(context, false, false, imageDatas);
                    photosDialog.showPhotos(0);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.edit_img_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editImg();
                    dialog.dismiss();
                }
            });
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.camare).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ##########拍照##########
                        CameraMainActivity.start(context, false);
                        dialog.dismiss();
                    }
                });
        view.findViewById(R.id.select).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ######### 调到图片选择界面##########
                        select();
                        dialog.dismiss();
                    }
                });
        dialog.setContentView(view);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.linearLayout).startAnimation(ani);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void select() {
        // ######### 调到图片选择界面##########
        Intent i = new Intent(context, ImageGridActivity.class);
        i.putExtra("max", 1);
        context.startActivityForResult(i, REQUST_PHOTOSELECT);
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
    private void cropBitmap(Uri uri) {
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(mOutputFile));
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.NONE);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(context, R.color.black));
        options.setToolbarWidgetColor(ActivityCompat.getColor(context, R.color.white));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(context, R.color.black));
        //是否能调整裁剪框
        //        options.setAspectRatioOptions(5,new AspectRatio("1:1",1f,1f),new AspectRatio("1:1",1f,1f));
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setFreeStyleCropEnabled(isCanChangeAspect);
        uCrop.withOptions(options)
                .withMaxResultSize(720, 720);
        //设置裁剪图片的宽高比，比如16：9（设置后就不能选择其他比例了、选择面板就不会出现了）
        uCrop.withAspectRatio(aspectX, aspectY);
        uCrop.start(context);
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
        options.setToolbarColor(ActivityCompat.getColor(context, R.color.black));
        options.setToolbarWidgetColor(ActivityCompat.getColor(context, R.color.white));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(context, R.color.black));
        //是否能调整裁剪框
        //        options.setAspectRatioOptions(5,new AspectRatio("1:1",1f,1f),new AspectRatio("1:1",1f,1f));
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options).withMaxResultSize(720, 720);
        //设置裁剪图片的宽高比，比如16：9（设置后就不能选择其他比例了、选择面板就不会出现了）
        uCrop.withAspectRatio(1, 1);
        uCrop.start(((Activity) context));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            final Uri resultUri = UCrop.getOutput(data);
//            mOutputFile = new File(getRealPathFromURI(resultUri));
            getResult(mOutputFile);
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
                        getResult(mOutputFile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data != null && requestCode == CameraMainActivity.CAMERA_CODE) {
            String path = data.getStringExtra(CameraMainActivity.CAMERA_TAG);
            if (!TextUtils.isEmpty(path)) {
                mOutputFile = new File(path);
                getResult(mOutputFile);
            }
        }
    }

    public void getResult(File file) {
    }

    public void editImg() {
    }

    public void onSave(Bundle outState) {
        outState.putSerializable("file", mOutputFile);
    }

    private String getRealPathFromURI(Uri contentUri) { //传入图片uri地址
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
