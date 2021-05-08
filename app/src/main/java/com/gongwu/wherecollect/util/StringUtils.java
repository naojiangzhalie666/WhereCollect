package com.gongwu.wherecollect.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.GoodsInfoBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.view.PopupMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static SimpleDateFormat dateFormat = null;

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
    }
    // /*** 获取URL ***/
    // public static String getHttpsUrl(String function,String key[], String
    // value[]) {
    // String url = "";
    // StringBuffer sb = new StringBuffer();
    // for (int i = 0; i < value.length; i++) {
    // sb.append(key[i] + "=");
    // LogUtils.e("参数", key[i] + "=" + value[i]);
    // try {
    // sb.append(value[i]);
    // LogUtils.e("参数",value[i]);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // sb.append("&");
    // }
    // sb.delete(sb.length() - 1, sb.length());
    // url = My +
    // return url;
    // }

    public static final int TYPE_GOODS_COUNT = 0;//数量
    public static final int TYPE_GOODS_STAR = 1;//评级
    public static final int TYPE_GOODS_SEASON = 2;//季节
    public static final int TYPE_GOODS_COLOR = 3;//颜色
    public static final int TYPE_GOODS_PRICE = 4;//价格
    public static final int TYPE_GOODS_CHANNEL = 5;//渠道
    public static final int TYPE_GOODS_PURCHASE_TIME = 6;//购买时间
    public static final int TYPE_GOODS_EXPIRY_TIME = 7;//到期时间
    public static final int TYPE_GOODS_CLASSIFY = 8;//分类
    public static final int TYPE_GOODS_NOTE = 9;//备注

    public static String getGoodsTypeString(int type) {
        switch (type) {
            case TYPE_GOODS_COUNT:
                return "数量";
            case TYPE_GOODS_STAR:
                return "重要程度";
            case TYPE_GOODS_SEASON:
                return "季节";
            case TYPE_GOODS_COLOR:
                return "颜色";
            case TYPE_GOODS_PRICE:
                return "价格";
            case TYPE_GOODS_CHANNEL:
                return "购货渠道";
            case TYPE_GOODS_PURCHASE_TIME:
                return "购买时间";
            case TYPE_GOODS_EXPIRY_TIME:
                return "过期时间";
            case TYPE_GOODS_CLASSIFY:
                return "子分类";
            case TYPE_GOODS_NOTE:
                return "备注";
        }
        return "";
    }

    public static List<GoodsInfoBean> getGoodsInfos(ObjectBean bean) {
        List<GoodsInfoBean> mList = new ArrayList<>();
        //数量
        if (bean.getCount() > 0) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_COUNT);
            infoBean.setValue(String.valueOf(bean.getCount()));
            mList.add(infoBean);
        }
        //评级
        if (bean.getStar() > 0) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_STAR);
            infoBean.setValue(String.valueOf(bean.getStar()));
            mList.add(infoBean);
        }
        //季节
        if (!TextUtils.isEmpty(bean.getSeason())) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_SEASON);
            infoBean.setValue(bean.getSeason());
            mList.add(infoBean);
        }
        //颜色
        if (!TextUtils.isEmpty(bean.getColorStr())) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_COLOR);
            infoBean.setValue(bean.getColorStr());
            mList.add(infoBean);
        }
        //价格
        if (!TextUtils.isEmpty(bean.getPrice())) {
            if (!bean.getPrice().equals("0") && !bean.getPrice().equals("0.0")) {
                GoodsInfoBean infoBean = new GoodsInfoBean();
                infoBean.setType(StringUtils.TYPE_GOODS_PRICE);
                infoBean.setValue(bean.getPrice());
                mList.add(infoBean);
            }
        }
        //渠道
        if (!TextUtils.isEmpty(bean.getChannelStr())) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_CHANNEL);
            infoBean.setValue(bean.getChannelStr());
            mList.add(infoBean);
        }
        //购买时间
        if (!TextUtils.isEmpty(bean.getBuy_date())) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_PURCHASE_TIME);
            infoBean.setValue(bean.getBuy_date());
            mList.add(infoBean);
        }
        //到期时间
        if (!TextUtils.isEmpty(bean.getExpire_date())) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_EXPIRY_TIME);
            infoBean.setValue(bean.getExpire_date());
            mList.add(infoBean);
        }
        //子分类
        if (bean.getCategories() != null && bean.getCategories().size() > 1) {
            Collections.sort(bean.getCategories(), new Comparator<BaseBean>() {
                @Override
                public int compare(BaseBean lhs, BaseBean rhs) {
                    return lhs.getLevel() - rhs.getLevel();
                }
            });
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_CLASSIFY);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.getCategories().size(); i++) {
                if (i == AppConstant.DEFAULT_INDEX_OF) {
                    continue;
                }
                sb.append(bean.getCategories().get(i).getName()).append("/");
            }
            sb.delete(sb.length() - 1, sb.length());
            infoBean.setValue(sb.toString());
            mList.add(infoBean);
        }
        //备注
        if (!TextUtils.isEmpty(bean.getDetail())) {
            GoodsInfoBean infoBean = new GoodsInfoBean();
            infoBean.setType(StringUtils.TYPE_GOODS_NOTE);
            infoBean.setValue(bean.getDetail());
            mList.add(infoBean);
        }
        return mList;
    }

    public static String toString(Context context, int stringId, Object... args) {
        return String.format(context.getString(stringId), args);
    }

    /**
     * @创建时间：Feb 17, 2013 9:23:14 PM
     * @方法描述：（对象为空字符或null或集合长度为0）
     * @返回值：true/false
     * @version
     */
    public static boolean isNullOrBlanK(Object param) {
        if (param == null) {
            return true;
        } else {
            if (String.class.isInstance(param)) {
                if (!"".equals(((String) param).trim())) {
                    return false;
                }
            } else if (List.class.isInstance(param)) {
                if (((ArrayList) param).size() != 0) {
                    return false;
                }
            } else if (java.util.Map.class.isInstance(param)) {
                if (((HashMap) param).size() != 0) {
                    return false;
                }
            } else if (String[].class.isInstance(param)) {
                if (((Object[]) param).length != 0) {
                    return false;
                }
            } else {
                return false;
            }
            return true;
        }
    }

    /****
     * 是否是有效电话
     ****/
    public static boolean isPhone(String name) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /***
     * 是否是有效Email
     ***/
    public static boolean isEmail(String email) {
        boolean flag = true;
        if (email.indexOf("@") == -1 || email.indexOf(".") == -1) {
            flag = false;
        }
        if (flag) {
            if (email.indexOf("@") > email.indexOf("."))
                flag = false;
        }
        return flag;
    }

    /**
     * 是否是有效密码
     ***/
    public static boolean isGoodPWD(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否是有效日期
     ***/
    public static boolean isGoodDate(String s) {
        try {
            dateFormat.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getCurrentVersion(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    public static String getCurrentVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 返回首字母
     *
     * @param strChinese
     * @return
     */
    public static String getPYIndexStr(String strChinese) {
        if (strChinese.equals("阚")) {
            return "K";
        }
        try {
            StringBuffer buffer = new StringBuffer();
            byte b[] = strChinese.getBytes("GBK");// 把中文转化成byte数组
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 255) > 128) {
                    int char1 = b[i++] & 255;
                    char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n
                    // 位，就相当于乘上2的n次方
                    int chart = char1 + (b[i] & 255);
                    buffer.append(getPYIndexChar((char) chart));
                    continue;
                }
                char c = (char) b[i];
                if (!Character.isJavaIdentifierPart(c))// 确定指定字符是否可以是 Java
                    // 标识符中首字符以外的部分。
                    c = 'A';
                buffer.append(Character.toUpperCase(c));
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println((new StringBuilder())
                    .append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519")
                    .append(e.getMessage()).toString());
        }
        return null;
    }


    /**
     * 得到首字母
     *
     * @param strChinese 文字
     */
    private static char getPYIndexChar(char strChinese) {
        int charGBK = strChinese;
        char result;
        if (charGBK >= 45217 && charGBK <= 45252)
            result = 'A';
        else if (charGBK >= 45253 && charGBK <= 45760)
            result = 'B';
        else if (charGBK >= 45761 && charGBK <= 46317)
            result = 'C';
        else if (charGBK >= 46318 && charGBK <= 46825)
            result = 'D';
        else if (charGBK >= 46826 && charGBK <= 47009)
            result = 'E';
        else if (charGBK >= 47010 && charGBK <= 47296)
            result = 'F';
        else if (charGBK >= 47297 && charGBK <= 47613)
            result = 'G';
        else if (charGBK >= 47614 && charGBK <= 48118)
            result = 'H';
        else if (charGBK >= 48119 && charGBK <= 49061)
            result = 'J';
        else if (charGBK >= 49062 && charGBK <= 49323)
            result = 'K';
        else if (charGBK >= 49324 && charGBK <= 49895)
            result = 'L';
        else if (charGBK >= 49896 && charGBK <= 50370)
            result = 'M';
        else if (charGBK >= 50371 && charGBK <= 50613)
            result = 'N';
        else if (charGBK >= 50614 && charGBK <= 50621)
            result = 'O';
        else if (charGBK >= 50622 && charGBK <= 50905)
            result = 'P';
        else if (charGBK >= 50906 && charGBK <= 51386)
            result = 'Q';
        else if (charGBK >= 51387 && charGBK <= 51445)
            result = 'R';
        else if (charGBK >= 51446 && charGBK <= 52217)
            result = 'S';
        else if (charGBK >= 52218 && charGBK <= 52697)
            result = 'T';
        else if (charGBK >= 52698 && charGBK <= 52979)
            result = 'W';
        else if (charGBK >= 52980 && charGBK <= 53688)
            result = 'X';
        else if (charGBK >= 53689 && charGBK <= 54480)
            result = 'Y';
        else if (charGBK >= 54481 && charGBK <= 55289)
            result = 'Z';
        else
            result = (char) (65 + (new Random()).nextInt(25));
        result = Character.toUpperCase(result);
        return result;
    }

    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     * @param context
     * @return 图片的绝对地址
     */
    public static String getPath(Activity context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, proj, null, null, null);
        cursor.moveToFirst();
        int actual_image_column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        return cursor.getString(actual_image_column_index);
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isGoodName(char c) {
        if (isChinese(c)) {
            return true;
        } else if (Character.isLowerCase(c) || Character.isUpperCase(c)) {
            return true;
        }
        return false;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
            return true;
        }
        return false;
    }

    /**
     * @param i
     * @return 输入1返回01
     */
    public static String formatIntTime(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }

    /**
     * 习题详情，替换url字符串
     *
     * @param str
     * @param list
     * @return
     */
    public static String replaceImgUrl(String str, List<String> list) {
        if (list == null || list.size() == 0) {
            return str;
        }
        for (int i = 0; i < list.size(); i++) {
            if (str.contains("/$img$/")) {
                String s;
                int indexOf = str.indexOf("/$img$/");
                String ss = str.substring(0, indexOf);
                s = ss + "<img src=" + "\"" + list.get(i).replace("\\", "/") + "\">";
                str = s + str.substring(indexOf + 7, str.length());
            }
        }
        return str;
    }

    /**
     * @param list
     * @return
     */
    public static int getListSize(List list) {
        return list == null ? 0 : list.size();
    }

    /**
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null ? true : list.isEmpty();
    }

    /**
     * 是否能在线打开的文件
     *
     * @param url
     * @return
     */
    public static boolean isOpen(String url) {
        url = url.toLowerCase();
        if (url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".bmp") || url.endsWith(".jpeg") || url
                .endsWith(".png") || url.endsWith(".html") || url.endsWith(".flv")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是PAD还是手机
     *
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断键盘是否打开
     *
     * @param context
     * @return
     */
    public static boolean isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom > 200;
    }


    public static int convertDipToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getResId(int position) {
        int i;
        if (position > 9) {
            i = position % 10;
        } else {
            i = position;
        }
        switch (i) {
            case 0:
                return R.color.goods_color_0;
            case 1:
                return R.color.goods_color_1;
            case 2:
                return R.color.goods_color_2;
            case 3:
                return R.color.goods_color_3;
            case 4:
                return R.color.goods_color_4;
            case 5:
                return R.color.goods_color_5;
            case 6:
                return R.color.goods_color_6;
            case 7:
                return R.color.goods_color_7;
            case 8:
                return R.color.goods_color_8;
            case 9:
                return R.color.goods_color_9;
            default:
                return R.color.goods_color_0;
        }
    }

    public static String getResCode(int position) {
        int i;
        if (position > 9) {
            i = position % 10;
        } else {
            i = position;
        }
        switch (i) {
            case 0:
                return "#B5B5B5";
            case 1:
                return "#9076F2";
            case 2:
                return "#F19EC2";
            case 3:
                return "#13B5B1";
            case 4:
                return "#E66868";
            case 5:
                return "#F29B76";
            case 6:
                return "#AFC4D5";
            case 7:
                return "#32B16C";
            case 8:
                return "#13B5B1";
            case 9:
                return "#7ECEF4";
            default:
                return "#35BFBB";
        }
    }

    /**
     * 拼接位置
     *
     * @return
     */
    public static String getGoodsLoction(ObjectBean bean) {
        if (StringUtils.isEmpty(bean.getLocations())) {
            return "未归位";
        }
        Collections.sort(bean.getLocations(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.getListSize(bean.getLocations()); i++) {
            sb.append(bean.getLocations().get(i).getName());
            if (i != bean.getLocations().size() - 1) {
                sb.append("/");
            }
        }
        return sb.length() == 0 ? "未归位" : sb.toString();
    }

    public static String getGoodsLoction(RoomFurnitureBean bean) {
        if (StringUtils.isEmpty(bean.getParents())) {
            return "未归位";
        }
        Collections.sort(bean.getParents(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.getListSize(bean.getParents()); i++) {
            sb.append(bean.getParents().get(i).getName()).append("/");
        }
        sb.append(bean.getName());
        return sb.length() == 0 ? "未归位" : sb.toString();
    }

    /**
     * 拼接分类
     *
     * @return
     */
    public static String getGoodsClassify(ObjectBean bean) {
        if (StringUtils.isEmpty(bean.getCategories())) {
            return "";
        }
        Collections.sort(bean.getCategories(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.getListSize(bean.getCategories()); i++) {
            sb.append(bean.getCategories().get(i).getName());
            if (i != bean.getCategories().size() - 1) {
                sb.append("/");
            }
        }
        return sb.length() == 0 ? "" : sb.toString();
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 获取状态栏高度
     * 该方法获取需要在onWindowFocusChanged方法回调之后
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = getStatusBarByResId(context);
        if (statusBarHeight <= 0) {
            statusBarHeight = getStatusBarByReflex(context);
        }
        return statusBarHeight;
    }

    /**
     * 通过状态栏资源id来获取状态栏高度
     *
     * @param context
     * @return
     */
    private static int getStatusBarByResId(Context context) {
        int height = 0;
        //获取状态栏资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            try {
                height = context.getResources().getDimensionPixelSize(resourceId);
            } catch (Exception e) {
            }
        }
        return height;
    }

    /**
     * 通过Activity的内容距离顶部高度来获取状态栏高度，该方法获取需要在onWindowFocusChanged回调之后
     *
     * @param activity
     * @return
     */
    public static int getStatusBarByTop(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 通过反射获取状态栏高度
     *
     * @param context
     * @return
     */
    private static int getStatusBarByReflex(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断微信是否安装
     *
     * @param context
     * @return true 已安装   false 未安装
     */
    public static boolean isWxAppInstalled(Context context) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(AppConstant.WX_APP_ID);
        boolean bIsWXAppInstalled = false;
        bIsWXAppInstalled = wxApi.isWXAppInstalled();
        return bIsWXAppInstalled;
    }

    public static boolean isTencent(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String value = (String) applicationInfo.metaData.getString("UMENG_CHANNEL");
            if ("tencent".equals(value)) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showMessage(Context mContext, int StringId) {
        PopupMessage popup = new PopupMessage(mContext);
        popup.setPopupGravity(Gravity.CENTER);
        popup.showPopupWindow();
        popup.initData(mContext.getString(StringId));
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
            if (f.length() <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
