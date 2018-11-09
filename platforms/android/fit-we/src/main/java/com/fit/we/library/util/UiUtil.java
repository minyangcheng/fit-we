package com.fit.we.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by minych on 18-4-7.
 */

public class UiUtil {

    public static void toastShort(Context context, String message) {
        if (TextUtils.isEmpty(message) || context == null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message) {
        if (TextUtils.isEmpty(message) || context == null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
