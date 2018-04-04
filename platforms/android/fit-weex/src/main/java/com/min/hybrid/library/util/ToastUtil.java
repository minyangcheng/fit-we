package com.min.hybrid.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil {

    public static void toastShort(Context context, String message) {
        if (TextUtils.isEmpty(message) || context==null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message) {
        if (TextUtils.isEmpty(message) || context==null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
