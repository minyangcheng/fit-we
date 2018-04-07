package com.min.hybrid.library.extend.module;

import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.FitLog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class ToolModule extends WXModule {

    @JSMethod(uiThread = true)
    public void printLog(JSONObject jsonObject, JSCallback successCallback, JSCallback errorCallback) {
        FitLog.d(FitConstants.LOG_TAG, jsonObject.toJSONString());
    }

}
