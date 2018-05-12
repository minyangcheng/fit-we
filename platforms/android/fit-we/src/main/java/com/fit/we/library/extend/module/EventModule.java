package com.fit.we.library.extend.module;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.bean.FitEvent;
import com.fit.we.library.container.FitContainerFragment;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.UiUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class EventModule extends WXModule {

    /**
     * 发送通知
     */
    @JSMethod(uiThread = true)
    public void postEvent(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            FitEvent event = new FitEvent();
            event.data = params.getJSONObject("data");
            event.type = params.getString("type");
            EventUtil.post(event);
            successCallback.invoke(null);
        }
    }

}
