package com.fit.we.library.extend.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.bean.Route;
import com.fit.we.library.container.FitContainerActivity;
import com.fit.we.library.container.FitContainerFragment;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.UiUtil;
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
        Context context = mWXSDKInstance.getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                activity.onBackPressed();
            }
        }
    }

    /**
     * 重载页面
     */
    @JSMethod(uiThread = true)
    public void reload(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            container.refresh();
            successCallback.invoke(null);
        }
    }

}
