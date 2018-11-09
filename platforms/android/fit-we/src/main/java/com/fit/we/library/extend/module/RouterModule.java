package com.fit.we.library.extend.module;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.bean.Route;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.ui.FitContainerActivity;
import com.fit.we.library.extend.weex.WeexHandlerManager;
import com.fit.we.library.util.FitUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class RouterModule extends WXModule {

    /**
     * 打开新的weex页面
     */
    @JSMethod(uiThread = true)
    public void open(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        Route routeInfo = JSON.parseObject(params.toJSONString(), Route.class);
        FitContainerActivity.startActivity(mWXSDKInstance.getContext(), routeInfo);
        successCallback.invoke(null);
    }

    /**
     * 打开原生页面
     */
    @JSMethod(uiThread = true)
    public void openLocal(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        try {
            String activityName = params.getString("className");
            String jsonStr = params.getString("data");
            Class clz = Class.forName(activityName);
            Intent intent = new Intent(mWXSDKInstance.getContext(), clz);
            intent.putExtra("from", "weex");
            if (!TextUtils.isEmpty(jsonStr)) {
                if (jsonStr.startsWith("[")) {
                    FitUtil.putIntentExtra(params.getJSONArray("data"), intent);
                } else if (jsonStr.startsWith("{")) {
                    FitUtil.putIntentExtra(params.getJSONObject("data"), intent);
                }
            }
            mWXSDKInstance.getContext().startActivity(intent);
            successCallback.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e.getMessage());
        }
    }

    /**
     * 关闭当前Activity
     */
    @JSMethod(uiThread = true)
    public void close(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        Activity activity = (Activity) mWXSDKInstance.getContext();
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 重载页面
     */
    @JSMethod(uiThread = true)
    public void reload(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.refresh();
            successCallback.invoke(null);
        }
    }

}
