package com.min.hybrid.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.min.hybrid.library.FitConstants;

public class SharePreferenceUtil {

    public static String getPageDevHostUrl(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants
                    .SP.NATIVE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(FitConstants.SP.PAGE_DEV_HOST_URL, null);
        }
        return null;
    }


    public static void setPageDevHostUrl(Context context, String pageUrl) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants
                    .SP.NATIVE_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(FitConstants.SP.PAGE_DEV_HOST_URL, pageUrl).apply();
        }
    }

    public static String getVersion(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants
                    .SP.NATIVE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(FitConstants.SP.VERSION, null);
        }
        return null;
    }


    public static void setVersion(Context context, String version) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants
                    .SP.NATIVE_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(FitConstants.SP.VERSION, version).apply();
        }
    }


    public static void setDownLoadVersion(Context context, String version) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants
                    .SP.NATIVE_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(FitConstants.SP.DOWNLOAD_VERSION, version).apply();
        }
    }

    public static String getDownLoadVersion(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants
                    .SP.NATIVE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(FitConstants.SP.DOWNLOAD_VERSION, null);
        }
        return null;
    }


    public static boolean getInterceptorActive(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants.SP.NATIVE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean(FitConstants.SP.INTERCEPTOR_ACTIVE, true);
        }
        return false;
    }

    public static void setInterceptorActive(Context context, boolean active) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FitConstants.SP.NATIVE_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(FitConstants.SP.INTERCEPTOR_ACTIVE, active).apply();
        }
    }

}
