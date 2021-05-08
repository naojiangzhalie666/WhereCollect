package com.gongwu.wherecollect.contract.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.gongwu.wherecollect.ImageSelect.ImageGridActivity;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddGoodsContract;
import com.gongwu.wherecollect.contract.model.AddGoodsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.GoodsInfoBean;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.object.AddGoodsActivity;
import com.gongwu.wherecollect.object.AddMoreGoodsActivity;
import com.gongwu.wherecollect.util.DateUtil;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.FileUtil;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.PermissionUtil;
import com.gongwu.wherecollect.util.QiNiuUploadUtil;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.gongwu.wherecollect.util.StringUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AddGoodsPresenter extends BasePresenter<IAddGoodsContract.IAddGoodsView> implements IAddGoodsContract.IAddGoodsPresenter {

    private static final String TAG = "AddGoodsPresenter";

    private static final int REQUST_CAMARE = 0x423;

    private static final int REQUST_PHOTOSELECT = 0x03;

    private File mOutputFile;

    private static class Inner {
        private static final AddGoodsPresenter instance = new AddGoodsPresenter();
    }

    private IAddGoodsContract.IAddGoodsModel mModel;

    private AddGoodsPresenter() {
        mModel = new AddGoodsModel();
    }

    public static AddGoodsPresenter getInstance() {
        return AddGoodsPresenter.Inner.instance;
    }

    /**
     * 上传图片
     *
     * @param mContext 上下文
     * @param file     图片
     */
    public void uploadImg(Context mContext, File file) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        QiNiuUploadUtil uploadUtil = QiNiuUploadUtil.getInstance(mContext);
        uploadUtil.setUpLoadListener(new QiNiuUploadUtil.UpLoadListener() {
            @Override
            public void onUpLoadSuccess(String path) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onUpLoadSuccess(path);
                }
            }

            @Override
            public void onUpLoadError(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
        uploadUtil.start(AppConstant.UPLOAD_GOODS_IMG_PATH, file);
    }


    @Override
    public void editGoods(Context context, ObjectBean tempBean, String names, String isbn) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddGoodsReq goodsReq = new AddGoodsReq();
        goodsReq.setUid(App.getUser(context).getId());
        goodsReq.setISBN(isbn);
        goodsReq.setChannel(TextUtils.isEmpty(tempBean.getChannel()) ? "" : JsonUtils.jsonFromObject(tempBean.getChannel().split(">")));
        goodsReq.setColor(TextUtils.isEmpty(tempBean.getColor()) ? "" : JsonUtils.jsonFromObject(tempBean.getColor().split("、")));
        goodsReq.setDetail(TextUtils.isEmpty(tempBean.getDetail()) ? "" : tempBean.getDetail());
        goodsReq.setPrice_max(tempBean.getPrice() + "");
        goodsReq.setPrice_min(tempBean.getPrice() + "");
        goodsReq.setSeason(tempBean.getSeason());
        goodsReq.setStar(tempBean.getStar() + "");
        goodsReq.setName(names);
        goodsReq.setImage_url(tempBean.getObject_url());
        goodsReq.setCount(tempBean.getCount() + "");
        goodsReq.setBuy_date(tempBean.getBuy_date());
        goodsReq.setExpire_date(tempBean.getExpire_date());
        goodsReq.setCode(tempBean.get_id());
        if (tempBean.getCategories() != null && tempBean.getCategories().size() > 0) {
            StringBuilder ca = new StringBuilder();
            for (int i = 0; i < StringUtils.getListSize(tempBean.getCategories()); i++) {
                ca.append(tempBean.getCategories().get(i).getCode());
                if (i != tempBean.getCategories().size() - 1) {
                    ca.append(",");
                }
            }
            goodsReq.setCategory_codes(ca.toString());
        }
        if (tempBean.getLocations() != null && tempBean.getLocations().size() > 0) {
            StringBuilder lc = new StringBuilder();
            for (int i = 0; i < StringUtils.getListSize(tempBean.getLocations()); i++) {
                lc.append(tempBean.getLocations().get(i).getCode());
                if (i != tempBean.getLocations().size() - 1) {
                    lc.append(",");
                }
            }
            goodsReq.setLocation_codes(lc.toString());
        }
        mModel.editGoods(goodsReq, new RequestCallback<ObjectBean>() {

            @Override
            public void onSuccess(ObjectBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editGoodsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void addObjects(Context context, ObjectBean tempBean, String names, String isbn) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        List<String> namesList = new ArrayList<>();
        namesList.add(names);
        List<String> files = new ArrayList<>();
        files.add(tempBean.getObject_url());
        AddGoodsReq goodsReq = new AddGoodsReq();
        goodsReq.setUid(App.getUser(context).getId());
        goodsReq.setISBN(isbn);
        goodsReq.setCategory_codes(StringUtils.isEmpty(tempBean.getCategories()) ? "" : tempBean.getCategories().get(tempBean.getCategories().size() - 1).getCode());
        goodsReq.setChannel(TextUtils.isEmpty(tempBean.getChannel()) ? "" : JsonUtils.jsonFromObject(tempBean.getChannel().split(">")));
        goodsReq.setColor(TextUtils.isEmpty(tempBean.getColor()) ? "" : JsonUtils.jsonFromObject(tempBean.getColor().split("、")));
        goodsReq.setDetail(TextUtils.isEmpty(tempBean.getDetail()) ? "" : tempBean.getDetail());
        goodsReq.setPrice_max(tempBean.getPrice() + "");
        goodsReq.setPrice_min(tempBean.getPrice() + "");
        goodsReq.setSeason(tempBean.getSeason());
        goodsReq.setStar(tempBean.getStar() + "");
        goodsReq.setName(JsonUtils.jsonFromObject(namesList));
        goodsReq.setImage_urls(JsonUtils.jsonFromObject(files));
        goodsReq.setCount(tempBean.getCount() + "");
        goodsReq.setBuy_date(tempBean.getBuy_date());
        goodsReq.setExpire_date(tempBean.getExpire_date());
        mModel.addObjects(goodsReq, new RequestCallback<List<ObjectBean>>() {

            @Override
            public void onSuccess(List<ObjectBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addObjectsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });

    }

    @Override
    public void addMoreGoods(Context mContext, List<ObjectBean> mlist, ObjectBean tempBean, RoomFurnitureBean location) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        List<String> namesList = new ArrayList<>();
        List<String> files = new ArrayList<>();
        for (ObjectBean bean : mlist) {
            if (AddMoreGoodsActivity.ADD_GOODS_CODE == bean.get__v()) {
                continue;
            }
            namesList.add(bean.getName());
            files.add(bean.getObject_url());
        }
        AddGoodsReq goodsReq = new AddGoodsReq();
        goodsReq.setUid(App.getUser(mContext).getId());
        goodsReq.setCategory_codes(StringUtils.isEmpty(tempBean.getCategories()) ? "" : tempBean.getCategories().get(tempBean.getCategories().size() - 1).getCode());
        goodsReq.setChannel(TextUtils.isEmpty(tempBean.getChannel()) ? "" : JsonUtils.jsonFromObject(tempBean.getChannel().split(">")));
        goodsReq.setColor(TextUtils.isEmpty(tempBean.getColor()) ? "" : JsonUtils.jsonFromObject(tempBean.getColor().split("、")));
        goodsReq.setDetail(TextUtils.isEmpty(tempBean.getDetail()) ? "" : tempBean.getDetail());
        goodsReq.setPrice_max(tempBean.getPrice() + "");
        goodsReq.setPrice_min(tempBean.getPrice() + "");
        goodsReq.setSeason(tempBean.getSeason());
        goodsReq.setStar(tempBean.getStar() + "");
        goodsReq.setName(JsonUtils.jsonFromObject(namesList));
        goodsReq.setImage_urls(JsonUtils.jsonFromObject(files));
        goodsReq.setCount(tempBean.getCount() + "");
        goodsReq.setBuy_date(tempBean.getBuy_date());
        goodsReq.setExpire_date(tempBean.getExpire_date());
        if (location != null) {
            goodsReq.setLocation_codes(location.getCode());
        }
        mModel.addMoreGoods(goodsReq, new RequestCallback<List<ObjectBean>>() {

            @Override
            public void onSuccess(List<ObjectBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addMoreGoodsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void getBookInfo(String uid, String isbn) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddGoodsReq req = new AddGoodsReq();
        req.setUid(uid);
        req.setISBN(isbn);
        mModel.getBookInfo(req, new RequestCallback<BookBean>() {

            @Override
            public void onSuccess(BookBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getBookInfoSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void getTaobaoInfo(String uid, String key) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddGoodsReq req = new AddGoodsReq();
        req.setUid(uid);
        req.setKey(key);
        mModel.getTaobaoInfo(req, new RequestCallback<BookBean>() {

            @Override
            public void onSuccess(BookBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getTaobaoInfoSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void removeObjectFromFurnitrue(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        req.setCode(code);
        mModel.removeObjectFromFurnitrue(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().removeObjectFromFurnitrueSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    public void openCamare(Context mContext) {
        String sdPath = App.CACHEPATH;
        File file = new File(sdPath);
        if (!file.exists()) {
            file.mkdir();
        }
        mOutputFile = new File(sdPath, System.currentTimeMillis() + ".jpg");
        PermissionX.init((BaseActivity) mContext).permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).
                onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                }).onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
            }
        }).request(new com.permissionx.guolindev.callback.RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted) {
                    Intent newIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    newIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
                    ((BaseActivity) mContext).startActivityForResult(newIntent, REQUST_CAMARE);
                } else {
                    Toast.makeText(mContext, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void downloadImg(Context mContext, BookBean book) {
        if (book == null || TextUtils.isEmpty(book.getPic())) {
            Toast.makeText(mContext, "该物品不支持扫码", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {//下载图片
            @Override
            public void run() {
                try {
                    File file = Glide.with(mContext).load(book.getPic()).downloadOnly(500, 500).get();
                    String newPath = App.CACHEPATH + System.currentTimeMillis() + ".jpg";
                    FileUtil.copyFile(file, newPath);
                    file = new File(newPath);
                    book.setImageFile(file);
                    ((Activity) mContext).runOnUiThread(new Runnable() {//回主线程
                        @Override
                        public void run() {
                            DialogUtil.show("提醒", "此操作会将部分共同属性刷新，是否继续?", "继续", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (getUIView() != null) {
                                        getUIView().updateBeanWithBook(book);
                                    }

                                }
                            }, null);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onActivityResult(Context mContext, int requestCode, int resultCode, Intent data) {
        //拍照的原照片
        if (requestCode == REQUST_CAMARE) {
            Uri uri = Uri.fromFile(mOutputFile);
            if (mOutputFile.length() > 0 && uri != null) {
                File imgOldFile = new File(mOutputFile.getAbsolutePath());
                //原照片
                if (getUIView() != null) {
                    getUIView().getCamareImg(imgOldFile);
                }
                startCropBitmap(mContext, imgOldFile);
            }
        }
        if (requestCode == REQUST_PHOTOSELECT && resultCode == ImageGridActivity.RESULT) {
            List<ImageData> temp = (ArrayList<ImageData>) data.getSerializableExtra("list");
            if (getUIView() != null) {
                getUIView().getSelectPhotoImg(new File(temp.get(AppConstant.DEFAULT_INDEX_OF).getBigUri()));
            }
        }
        //裁剪界面返回的照片
        if (requestCode == UCrop.REQUEST_CROP) {
            if (getUIView() != null) {
                getUIView().getCropBitmap(mOutputFile);
            }
        }
        //扫码
        if (requestCode == AddGoodsActivity.BOOK_CODE && resultCode == CaptureActivity.result) {//扫描的到结果
            String code = data.getStringExtra("result");
            if (code.contains("http")) {
                //网络商城
                getTaobaoInfo(App.getUser(mContext).getId(), code);
            } else {
                //书本
                getBookInfo(App.getUser(mContext).getId(), code);
            }
        }
    }

    public void startCropBitmap(Context mContext, File imgOldFile) {
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

    public void startImageGridActivity(Context mContext) {
        Intent i = new Intent(mContext, ImageGridActivity.class);
        i.putExtra("max", AppConstant.SELECT_PHOTO_COUNT);
        ((Activity) mContext).startActivityForResult(i, REQUST_PHOTOSELECT);
    }

    public void setLocation(ObjectBean objectBean, RoomFurnitureBean location) {
        Collections.sort(location.getParents(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        List<BaseBean> beanList = new ArrayList<>();
        beanList.addAll(location.getParents());
        BaseBean baseBean = new BaseBean();
        baseBean.setLevel(location.getLevel());
        baseBean.setCode(location.getCode());
        beanList.add(baseBean);
        objectBean.setLocations(beanList);
    }

}
