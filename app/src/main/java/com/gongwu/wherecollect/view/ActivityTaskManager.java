package com.gongwu.wherecollect.view;

import android.app.Activity;

import com.gongwu.wherecollect.util.Lg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * author: Zhang Shuliang <br/>
 * email : shuliangzhang@ecarx.com.cn <br/>
 * date  : 2018/01/05 <br/>
 * desc  :
 * </pre>
 */
public class ActivityTaskManager {
    private static final String TAG = "ActivityTaskManager";
    private static ActivityTaskManager sInstance;

    private HashMap<String, Activity> mActivityMap;

    public static synchronized ActivityTaskManager getInstance() {
        if (sInstance == null) {
            synchronized (ActivityTaskManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityTaskManager();
                }
            }
        }
        return sInstance;
    }

    private ActivityTaskManager() {
        mActivityMap = new HashMap<>();
    }

    /**
     * 添加Activity对象
     *
     * @param activity
     */
    public void putActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Lg.getInstance().d(TAG, "putActivity name = " + activity.getClass().getName());
        mActivityMap.put(activity.getClass().getName() + "_" + activity, activity);
    }

    /**
     * 移除Activity对象
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        Lg.getInstance().d(TAG, "removeActivity name = " + activity.getClass().getName());
        mActivityMap.remove(activity.getClass().getName() + "_" + activity);
    }

    public boolean lookupActivity(Activity activity) {
        return mActivityMap.containsKey(activity.getClass().getName());
    }

    /**
     * 关闭所有Activity
     */
    public void finishAllActivity() {
        Lg.getInstance().d(TAG, "finishAllActivity mActivityMap = " + mActivityMap);
        Set<String> activityNames = mActivityMap.keySet();
        for (String string : activityNames) {
            finishActivity(mActivityMap.get(string));
        }
        mActivityMap.clear();
    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭所有Activity除了某个Activity
     *
     * @param activity
     */
    public void finishAllActivityExcept(Activity activity) {
        String exceptName = activity.getClass().getName() + "_" + activity;
        Lg.getInstance().d(TAG, "finishAllActivityExcept mActivityMap = " + mActivityMap + ", except = " + exceptName);
        Set<String> activityNames = new HashSet<>(mActivityMap.keySet());
        activityNames.remove(exceptName);
        for (String key : activityNames) {
            finishActivity(mActivityMap.get(key));
            mActivityMap.remove(key);
        }
    }

}
