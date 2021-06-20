package com.gongwu.wherecollect.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.extensions.BeautyPreviewExtender;
import androidx.camera.extensions.NightImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gongwu.wherecollect.ImageSelect.ImageGridActivity;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.object.AddGoodsActivity;
import com.gongwu.wherecollect.object.AddMoreGoodsActivity;
import com.gongwu.wherecollect.util.FileUtil;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.google.common.util.concurrent.ListenableFuture;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 相机拍照
 */
@SuppressLint("MissingPermission")
public class CameraMainActivity extends BaseActivity {

    public static final String FRAGMENT_TAG = "camera";
    public static final String CAMERA_TAG = "camera_tag";
    public static final int CAMERA_CODE = 0x1252;
    private static final int BOOK_CODE = 0x123;

    @BindView(R.id.record_button)
    ImageButton recordButton;
    @BindView(R.id.continuous_text)
    TextView continuousText;
    @BindView(R.id.images_list_layout)
    View imagesLayout;
    @BindView(R.id.images_view)
    ImageView imagesView;
    @BindView(R.id.num_text)
    TextView numText;
    @BindView(R.id.camera_select_img_layout)
    View selectImgView;
    @BindView(R.id.rl_camera_saoma)
    View cameraSaoma;
    @BindView(R.id.saoma_iv)
    ImageView saomaIv;
    @BindView(R.id.saoma_text)
    TextView saomaText;
    @BindView(R.id.select_img_iv)
    ImageView selectImgIv;
    @BindView(R.id.select_img_tv)
    TextView selectImgTv;
    @BindView(R.id.view_finder)
    PreviewView mPreviewView;

    private ProcessCameraProvider mCameraProvider;
    private Preview mPreview;
    private Camera mCamera;
    private ImageCapture mImageCapture;
    private ImageAnalysis mImageAnalysis;
    /**
     * 判断单拍还是连拍
     */
    private boolean continuous = false;
    private boolean addMore;
    private boolean isShowRightBtn;
    private RoomFurnitureBean locationCode;
    private ArrayList<String> files = new ArrayList<>();
    private final int maxImags = 10;
    private boolean isBack = true;

    public static void start(Context context, boolean addMore, RoomFurnitureBean locationCode) {
        Intent intent = new Intent(context, CameraMainActivity.class);
        intent.putExtra("addMore", addMore);
        if (locationCode != null) {
            intent.putExtra("locationCode", locationCode);
        }
        context.startActivity(intent);
    }

