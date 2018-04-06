package com.min.hybrid.library;

import android.content.Context;

import com.min.hybrid.library.extend.ImageAdapter;
import com.min.hybrid.library.resource.ResourceCheck;
import com.min.hybrid.library.resource.ResourceParse;
import com.min.hybrid.library.util.ImageLoaderWrap;
import com.min.hybrid.library.util.ModuleLoader;
import com.min.hybrid.library.util.SharePreferenceUtil;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

/**
 * Created by minyangcheng on 2018/3/17.
 */

public class FitWe {

    private static FitWe fitWe;

    private FitConfiguration configuration;
    private ResourceCheck resourceCheck;
    private ResourceParse resourceParse;

    private FitWe() {
    }

    public static FitWe getInstance() {
        if (fitWe == null) {
            synchronized (FitWe.class) {
                if (fitWe == null) {
                    fitWe = new FitWe();
                }
            }
        }
        return fitWe;
    }

    public void init(FitConfiguration configuration) {
        this.configuration = configuration;
        check();
        init();
    }

    private void check() {
        if (configuration == null) {
            throw new RuntimeException("config can not be null");
        }
        if (configuration.getApplication() == null) {
            throw new RuntimeException("config context can not be null");
        }
        if (configuration.getHostServer() == null) {
            throw new RuntimeException("config pageHostUrl can not be null");
        }
    }

    private void init() {
        setupModule();
        resourceCheck = new ResourceCheck(configuration.getContext(), configuration.getCheckApiHandler());
        resourceParse = new ResourceParse();
        ImageLoaderWrap.initImageLoader(configuration.getContext());
    }

    private void setupModule() {
        InitConfig initConfig = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(configuration.getApplication(), initConfig);
        try {
//            WXSDKEngine.registerModule("poneInfo", PhoneInfoModule.class);
//            WXSDKEngine.registerModule("tool", ToolModule.class);
//            WXSDKEngine.registerComponent("rich", RichText.class, false);
            ModuleLoader.loadModuleFromAsset(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResourceParse getResourceParse() {
        check();
        return resourceParse;
    }

    public ResourceCheck getResourceCheck() {
        check();
        return resourceCheck;
    }

    public FitConfiguration getConfiguration() {
        check();
        return configuration;
    }

    public void checkVersion() {
        getResourceCheck().checkVersion();
    }

    public long prepareJsBundle(Context context) {
        return getResourceParse().prepareJsBundle(context);
    }

    public String getVersion() {
        return SharePreferenceUtil.getVersion(configuration.getContext());
    }

    public Context getContext() {
        return configuration.getContext();
    }

}
