package com.min.hybrid.library.bean;

import android.content.pm.ActivityInfo;

import java.io.Serializable;

public class RouteInfo implements Serializable {

    // 0:不加载导航栏 1：默认类型
    public int pageStyle = 1;

    public int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public String pagePath;

    public String uri;

    public String title;

    public boolean showBackBtn = true;

    public String containerHashValue;

    public RouteInfo() {
    }

    public RouteInfo(String pagePath) {
        this.pagePath = pagePath;
    }

}
