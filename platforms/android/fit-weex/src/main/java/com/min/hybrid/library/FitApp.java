package com.min.hybrid.library;

import android.app.Application;

import com.min.hybrid.library.extend.compontent.RichText;
import com.min.hybrid.library.extend.module.PhoneInfoModule;
import com.min.hybrid.library.extend.module.ToolModule;
import com.min.hybrid.library.extend.ImageAdapter;
import com.min.hybrid.library.util.ImageLoaderWrap;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

public class FitApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this, config);
        try {
            WXSDKEngine.registerModule("poneInfo", PhoneInfoModule.class);
            WXSDKEngine.registerModule("tool", ToolModule.class);
            WXSDKEngine.registerComponent("rich", RichText.class, false);
        } catch (WXException e) {
            e.printStackTrace();
        }

        ImageLoaderWrap.initImageLoader(this);
    }
}
