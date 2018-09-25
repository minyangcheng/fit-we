package com.fit.we.library.extend.module;

import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.R;
import com.fit.we.library.container.FitContainerFragment;
import com.fit.we.library.util.DeviceUtil;
import com.fit.we.library.util.NavigationBarEventHandler;
import com.fit.we.library.util.UiUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class NavigatorModule extends WXModule {

    /**
     * 隐藏导航栏
     */
    @JSMethod(uiThread = true)
    public void hide(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            container.getNavigationBar().hide();
            successCallback.invoke(null);
        }
    }

    /**
     * 显示导航栏
     */
    @JSMethod(uiThread = true)
    public void show(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            container.getNavigationBar().show();
            successCallback.invoke(null);
        }
    }

    /**
     * 显示状态栏
     */
    @JSMethod(uiThread = true)
    public void showStatusBar(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            DeviceUtil.setStatusBarVisibility(container.getActivity(), true);
            successCallback.invoke(null);
        }
    }

    /**
     * 隐藏状态栏
     */
    @JSMethod(uiThread = true)
    public void hideStatusBar(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            DeviceUtil.setStatusBarVisibility(container.getActivity(), false);
            successCallback.invoke(null);
        }
    }

    /**
     * 隐藏导航栏返回按钮,只能在首页使用
     */
    @JSMethod(uiThread = true)
    public void hideBackBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            container.getNavigationBar().hideNbBack();
            successCallback.invoke(null);
        }
    }

    /**
     * 监听系统返回按钮
     */
    @JSMethod(uiThread = true)
    public void hookSysBack(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            container.getNavigationBarEventHandler().addJSCallback(NavigationBarEventHandler.OnClickSysBack, successCallback);
        }
    }

    /**
     * 监听导航栏返回按钮
     */
    @JSMethod(uiThread = true)
    public void hookBackBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            container.getNavigationBarEventHandler().addJSCallback(NavigationBarEventHandler.OnClickNbBack, successCallback);
        }
    }

    /**
     * 设置标题
     * title:   标题
     * subTitle:副标题
     * clickable：是否可点击，1：是，并且显示小箭头 其他：否
     * direction：箭头方向 top：向上 bottom：向下
     */
    @JSMethod(uiThread = true)
    public void setTitle(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            String title = params.getString("title");
            String subTitle = params.getString("subTitle");
            boolean clickable = "1".equals(params.getIntValue("clickable"));
            String direction = params.getString("direction");
            container.getNavigationBar().nbCustomTitleLayout.removeAllViews();
            container.getNavigationBar().titleParent.setVisibility(View.VISIBLE);
            container.getNavigationBar().setNbTitle(title, subTitle);
            if ("bottom".equals(direction)) {
                container.getNavigationBar().setTitleClickable(clickable, R.mipmap.img_arrow_black_down);
            } else {
                container.getNavigationBar().setTitleClickable(clickable, R.mipmap.img_arrow_black_up);
            }
            if (clickable) {
                container.getNavigationBarEventHandler().addJSCallback(NavigationBarEventHandler.OnClickNbTitle, successCallback);
            } else {
                container.getNavigationBarEventHandler().removeJSCallback(NavigationBarEventHandler.OnClickNbTitle);
            }
        }
    }

    /**
     * 设置导航栏最右侧按钮
     * isShow：是否显示，0：隐藏 其他：显示
     * text：文字按钮
     * imageUrl:图片按钮，格式为url地址,优先级高
     */
    @JSMethod(uiThread = true)
    public void setRightBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            int which = params.getIntValue("which");
            boolean isShow = !"0".equals(params.getString("isShow"));
            String text = params.getString("text");
            String imageUrl = params.getString("imageUrl");
            if (isShow) {
                container.getNavigationBar().setRightBtn(which, imageUrl, text);
                container.getNavigationBarEventHandler().addJSCallback(NavigationBarEventHandler.OnClickNbRight + which, successCallback);
            } else {
                container.getNavigationBar().hideRightBtn(which);
                container.getNavigationBarEventHandler().removeJSCallback(NavigationBarEventHandler.OnClickNbRight + which);
            }
        }
    }

    /**
     * 设置导航栏左侧按钮
     * isShow：是否显示，0：隐藏 其他：显示
     * text：文字按钮
     * imageUrl:图片按钮，格式为url地址,优先级高
     */
    @JSMethod(uiThread = true)
    public void setLeftBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        FitContainerFragment container = UiUtil.getContainerFragment(mWXSDKInstance);
        if (container != null) {
            boolean isShow = !"0".equals(params.getString("isShow"));
            String text = params.getString("text");
            String imageUrl = params.getString("imageUrl");
            if (isShow) {
                container.getNavigationBar().setLeftBtn(imageUrl, text);
                container.getNavigationBarEventHandler().addJSCallback(NavigationBarEventHandler.OnClickNbLeft, successCallback);
            } else {
                container.getNavigationBar().hideLeftBtn();
                container.getNavigationBarEventHandler().removeJSCallback(NavigationBarEventHandler.OnClickNbLeft);
            }
        }
    }

}
