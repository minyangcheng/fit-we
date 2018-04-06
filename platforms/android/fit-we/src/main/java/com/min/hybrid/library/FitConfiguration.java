package com.min.hybrid.library;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.min.hybrid.library.resource.CheckApiHandler;
import com.min.hybrid.library.util.SharePreferenceUtil;

/**
 * Created by minyangcheng on 2018/1/17.
 */

public class FitConfiguration {

    private Application application;
    private String hostServer;
    private CheckApiHandler checkApiHandler;

    public FitConfiguration(Application application) {
        this.application = application;
    }

    public String getHostServer() {
        String devPageUrl = SharePreferenceUtil.getPageDevHostUrl(getContext());
        if (TextUtils.isEmpty(devPageUrl)) {
            return hostServer;
        } else {
            return devPageUrl;
        }
    }

    public FitConfiguration setHostServer(String hostServer) {
        this.hostServer = hostServer;
        return this;
    }

    public Context getContext() {
        return application.getApplicationContext();
    }

    public Application getApplication() {
        return application;
    }

    public CheckApiHandler getCheckApiHandler() {
        return checkApiHandler;
    }

    public FitConfiguration setCheckApiHandler(CheckApiHandler checkApiHandler) {
        this.checkApiHandler = checkApiHandler;
        return this;
    }

}
