package com.fit.we.library.bean;

import android.content.pm.ActivityInfo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class Route implements Serializable {

    private boolean showNavigationBar;

    private boolean showBackBtn;

    private String title;

    private int screenOrientation;

    private String pageUri;

    private JSONObject paramData;

    public Route() {
    }

    public boolean isShowNavigationBar() {
        return showNavigationBar;
    }

    public Route setShowNavigationBar(boolean showNavigationBar) {
        this.showNavigationBar = showNavigationBar;
        return this;
    }

    public boolean isShowBackBtn() {
        return showBackBtn;
    }

    public Route setShowBackBtn(boolean showBackBtn) {
        this.showBackBtn = showBackBtn;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Route setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public Route setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
        return this;
    }

    public String getPageUri() {
        return pageUri;
    }

    public Route setPageUri(String pageUri) {
        this.pageUri = pageUri;
        return this;
    }

    public JSONObject getParamData() {
        return paramData;
    }

    public Route setParamData(JSONObject paramData) {
        this.paramData = paramData;
        return this;
    }

    public static Route createRoute() {
        Route route = new Route();
        route.setShowNavigationBar(true);
        route.setShowBackBtn(true);
        route.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return route;
    }

}
