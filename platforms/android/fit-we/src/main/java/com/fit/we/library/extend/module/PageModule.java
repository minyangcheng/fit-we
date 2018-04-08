package com.fit.we.library.extend.module;

import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.bean.FitEvent;
import com.fit.we.library.bean.RouteInfo;
import com.fit.we.library.container.FitContainerActivity;
import com.fit.we.library.container.FitContainerFragment;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.UiUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class PageModule extends WXModule {

    /**
     * 打开新的H5页面
     */
    @JSMethod(uiThread = true)
    public void open(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            RouteInfo routeInfo = JSON.parseObject(params.toJSONString(), RouteInfo.class);
            FitContainerActivity.startActivity(container.getActivity(), routeInfo);
            successCallback.invoke(null);
        }
    }

    /**
     * 打开原生页面
     */
    @JSMethod(uiThread = true)
    public void openLocal(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            try {
                String activityName = params.getString("className");
                String jsonStr = params.getString("data");
                Class clz = Class.forName(activityName);
                Intent intent = new Intent(container.getActivity(), clz);
                intent.putExtra("from", "weex");
                if (!TextUtils.isEmpty(jsonStr)) {
                    if (jsonStr.startsWith("[")) {
                        FitUtil.putIntentExtra(params.getJSONArray("data"), intent);
                    } else if (jsonStr.startsWith("{")) {
                        FitUtil.putIntentExtra(params.getJSONObject("data"), intent);
                    }
                }
                container.getActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                errorCallback.invoke(e.getMessage());
            }
        }
    }

    /**
     * 关闭当前Activity
     */
    @JSMethod(uiThread = true)
    public void close(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerActivity container = UiUtil.getContainerActivity(mWXSDKInstance);
        if (container != null) {
            container.finish();
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
