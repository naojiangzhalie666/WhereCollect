package com.gongwu.wherecollect.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FileShareUtils {
    private static final String TAG = "FileShareUtils";
    //文件输出流
    private static OutputStream outputStream;
    /**
     * 维度。A4尺寸为210×297毫米或8.27×11.69英寸。
     * 在PostScript中，其尺寸四舍五入为595×842点。
     */
    private static final int A4_WIDTH = 2520 / 2; // 210 * 6
    private static final int A4_HEIGHT = 3564 / 2; // 297 * 6

    /**
     * 创建csv文件
     *
     * @param context 上下文
     * @param datas   Lists of csv data
     */
    public static void shareCsvFile(Context context, List<String> datas) {
        //应用路径：/storage/emulated/0/Android/data/你的应用包名/files/test
        File csvFile = context.getExternalFilesDir("HelloWord");
        if (!csvFile.exists()) {
            // 如果你想在已经存在的文件夹(zainar)下建立新的文件夹（database），就可以用此方法。
            // 此方法不能在不存在的文件夹下建立新的文件夹。假如想建立名字是"database"文件夹，那么它的父文件夹必须存在。
            csvFile.mkdir();
        }
        String fileName = getFileName();
        //时间戳.csv
        File file = new File(csvFile, fileName + ".csv");
        try {
            //创建一个文件夹
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "createCsvFile: " + e.getMessage());
        }
        //分享CSV
        startIntent(context, getUriForFile(context, writeDataToFile(file, datas)), fileName, 0);
    }

    /**
     * 写数据至文件夹中，创建file
     *
     * @param file     创建CSV文件对象
     * @param dataList 分享的数组
     * @return
     */
    private static File writeDataToFile(final File file, List<String> dataList) {
        //目录是否存在该文件
        if (file.exists()) {
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "writeDataToFile: " + e.getMessage());
            }
            //文件输出流
            final OutputStream putStream = outputStream;
            try { //写入Utf-8文件头
                //在utf-8编码文件中BOM在文件头部，占用三个字节，用来标示该文件属于utf-8编码，
                //现在已经有很多软件识别bom头，但是还有些不能识别bom头，比如PHP就不能识别bom头，
                //这也是用记事本编辑utf-8编码后执行就会出错的原因了
                putStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            } catch (IOException e) {
                e.printStackTrace();
            }
            //减去1的标题栏
            String[] headerArray = new String[dataList.size() - 1];
            headerArray = dataList.toArray(headerArray);
            //1、普通形式
            try {
                for (int i = 0; i < headerArray.length; i++) {
                    putStream.write(headerArray[i].getBytes());
                }
                putStream.close();//关闭流
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "writeDataToFile: " + e.getMessage());
            }
        } else {
            //无法创建CSV文件
            Log.e(TAG, "创建CSV文件失败");
        }
        return file;
    }

    /**
     * 创建Pdf
     */
    public static void sharePdfFile(Context mContext,File file) {
        //4、Intent意图-分享PDF
        startIntent(mContext, getUriForFile(mContext, file), "", 1);
    }

    /**
     * 创建Bitmap图片
     */
    public static void sharePicFile(Activity context) {
        String fileName = getFileName();
        File path = getFileUrl(context);
        saveBitmap(context, path, viewToBitmap(context), fileName);
        File file = new File(path + "/" + fileName + ".png");
        startIntent(context, getUriForFile(context, file), fileName, 2);
        Log.e(TAG, "sharePicFile: " + file.toString());
    }

    /**
     * 当前界面转化为Bitmap
     * 需要截取状态栏则将stateHeight设置为0
     */
    private static Bitmap viewToBitmap(Activity activity) {
        Bitmap bitmap;
        View view = activity.getWindow().getDecorView();
        //设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        //如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        //返回这个缓存视图
        bitmap = view.getDrawingCache();
        //获取状态栏高度（90）
        Rect frame = new Rect();
        //测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stateHeight = frame.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e(TAG, "stateHeight size：" + stateHeight);
        Log.e(TAG, "width size：" + width);
        Log.e(TAG, "height size：" + height);
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, stateHeight, width, height - stateHeight);
        return bitmap;
    }

    /**
     * 保存图片
     *
     * @param context
     * @param dir
     * @param bitmap
     * @param fileName
     */
    @SuppressLint("SdCardPath")
    public static void saveBitmap(Context context, File dir, Bitmap bitmap, String fileName) {
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, fileName + ".png");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();//空方法体，此输出流并强制写出所有缓冲的输出字节
                out.close();//关闭流
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "saveBitmap: " + e.getMessage());
        }
        //发送广播更新，扫描某个文件(文件绝对路径，必须是以 Environment.getExternalStorageDirectory() 方法的返回值开头)
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    /**
     * 分享文本
     */
    public static void shareText(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //指定包名：注意判断是否安装微信、QQ等；否则报错ActivityNotFoundException: No Activity found to handle Intent
        //微信：com.tencent.mm   QQ：com.tencent.mobileqq
        //intent.setPackage("com.tencent.mobileqq");//不指定包名则会显示所有可分享的应用
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "分享的内容");
        context.startActivity(intent);
    }

    /**
     * 当前时间戳作为分享的文件名
     */
    private static String getFileName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * 返回uri
     */
    private static Uri getUriForFile(Context context, File file) {
        //应用包名.fileProvider
        String authority = context.getPackageName().concat(".fileProvider");
        Uri fileUri = FileProvider.getUriForFile(context, authority, file);
        Log.e(TAG, "onSuccess: 文件路径：" + authority);
        Log.e(TAG, "onSuccess: 文件路径：" + fileUri.toString());
        Log.e(TAG, "onSuccess: 文件路径：" + file.toString());
        return fileUri;
    }

    /**
     * 返回文件夹
     */
    private static File getFileUrl(Context context) {
        File root = context.getFilesDir();
        File dir = new File(root, "hello/");
        if (!dir.exists()) {
            //创建失败
            if (!dir.mkdir()) {
                Log.e(TAG, "createBitmapPdf: 创建失败");
            }
        }
        return dir;
    }

    /**
     * 分享CSV文件
     * true：csv false：pdf
     */
    @SuppressLint("WrongConstant")
    private static void startIntent(Context context, Uri fileUri, String fileName, int isType) {

        Log.e(TAG, "startIntent: " + fileUri.toString());
        Log.e(TAG, "startIntent: " + fileName);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_STREAM, fileUri);
        share.putExtra(Intent.EXTRA_SUBJECT, fileName);
        String title = "分享";
        if (isType == 0) {
            share.setType("application/vnd.ms-excel");
            context.startActivity(Intent.createChooser(share, title));
        } else if (isType == 1) {
            share.setType("application/pdf");
            //管理应用程序包
            PackageManager packageManager = context.getPackageManager();
            //该组件或应用程序处于默认开启状态（其在清单指定）。
            List<ResolveInfo> list = packageManager.queryIntentActivities(share, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (null == list || list.size() == 0) {
                Toast.makeText(context, "没有找到可阅读PDF程序", Toast.LENGTH_SHORT).show();
            } else {
                context.startActivity(Intent.createChooser(share, title));
            }
        } else if (isType == 2) {
            share.setType("image/*");
            //安卓版本是否大于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.startActivity(Intent.createChooser(share, title));
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getFileUrl(context), title)));
                context.startActivity(share);
            }
        }
    }
}
