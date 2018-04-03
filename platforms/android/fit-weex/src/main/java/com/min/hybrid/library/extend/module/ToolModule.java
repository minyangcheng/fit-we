package com.min.hybrid.library.extend.module;

import android.text.TextUtils;

import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.FitLog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixinke on 2017/3/3.
 */

public class ToolModule extends WXModule {

    public static final Map<String, JSCallback> longCallBack = new HashMap<>();

    @JSMethod(uiThread = true)
    public void printLog(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        FitLog.d(FitConstants.PRINT_LOG, str);
    }

    @JSMethod(uiThread = true)
    public void getLocation(Map<String, String> map, JSCallback callback, JSCallback callback_) {
        FitLog.d(FitConstants.PRINT_LOG, "map=%s , callback=%s", map == null, callback == null);
        Map<String, String> data = new HashMap<>();
        data.put("x", map.get("x"));
        data.put("y", map.get("y"));
//        callback.invoke(data);
//        callback.invoke(data);
        callback.invokeAndKeepAlive(data);
        callback.invokeAndKeepAlive(data);
        callback_.invoke("this is error");
    }

}
