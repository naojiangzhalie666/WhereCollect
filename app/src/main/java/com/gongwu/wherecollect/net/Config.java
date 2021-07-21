package com.gongwu.wherecollect.net;


/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 8:52
 * @change
 * @class describe
 */
public class Config {
    public static final boolean IS_TEST = true;
    //网络环境
    public static final String BASE_URL = IS_TEST ? "http://www.shouner.com:9202/" : "https://www.shouner.com/";

    //    public static final String SHARE_URL = IS_TEST ? "http://std.shouner.com/popularize/" : "https://rg.shouner.com/";
    public static final String SHARE_URL = IS_TEST ? "http://std.shouner.com/popularize-energy/?user_id=" : "https://www.shouner.com/h5/popularize-energy/?user_id=";

    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";

    public static final String WEB_SERVICE_NAME = "收哪儿服务使用协议";
    public static final String WEB_SERVICE_URL = BASE_URL + "article?identifier=xy-fw";

    public static final String WEB_PRIVACY_NAME = "收哪儿隐私保护政策";
    public static final String WEB_PRIVACY_URL = BASE_URL + "article?identifier=xy-ys";

    public static final String WEB_ENERGY_NAME = "能量值帮助文档";
    public static final String WEB_ENERGY_URL = BASE_URL + "article?identifier=yd-nlz";

    public static final String WEB_COLLECTION_NAME = "领取码介绍";
    public static final String WEB_COLLECTION_URL = BASE_URL + "article?identifier=yd-lqm";

    public static final String WEB_TBBARCODE_NAME = "淘口令介绍";
    public static final String WEB_TBBARCODE_URL = BASE_URL + "article?identifier=yd-tkl";

    public static final String WEB_GUIDER_NAME = "帮助与引导";
    public static final String WEB_GUIDER_URL = IS_TEST ? "http://std.shouner.com/popularize/" : "https://rg.shouner.com/";
    //个人详情 显示小版本,发一个测试版本就加一,好让测试区别 测试版本
    public static final int VERSION = 82;

}
