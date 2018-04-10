package com.fit.we.library.bean;

import android.content.pm.ActivityInfo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class RouteInfo implements Serializable {

    // 0:不加载导航栏 1：默认类型
    public int pageStyle = 1;

    public int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public String pagePath;

    public String title;

    public boolean showBackBtn = true;

    public JSONObject data;

    public String uri;

    public RouteInfo() {
    }

    public RouteInfo(String pagePath) {
        this.pagePath = pagePath;
    }

}