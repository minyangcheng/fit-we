package com.fit.we.library.extend.weex;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;

public class LongCallbackHandler {

    public static String OnClickNbBack = "OnClickNbBack";//导航栏返回按钮
    public static String OnClickNbLeft = "OnClickNbLeft";//导航栏左侧按钮(非返回按钮)
    public static String OnClickNbTitle = "OnClickNbTitle";//导航栏标题按钮
    public static String OnClickNbRight = "OnClickNbRight";//导航栏最右侧按钮
    public static String OnClickSysBack = "OnClickSysBack";//系统返回按钮（物理返回键）

    private HashMap<String, JSCallback> callBackMap = new HashMap<>();

    public LongCallbackHandler() {
    }

    public void onClickNbBack() {
        call(OnClickNbBack, null);
    }

    public void onSysClickBack() {
        call(OnClickSysBack, null);
    }

    public void onClickNbLeft() {
        call(OnClickNbLeft, null);
    }

    public void onClickNbTitle(int which) {
        JSONObject object = new JSONObject();
        object.put("which", which);
        call(OnClickNbTitle, object);
    }

    public void onClickNbRight(int which) {
        JSONObject object = new JSONObject();
        object.put("which", which);
        call(OnClickNbRight + which, object);
    }

    private void call(String key, JSONObject object) {
        JSCallback jsCallback = callBackMap.get(key);
        if (jsCallback != null) {
            jsCallback.invokeAndKeepAlive(object);
        }
    }

    public void addJSCallback(String key, JSCallback jsCallback) {
        callBackMap.put(key, jsCallback);
    }

    public void removeJSCallback(String key) {
        callBackMap.remove(key);
    }

    public boolean hasJSCallback(String key) {
        return callBackMap.containsKey(key);
    }

}
