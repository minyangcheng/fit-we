package com.fit.we.library.extend.weex;

import com.taobao.weex.WXSDKInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minych on 18-11-9.
 */

public class WeexHandlerManager {

    private static Map<WXSDKInstance, IWeexHandler> weexHandlerMap = new HashMap<>();

    public static void add(WXSDKInstance wxsdkInstance, IWeexHandler weexHandler) {
        if (wxsdkInstance == null || weexHandler == null) {
            return;
        }
        weexHandlerMap.put(wxsdkInstance, weexHandler);
    }

    public static void remove(WXSDKInstance wxsdkInstance) {
        if (wxsdkInstance == null) {
            return;
        }
        weexHandlerMap.remove(wxsdkInstance);
    }

    public static IWeexHandler getWeexHandler(WXSDKInstance wxsdkInstance) {
        if (wxsdkInstance == null) {
            return null;
        }
        return weexHandlerMap.get(wxsdkInstance);
    }

}
