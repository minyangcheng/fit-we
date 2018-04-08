package com.fit.we.library.util;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

public class RuntimeUtil {

    /**
     * 获取manifest中配置的metaData信息
     * <meta-data
     * android:name="key"
     * android:value="value" />
     *
     * @param ctx
     * @param key
     * @return
     */
    public static String getMetaData(Context ctx, String key) {
        String packageName = ctx.getPackageName();
        PackageManager packageManager = ctx.getPackageManager();
        Bundle bd;
        String value = "";
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            bd = info.metaData;//获取metaData标签内容
            if (bd != null) {
                Object keyO = bd.get(key);
                if (keyO != null) {
                    value = keyO.toString();//这里获取的就是value值
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断某个应用在系统是否有安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean existAppInSystem(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo == null ? false : true;
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    /**
     * 获取一个打开app的Intent
     * 如果app所有Activity已被finish则重新打开APP
     * 如果app在后台运行则返回前台
     *
     * @return
     */
    public static Intent getLaunchAppIntent(Context context) {
        String pkg = context.getPackageName();
        String mainClass = context.getPackageManager().getLaunchIntentForPackage(pkg).getComponent().getClassName();
        return getLaunchAppIntent(context, pkg, mainClass);
    }

    /**
     * 获取一个打开app的Intent
     * 如果app所有Activity已被finish则重新打开APP
     * 如果app在后台运行则返回前台
     *
     * @param context
     * @param packageName 包名
     * @param className   指定打开的类名，若为空则打开主界面
     * @return
     */
    public static Intent getLaunchAppIntent(Context context, String packageName, String className) {
        Intent intent;
        if (!TextUtils.isEmpty(className)) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
        } else {
            intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        }
        //关键的一步，设置启动模式,否则会重复打开Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    /**
     * 程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String curPackageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> app = am.getRunningAppProcesses();
        if (app == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo a : app) {
            if (a.processName.equals(curPackageName) &&
                    a.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getPackageName(Context context) {
        return getPackageInfo(context).packageName;
    }

    /**
     * 获取app版本号
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    /**
     * 获取app签名md5值
     */
    public static String getSignMd5(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return Md5Util.encryptionMD5(sign.toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 复制到剪贴板
     */
    public static void clipboard(Context context, String text) {
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText("simple text", text));
    }

    /**
     * 去掉通知图标
     *
     * @param id
     * @param type
     */
    public static void cancelNotify(String id, String type, Context context) {
        NotificationManager manger = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            manger.cancelAll();
        } else {
            int notifyid = 1;
            try {
                notifyid = Integer.parseInt(id) + Integer.parseInt(type);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            manger.cancel(notifyid);
        }
    }

    /**
     * 去掉所有通知图标
     */
    public static void cancelAllNotify(Context context) {
        cancelNotify(null, null, context);
    }

}
