package com.fit.we.library.extend.module;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConstants;
import com.fit.we.library.util.FitLog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class ToolModule extends WXModule {

    @JSMethod(uiThread = true)
    public void printLog(JSONObject jsonObject, JSCallback successCallback, JSCallback errorCallback) {
        FitLog.d(FitConstants.LOG_TAG, jsonObject.toJSONString());
    }

}