package com.min.hybrid.library.extend.module;

import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.FitLog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;

public class ToolModule extends WXModule {

    public static final Map<String, JSCallback> longCallBack = new HashMap<>();

    @JSMethod(uiThread = true)
    public void printLog(JSONObject jsonObject, JSCallback successCallback, JSCallback errorCallback) {
        FitLog.d(FitConstants.LOG_TAG, jsonObject.toJSONString());
    }

    @JSMethod(uiThread = true)
    public void getLocation(JSONObject jsonObject, JSCallback successCallback, JSCallback errorCallback) {
//        FitLog.d(FitConstants.LOG_TAG, "map=%s , callback=%s", map == null, callback == null);
//        Map<String, String> data = new HashMap<>();
//        data.put("x", map.get("x"));
//        data.put("y", map.get("y"));
//        JSONObject jsonObject = new JSONObject();
////        callback.invoke(data);
////        callback.invoke(data);
//        callback.invokeAndKeepAlive(data);
//        callback.invokeAndKeepAlive(data);
//        callback_.invoke("this is error");
    }

}