    public static void start(Context context, boolean isShowRightBtn) {
        Intent intent = new Intent(context, CameraMainActivity.class);
        intent.putExtra("isShowRightBtn", isShowRightBtn);
        ((Activity) context).startActivityForResult(intent, CameraMainActivity.CAMERA_CODE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.camerafragment_activity_main;
    }

    @Override
    protected void initViews() {
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        setupCamera(mPreviewView);
        initView();
        files.clear();
    }

    @Override
    protected void initPresenter() {

    }

    private void initView() {
        addMore = getIntent().getBooleanExtra("addMore", false);
        isShowRightBtn = getIntent().getBooleanExtra("isShowRightBtn", true);
        locationCode = (RoomFurnitureBean) getIntent().getSerializableExtra("locationCode");
        if (addMore) {
            continuous = true;
            continuousText.setVisibility(View.GONE);
            saomaIv.setImageResource(R.drawable.icon_camera_saoma_enable);
            saomaText.setTextColor(getResources().getColor(R.color.color999));
            cameraSaoma.setEnabled(false);
        }
        if (!isShowRightBtn) {
            continuous = false;
            continuousText.setVisibility(View.GONE);
        }
        mPreviewView.setOnTouchListener((v, event) -> {
            FocusMeteringAction action = new FocusMeteringAction.Builder(
                    mPreviewView.getMeteringPointFactory()
                            .createPoint(event.getX(), event.getY())).build();
            try {
                showTapView((int) event.getX(), (int) event.getY());
                Log.d("Camera", "Focus camera");
                mCamera.getCameraControl().startFocusAndMetering(action);
            } catch (Exception e) {
                Log.e("Camera", "Error focus camera");
            }
            return false;
        });
    }

    /**
     * 点击事件
     *
     * @param view view
     */
    @OnClick({R.id.video_switch_flash_layout, R.id.back_view, R.id.continuous_text, R.id.images_list_layout,
            R.id.camera_select_img_layout, R.id.rl_camera_saoma, R.id.record_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_view:
                onBackPressed();
                break;
            case R.id.continuous_text:
                if (continuous) {
                    addMore = false;
                    continuousText.setText("批量添加");
                    saomaIv.setImageResource(R.drawable.icon_camera_saoma);
                    saomaText.setTextColor(getResources().getColor(R.color.white));
                    cameraSaoma.setEnabled(true);
                } else {
                    addMore = true;
                    continuousText.setText("单品添加");
                    saomaIv.setImageResource(R.drawable.icon_camera_saoma_enable);
                    saomaText.setTextColor(getResources().getColor(R.color.color999));
                    cameraSaoma.setEnabled(false);
                }
                continuous = !continuous;
                break;
            case R.id.camera_select_img_layout:
                Intent i = new Intent(mContext, ImageGridActivity.class);
                i.putExtra("max", continuous ? 9 : 1);
                startActivityForResult(i, SelectImgDialog.REQUST_PHOTOSELECT);
                break;
            case R.id.rl_camera_saoma:
                startActivityForResult(new Intent(mContext, CaptureActivity.class), BOOK_CODE);
                break;
            case R.id.images_list_layout:
                AddMoreGoodsActivity.start(mContext, files, locationCode);
                finish();
                break;
            case R.id.record_button:
                takenPictureInternal(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BOOK_CODE && resultCode == CaptureActivity.result) {//扫描的到结果
            String result = data.getStringExtra("result");
            AddGoodsActivity.start(mContext, "", result, locationCode);
            finish();
        }
        //裁剪界面返回的照片
        if (requestCode == UCrop.REQUEST_CROP) {
            //单拍
            if (mImgFile != null && mImgFile.exists()) {
                mImgFile.delete();
            }
            if (!isShowRightBtn) {
                Intent intent = new Intent();
                intent.putExtra(CAMERA_TAG, mOutputFile.exists() ? (mOutputFile.length() > 0 ? mOutputFile.getAbsolutePath() : "") : "");
                setResult(RESULT_OK, intent);
            } else {
                AddGoodsActivity.start(mContext, mOutputFile.exists() ? (mOutputFile.length() > 0 ? mOutputFile.getAbsolutePath() : null) : null, "", locationCode);
            }
            finish();
        }
        if (requestCode == SelectImgDialog.REQUST_PHOTOSELECT && resultCode == ImageGridActivity.RESULT) {
            List<ImageData> temp = (ArrayList<ImageData>) data.getSerializableExtra("list");
            if (temp.size() == 1 && !continuous) {
                startCropBitmap(mContext, new File(temp.get(0).getBigUri()));
            } else if (temp.size() > 0 && continuous) {
                for (ImageData id : temp) {
                    files.add(FileUtil.compress(new File(id.getBigUri()), false).getAbsolutePath());
                }
                selectImgIv.setImageResource(R.drawable.icon_camera_img_enable);
                selectImgTv.setTextColor(getResources().getColor(R.color.color999));
                selectImgView.setEnabled(false);
                cameraSaoma.setVisibility(View.GONE);
                imagesLayout.setVisibility(View.VISIBLE);
                continuousText.setVisibility(View.GONE);
                imagesView.setImageURI(FileUtil.getUriFromFile(mContext, new File(files.get(files.size() - 1))));
                numText.setText(String.valueOf(files.size()));
                if (files.size() == maxImags) {
                    AddMoreGoodsActivity.start(mContext, files, locationCode);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isShowRightBtn) {
            Intent intent = new Intent();
            if (addMore) {
                intent.setClass(mContext, AddMoreGoodsActivity.class);
            } else {
                intent.setClass(mContext, AddGoodsActivity.class);
            }
            if (locationCode != null) {
                intent.putExtra("locationCode", locationCode);
            }
            startActivity(intent);
        } else {
            finish();
        }
        super.onBackPressed();
    }


    private File mOutputFile;
    private File mImgFile;

    private void startCropBitmap(Context mContext, File imgOldFile) {
        mImgFile = imgOldFile;
        mOutputFile = new File(App.CACHEPATH, System.currentTimeMillis() + ".jpg");
        try {
            mOutputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //裁剪界面
        cropBitmap(mContext, imgOldFile, mOutputFile);
    }

    // 剪切界面
    private void cropBitmap(Context context, File imgFile, File mOutputFile) {
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

    private void setupCamera(PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                mCameraProvider = cameraProviderFuture.get();
                bindPreview(mCameraProvider, previewView);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void showTapView(int x, int y) {
        Log.d("Camera", "Tap x:" + x + " y:" + y);
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // popupWindow.setBackgroundDrawable(getDrawable(android.R.color.holo_blue_bright));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_focus_view);
        popupWindow.setContentView(imageView);
        // popupWindow.showAtLocation(binding.previewView, Gravity.CENTER, x, y);
        popupWindow.showAsDropDown(mPreviewView, x, y);
        mPreviewView.postDelayed(popupWindow::dismiss, 600);
        // binding.previewView.playSoundEffect(SoundEffectConstants.CLICK);
    }

    private void takenPictureInternal(boolean isExternal) {
        Log.d("Camera", "takenPictureInternal isExternal:" + isExternal);
        File dir = new File(App.CACHEPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //创建文件
        File file = new File(App.CACHEPATH, System.currentTimeMillis() + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        //创建包文件的数据，比如创建文件
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        if (mImageCapture != null) {
            mImageCapture.takePicture(outputFileOptions, CameraXExecutors.mainThreadExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            if (!continuous) {
                                startCropBitmap(mContext, file);
                            } else {
                                files.add(FileUtil.compress(file, true).getAbsolutePath());
                                selectImgIv.setImageResource(R.drawable.icon_camera_img_enable);
                                selectImgTv.setTextColor(getResources().getColor(R.color.color999));
                                selectImgView.setEnabled(false);
                                cameraSaoma.setVisibility(View.GONE);
                                imagesLayout.setVisibility(View.VISIBLE);
                                continuousText.setVisibility(View.GONE);
                                imagesView.setImageURI(FileUtil.getUriFromFile(mContext, new File(files.get(files.size() - 1))));
                                numText.setText(String.valueOf(files.size()));
                                if (files.size() == maxImags) {
                                    AddMoreGoodsActivity.start(mContext, files, locationCode);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Log.d("Camera", "onError:" + exception.getImageCaptureError());
                        }
                    });
        }
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider,
                             PreviewView previewView) {
        Preview.Builder previewBuilder = new Preview.Builder();
        ImageCapture.Builder captureBuilder = new ImageCapture.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation());
        CameraSelector cameraSelector = isBack ? CameraSelector.DEFAULT_BACK_CAMERA
                : CameraSelector.DEFAULT_FRONT_CAMERA;
        mImageAnalysis = new ImageAnalysis.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .setTargetResolution(new Size(720, 1440))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        setPreviewExtender(previewBuilder, cameraSelector);
        mPreview = previewBuilder.build();

        setCaptureExtender(captureBuilder, cameraSelector);
        mImageCapture = captureBuilder.build();

        cameraProvider.unbindAll();
        mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                mPreview, mImageCapture, mImageAnalysis);
        mPreview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    private void setPreviewExtender(Preview.Builder builder, CameraSelector cameraSelector) {
        BeautyPreviewExtender beautyPreviewExtender = BeautyPreviewExtender.create(builder);
        if (beautyPreviewExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            Log.d("Camera", "beauty preview extension enable");
            beautyPreviewExtender.enableExtension(cameraSelector);
        } else {
            Log.d("Camera", "beauty preview extension not available");
        }
    }

    private void setCaptureExtender(ImageCapture.Builder builder, CameraSelector cameraSelector) {
        NightImageCaptureExtender nightImageCaptureExtender = NightImageCaptureExtender.create(builder);
        if (nightImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            Log.d("Camera", "night capture extension enable");
            nightImageCaptureExtender.enableExtension(cameraSelector);
        } else {
            Log.d("Camera", "night capture extension not available");
        }

    }
}
