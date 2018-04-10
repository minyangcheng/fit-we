package com.fit.we.library;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.fit.we.library.resource.CheckApiHandler;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minyangcheng on 2018/4/1.
 */

public class FitConfiguration {

    private Application application;
    private String hostServer;
    private CheckApiHandler checkApiHandler;
    private IWXImgLoaderAdapter imageAdapter;
    private Map<String, String> nativeParams = new HashMap<>();
    private boolean debug;

    public FitConfiguration(Application application) {
        if (application == null) {
            throw new RuntimeException("fit-we application can not be null");
        }
        this.application = application;
    }

    public Context getContext() {
        return application.getApplicationContext();
    }

    public Application getApplication() {
        return application;
    }

    public String getHostServer() {
        return hostServer;
    }

    public FitConfiguration setHostServer(String hostServer) {
        this.hostServer = hostServer;
        return this;
    }

    public CheckApiHandler getCheckApiHandler() {
        return checkApiHandler;
    }

    public FitConfiguration setCheckApiHandler(CheckApiHandler checkApiHandler) {
        this.checkApiHandler = checkApiHandler;
        return this;
    }

    public FitConfiguration addNativeParam(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return this;
        }
        nativeParams.put(key, value);
        return this;
    }

    public Map<String, String> getNativeParams() {
        return nativeParams;
    }

    public IWXImgLoaderAdapter getImageAdapter() {
        return imageAdapter;
    }

    public void setImageAdapter(IWXImgLoaderAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    public boolean isDebug() {
        return debug;
    }

    public FitConfiguration setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }
}