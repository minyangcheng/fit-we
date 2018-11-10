package com.fit.we.library.extend.weex;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.fit.we.library.FitConstants;
import com.fit.we.library.FitWe;
import com.fit.we.library.R;
import com.fit.we.library.bean.FitEvent;
import com.fit.we.library.bean.ReceiveNewVersionEvent;
import com.fit.we.library.bean.RefreshWeexPage;
import com.fit.we.library.bean.Route;
import com.fit.we.library.ui.FitWeDebugActivity;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.UriHandler;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by minych on 18-11-9.
 */

public class WeexProxy {

    private Activity mActivity;
    private Route mRoute;
    private IWeexHandler mWeexHandler;

    public FrameLayout mWeexContainer;
    private ProgressBar mPb;

    private LongCallbackHandler mLongCallbackHandler = new LongCallbackHandler();

    private WXSDKInstance mWXSDKInstance;

    private long mCounter;
    private long mStartTime;

    public WeexProxy(Activity activity, Route route, IWeexHandler weexHandler) {
        this.mActivity = activity;
        this.mRoute = route;
        this.mWeexHandler = weexHandler;
    }

    public void onCreate(ViewGroup parent) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_weex, parent);
        findView(view);
        createWeexInstance();
        render();
        EventUtil.register(this);
    }

    private void findView(View view) {
        mWeexContainer = view.findViewById(R.id.view_weex);
        mPb = view.findViewById(R.id.pb);
    }

    public void onBackPressed(String eventType) {
        if (mLongCallbackHandler.hasJSCallback(eventType)) {
            if (eventType == mLongCallbackHandler.OnClickNbBack) {
                mLongCallbackHandler.onClickNbBack();
            } else {
                mLongCallbackHandler.onSysClickBack();
            }
        } else {
            fireLifeCycleEvent("onDestroy");
            mActivity.finish();
        }
    }

    public void setDebugMode(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCounter == 0) {
                    mStartTime = System.currentTimeMillis();
                }
                mCounter++;
                if (mCounter % 3 == 0) {
                    if (System.currentTimeMillis() - mStartTime < 3000) {
                        FitWeDebugActivity.startActivity(mActivity);
                    }
                    mCounter = 0;
                }
            }
        });
    }

    private void createWeexInstance() {
        mWXSDKInstance = new WXSDKInstance(mActivity);
        mWXSDKInstance.registerRenderListener(new IWXRenderListener() {
            @Override
            public void onViewCreated(WXSDKInstance instance, View view) {
                if (mWeexContainer != null) {
                    mWeexContainer.removeAllViews();
                    mWeexContainer.addView(view);
                }
            }

            @Override
            public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
                mPb.setVisibility(View.GONE);
                FitLog.d(FitConstants.LOG_TAG, "render page success : instanceId=%s , bundleUrl=%s", mWXSDKInstance.getInstanceId(), mWXSDKInstance.getBundleUrl());
            }

            @Override
            public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

            }

            @Override
            public void onException(WXSDKInstance instance, String errCode, String msg) {
                mPb.setVisibility(View.GONE);
                FitLog.d(FitConstants.LOG_TAG, "render page occur error : instanceId=%s , bundleUrl=%s", mWXSDKInstance.getInstanceId(), mWXSDKInstance.getBundleUrl());
                FitLog.d(FitConstants.LOG_TAG, "---------------------------------------->\n%s\n<----------------------------------------", msg);
            }
        });
        mWXSDKInstance.onActivityCreate();
        WeexHandlerManager.add(mWXSDKInstance, mWeexHandler);
        mPb.setVisibility(View.VISIBLE);
    }

    private void destroyWeexInstance() {
        WeexHandlerManager.remove(mWXSDKInstance);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.destroy();
        }
    }

    private void render() {
        String uri = UriHandler.handlePageUri(mActivity, mRoute.getPageUri());
        mRoute.setPageUri(uri);
        HashMap<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, uri);
        options.put(FitConstants.KEY_ROUTE, mRoute);
        options.put(FitConstants.KEY_NATIVE_PARAMS, FitWe.getInstance().getConfiguration().getNativeParams());
        if (uri.startsWith("http")) {
            mWXSDKInstance.renderByUrl(uri, uri, options, null, WXRenderStrategy.APPEND_ASYNC);
        } else {
            mWXSDKInstance.render(uri, WXFileUtils.loadFileOrAsset(uri, mActivity), options, null, WXRenderStrategy.APPEND_ASYNC);
        }
        FitLog.d(FitConstants.LOG_TAG, "load page route=%s", JSON.toJSONString(mRoute));
    }

    public void refresh() {
        destroyWeexInstance();
        createWeexInstance();
        render();
    }

    public void onStart() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
            fireLifeCycleEvent("onStart");
        }
    }

    public void onResume() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
            fireLifeCycleEvent("onResume");
        }
    }

    public void onPause() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
            fireLifeCycleEvent("onPause");
        }
    }

    public void onStop() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
            fireLifeCycleEvent("onStop");
        }
    }

    public void onDestroy() {
        destroyWeexInstance();
        EventUtil.unregister(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(FitEvent event) {
        if (mWXSDKInstance != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", event.type);
            map.put("data", event.data);
            mWXSDKInstance.fireGlobalEventCallback("eventbus", map);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(RefreshWeexPage event) {
        if (mWXSDKInstance != null) {
            refresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ReceiveNewVersionEvent event) {
        if (mWXSDKInstance != null && FitWe.getInstance().getConfiguration().isDebug() && SharePreferenceUtil.getLocalFileActive(mActivity)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle("提示")
                .setMessage("发现新版本，是否重启更新？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            builder.show();
        }
    }

    private void fireLifeCycleEvent(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageUri", mRoute.getPageUri());
        mWXSDKInstance.fireGlobalEventCallback(type, map);
    }

    public LongCallbackHandler getLongCallbackHandler() {
        return mLongCallbackHandler;
    }

}
