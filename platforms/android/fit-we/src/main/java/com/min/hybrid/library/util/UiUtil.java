package com.min.hybrid.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.min.hybrid.library.container.FitContainerActivity;
import com.min.hybrid.library.container.FitContainerFragment;
import com.taobao.weex.WXSDKInstance;

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

    public static FitContainerFragment getContainerFragment(WXSDKInstance wxsdkInstance) {
        if (wxsdkInstance != null) {
            if(wxsdkInstance.getContext()!=null){
                FitContainerActivity activity = (FitContainerActivity) wxsdkInstance.getContext();
                return activity.getContainerFragment();
            }
        }
        return null;
    }

    public static FitContainerActivity getContainerActivity(WXSDKInstance wxsdkInstance) {
        if (wxsdkInstance != null) {
            FitContainerActivity activity = (FitContainerActivity) wxsdkInstance.getContext();
            return activity;
        }
        return null;
    }

}
