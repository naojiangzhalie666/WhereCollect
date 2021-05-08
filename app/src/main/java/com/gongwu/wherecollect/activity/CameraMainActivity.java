package com.gongwu.wherecollect.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.gongwu.wherecollect.ImageSelect.ImageGridActivity;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.object.AddGoodsActivity;
import com.gongwu.wherecollect.object.AddMoreGoodsActivity;
import com.gongwu.wherecollect.util.FileUtil;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 相机拍照
 */
@SuppressLint("MissingPermission")
public class CameraMainActivity extends BaseActivity {

    public static final String FRAGMENT_TAG = "camera";
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

    /**
     * 判断单拍还是连拍
     */
    private boolean continuous = false;
    private boolean addMore;
    private RoomFurnitureBean locationCode;
    private ArrayList<String> files = new ArrayList<>();
    private final int maxImags = 10;

    public static void start(Context context, boolean addMore, RoomFurnitureBean locationCode) {
        Intent intent = new Intent(context, CameraMainActivity.class);
        intent.putExtra("addMore", addMore);
        if (locationCode != null) {
            intent.putExtra("locationCode", locationCode);
        }
        context.startActivity(intent);
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
        initView();
        addCamera();
    }

    @Override
    protected void initPresenter() {

    }

    private void initView() {
        addMore = getIntent().getBooleanExtra("addMore", false);
        locationCode = (RoomFurnitureBean) getIntent().getSerializableExtra("locationCode");
        if (addMore) {
            continuous = true;
            continuousText.setVisibility(View.GONE);
            saomaIv.setImageResource(R.drawable.icon_camera_saoma_enable);
            saomaText.setTextColor(getResources().getColor(R.color.color999));
            cameraSaoma.setEnabled(false);
        }
    }

    /**
     * 点击事件
     *
     * @param view view
     */
    @OnClick({R.id.video_switch_flash_layout, R.id.back_view, R.id.continuous_text, R.id.images_list_layout, R.id.camera_select_img_layout, R.id.rl_camera_saoma})
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
        //裁剪界面返回的照片
        if (requestCode == UCrop.REQUEST_CROP) {
            //单拍
            AddGoodsActivity.start(mContext, mOutputFile.exists() ? (mOutputFile.length() > 0 ? mOutputFile.getAbsolutePath() : null) : null, "", locationCode);
            finish();
        }
    }

    @OnClick(R.id.record_button)
    public void onRecordButtonClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultAdapter() {
                @Override
                public void onVideoRecorded(String filePath) {
                }

                @Override
                public void onPhotoTaken(byte[] bytes, String filePath) {
                    File file = new File(filePath);
                    //批量
                    if (continuous) {
                        selectImgIv.setImageResource(R.drawable.icon_camera_img_enable);
                        selectImgTv.setTextColor(getResources().getColor(R.color.color999));
                        selectImgView.setEnabled(false);
                        cameraSaoma.setVisibility(View.GONE);
                        imagesLayout.setVisibility(View.VISIBLE);
                        continuousText.setVisibility(View.GONE);
                        files.add(FileUtil.compress(file, true).getAbsolutePath());
                        imagesView.setImageURI(FileUtil.getUriFromFile(mContext, new File(files.get(files.size() - 1))));
                        numText.setText(String.valueOf(files.size()));
                        if (files.size() == maxImags) {
                            AddMoreGoodsActivity.start(mContext, files, locationCode);
                            finish();
                        }
                    } else {
                        startCropBitmap(mContext, file);
                    }
                }
            }, App.CACHEPATH, String.valueOf(System.currentTimeMillis()));
        }
    }

    @Override
    public void onBackPressed() {
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
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            addCamera();
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    public void addCamera() {

        final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR).build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();

        if (cameraFragment != null) {
            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                @Override
                public void lockControls() {
                    recordButton.setEnabled(false);
                }

                @Override
                public void unLockControls() {
                    recordButton.setEnabled(true);
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                }

                @Override
                public void allowRecord(boolean allow) {
                    recordButton.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                }
            });
        }
    }

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
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
}
