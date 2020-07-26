package com.gongwu.wherecollect.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

/**
 * Function:七牛云资源上传
 * Date: 2017/11/7
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class QiNiuUploadUtil {
    private static final String TAG = "QiNiuUploadUtil";

    private Context context;
    private UploadManager uploadManager;

    private QiNiuUploadUtil() {
    }

    private static QiNiuUploadUtil mUploadUtil;

    public static QiNiuUploadUtil getInstance(Context context) {
        if (mUploadUtil == null) {
            synchronized (QiNiuUploadUtil.class) {
                if (mUploadUtil == null) {
                    mUploadUtil = new QiNiuUploadUtil(context);
                }
            }
        }
        return mUploadUtil;
    }


    public QiNiuUploadUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public void start(String path, File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String key = String.format("%s-%s%s", path, System.currentTimeMillis(), file.getName());
                Map<String, String> map = new TreeMap<>();
                map.put("uid", App.getUser(context).getId());
                map.put("key", key);
                ApiUtils.getQiniuToken(App.getUser(context).getId(), key, new ApiCallBack<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> map) {
                        String token = (String) map.get("token");
                        upLoad(key, token, file);
                    }

                    @Override
                    public void onFailed(String msg) {
                        if (listener != null) {
                            listener.onUpLoadError(msg);
                        }
                    }
                });
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String url = (String) msg.obj;
                    if (listener != null) {
                        listener.onUpLoadSuccess(url);
                    }
                    break;
                case 2:
                    if (listener != null) {
                        listener.onUpLoadError("load error");
                    }
                    break;
            }
        }
    };

    private void upLoad(String key, String token, File file) {
        uploadManager = new UploadManager();
        uploadManager.put(file, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            //                                {"hash":"FlY2_jV4gKjYHz6fkDQ9qLSr67rb",
                            // "key":"urser-15100554679801510055460290.jpg"}
                            //http://7xroa4.com1.z0.glb.clouddn.com/
                            //http://cdn.shouner.com/
                            try {
                                String url = "http://cdn.shouner.com/" + res.getString("key");
                                handler.obtainMessage(1, url).sendToTarget();
                                FileUtil.deleteFolderFile(file.getAbsolutePath());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(2);
                            }
                        } else {
                            handler.sendEmptyMessage(2);
                            Lg.getInstance().e(TAG, "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Lg.getInstance().e(TAG, key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

    private UpLoadListener listener;

    public void setUpLoadListener(UpLoadListener listener) {
        this.listener = listener;
    }

    public interface UpLoadListener {

        void onUpLoadSuccess(String path);

        void onUpLoadError(String msg);
    }
}
