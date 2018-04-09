package com.fit.we.library.container;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.fit.we.library.FitConstants;
import com.fit.we.library.FitWe;
import com.fit.we.library.R;
import com.fit.we.library.bean.FitEvent;
import com.fit.we.library.bean.RouteInfo;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.NavigationBarEventHandler;
import com.fit.we.library.util.UriHandler;
import com.fit.we.library.widget.HudDialog;
import com.fit.we.library.widget.NavigationBar;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.Subscribe;

;

public class FitContainerFragment extends Fragment implements IWXRenderListener {

    private static final String TAG = FitContainerFragment.class.getSimpleName();

    public NavigationBar mNavigationBar;
    public FrameLayout mWeexContainer;

    private WXSDKInstance mWXSDKInstance;
    private RouteInfo mRouteInfo;

    private NavigationBarEventHandler mNbEventHandler;

    private long mCounter;
    private long mStartTime;

    private HudDialog mHudDialog;

    public static FitContainerFragment newInstance(RouteInfo routeInfo) {
        FitContainerFragment fragment = new FitContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(FitConstants.KEY_ROUTE_INFO, routeInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        findView(view);
        init();
        EventUtil.register(this);
        return view;
    }

    private void init() {
        mNbEventHandler = new NavigationBarEventHandler();
        initView();
        render();
        setDebugMode();
    }

    private void findView(View view) {
        mNavigationBar = (NavigationBar) view.findViewById(R.id.view_nb);
        mWeexContainer = (FrameLayout) view.findViewById(R.id.weex_container);
    }

    public NavigationBar getNavigationBar() {
        return mNavigationBar;
    }

    public FrameLayout getWeexContainer() {
        return mWeexContainer;
    }

    public NavigationBarEventHandler getNavigationBarEventHandler() {
        return mNbEventHandler;
    }

    private void initView() {
        mNavigationBar.setOnNavigationBarListener(new NavigationBar.INbOnClick() {
            @Override
            public void onNbBack() {
                backPress(NavigationBarEventHandler.OnClickNbBack);
            }

            @Override
            public void onNbLeft(View view) {
                if (view.getTag() != null && "close".equals(view.getTag().toString())) {
                    onNbBack();
                } else {
                    mNbEventHandler.onClickNbLeft();
                }
            }

            @Override
            public void onNbRight(View view, int which) {
                mNbEventHandler.onClickNbRight(which);
            }

            @Override
            public void onNbTitle(View view) {
                mNbEventHandler.onClickNbTitle(0);
            }
        });
        if (!mRouteInfo.showBackBtn) {
            mNavigationBar.hideNbBack();
        }
        if (mRouteInfo.orientation >= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && mRouteInfo.orientation <= ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
            getActivity().setRequestedOrientation(mRouteInfo.orientation);
        }
        if (!TextUtils.isEmpty(mRouteInfo.title)) {
            mNavigationBar.setNbTitle(mRouteInfo.title);
        }
        if (mRouteInfo.pageStyle == -1) {
            mNavigationBar.hide();
        }
    }

    public void onBackPressed() {
        backPress(NavigationBarEventHandler.OnClickSysBack);
    }

    public void backPress(String eventType) {
        if (mNbEventHandler.hasJSCallback(eventType)) {
            if (eventType == mNbEventHandler.OnClickNbBack) {
                mNbEventHandler.onClickNbBack();
            } else {
                mNbEventHandler.onSysClickBack();
            }
        } else {
            getActivity().finish();
        }
    }

    private void setDebugMode() {
        mNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCounter == 0) {
                    mStartTime = System.currentTimeMillis();
                }
                mCounter++;
                if (mCounter % 6 == 0) {
                    if (System.currentTimeMillis() - mStartTime < 2000) {
                        HybridDebugActivity.startActivity(getActivity());
                    }
                    mCounter = 0;
                }
            }
        });
    }

    private void render() {
        mWXSDKInstance = new WXSDKInstance(getActivity());
        mWXSDKInstance.registerRenderListener(this);
        String uri = UriHandler.handlePageUri(getActivity(), mRouteInfo.pagePath);
        mRouteInfo.uri = uri;
        HashMap<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, uri);
        options.put(FitConstants.KEY_ROUTE_INFO, mRouteInfo);
        options.put(FitConstants.KEY_NATIVE_PARAMS, FitWe.getInstance().getConfiguration().getNativeParams());
        if (uri.startsWith("http")) {
            mWXSDKInstance.renderByUrl(uri, uri, options, null, WXRenderStrategy.APPEND_ASYNC);
        } else {
            mWXSDKInstance.render(uri, WXFileUtils.loadFileOrAsset(uri, getActivity()), options, null, WXRenderStrategy.APPEND_ASYNC);
        }
        FitLog.d(FitConstants.LOG_TAG, "load page route=%s", JSON.toJSONString(mRouteInfo));
    }

    public void refresh() {
        if (mWXSDKInstance != null && !mWXSDKInstance.isDestroy()) {
            mWXSDKInstance.destroy();
        }
        render();
    }

    private void getDataFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FitConstants.KEY_ROUTE_INFO)) {
            mRouteInfo = (RouteInfo) bundle.getSerializable(FitConstants.KEY_ROUTE_INFO);
        }
        if (mRouteInfo == null || TextUtils.isEmpty(mRouteInfo.pagePath) || !mRouteInfo.pagePath.startsWith("fit://")) {
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
        EventUtil.unregister(this);
    }

    @Subscribe
    public void onEvent(FitEvent event) {
        if (mWXSDKInstance != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", event.type);
            map.put("data", event.data);
            mWXSDKInstance.fireGlobalEventCallback("eventbus", map);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        mWeexContainer.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        FitLog.d(TAG, "onRenderSuccess instanceId=%s , bundleUrl=%s", mWXSDKInstance.getInstanceId(), mWXSDKInstance.getBundleUrl());
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        FitLog.d(TAG, "routeInfo=%s onRefreshSuccess", JSON.toJSONString(mRouteInfo));
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        FitLog.e(TAG, "routeInfo=%s onException msg=%s", JSON.toJSONString(mRouteInfo), msg);
    }

    public void showHudDialog(String message, boolean cancelable) {
        if (mHudDialog == null) {
            mHudDialog = HudDialog.createProgressHud(getContext());
        }
        mHudDialog.setCancelable(cancelable);
        mHudDialog.setMessage(message);
        if (!mHudDialog.isShowing()) {
            mHudDialog.show();
        }
    }

    public void hideHudDialog() {
        if (mHudDialog != null && mHudDialog.isShowing()) {
            mHudDialog.dismiss();
        }
    }

}
