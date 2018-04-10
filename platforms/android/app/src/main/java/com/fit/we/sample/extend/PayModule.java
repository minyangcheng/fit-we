package com.fit.we.sample.extend;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConstants;
import com.fit.we.library.util.FitLog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class PayModule extends WXModule {

    @JSMethod(uiThread = true)
    public void payMoney(JSONObject jsonObject, JSCallback successCallback, JSCallback errorCallback) {
        FitLog.d(FitConstants.LOG_TAG, jsonObject.toJSONString());
        JSONObject result = new JSONObject();
        result.put("money", "1000");
        result.put("date", "2018-03-21 10:09");
        successCallback.invoke(result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
