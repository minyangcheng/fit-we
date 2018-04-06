package com.min.hybrid.library.util;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.min.fit.weex.R;

import java.util.List;
import java.util.Map;

/**
 * Created by minyangcheng on 2018/1/10.
 */

public class FitUtil {

    public static Handler handler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static String format(String formatStr, String defaultStr) {
        return TextUtils.isEmpty(formatStr) ? defaultStr : formatStr;
    }

    public static String format(String formatStr) {
        return format(formatStr, "");
    }

    public static int formatInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public static long formatLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public static double formatDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public static boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 0;
        }
        version1 = version1.toLowerCase().replaceAll("v", "");
        version2 = version2.toLowerCase().replaceAll("v", "");
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static String joinMapToUrl(String url, Map<String, Object> map) {
        if (url == null || map == null) {
            return url;
        }
        if (map.size() > 0) {
            url += "?";
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            url = url + entry.getKey() + "=" + entry.getValue() + "&";
        }
        if (map.size() > 0) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static Animator alpha(View view, int from, int to, int duration) {
        Animator animator = ObjectAnimator.ofFloat(view, "alpha", from, to);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    public static Intent putIntentExtra(JSONObject jsonObject, Intent intent) {
        if (jsonObject == null || intent == null) {
            return null;
        }
        String key = null;
        Object value = null;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value instanceof Boolean) {
                intent.putExtra(key, (boolean) value);
            } else if (value instanceof String) {
                intent.putExtra(key, value.toString());
            } else if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Byte) {
                intent.putExtra(key, (Byte) value);
            } else if (value instanceof Short) {
                intent.putExtra(key, (Short) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else {
                intent.putExtra(key, value.toString());
            }
        }
        return intent;
    }

    public static Intent putIntentExtra(JSONArray jsonArray, Intent intent) {
        if (jsonArray == null || intent == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            putIntentExtra((JSONObject) jsonArray.get(i), intent);
        }
        return intent;
    }

    /**
     * 获取主题颜色
     */
    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}
