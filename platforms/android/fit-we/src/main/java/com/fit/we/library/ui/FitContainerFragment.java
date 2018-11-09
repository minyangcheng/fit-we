package com.fit.we.library.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import com.fit.we.library.extend.weex.LongCallbackHandler;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.SharePreferenceUtil;
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
import de.greenrobot.event.ThreadMode;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class FitContainerFragment extends Fragment {

    public NavigationBar mNavigationBar;
    public FrameLayout mWeexContainer;
    private ProgressBar mPb;

    private LongCallbackHandler mNbEventHandler = new LongCallbackHandler();
    private Route mRoute;

    private WXSDKInstance mWXSDKInstance;

    private long mCounter;
    private long mStartTime;

    private HudDialog mHudDialog;

    public static FitContainerFragment newInstance(Route routeInfo) {
        FitContainerFragment fragment = new FitContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(FitConstants.KEY_ROUTE, routeInfo);
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
        initView();
        createWeexInstance();
        render();
        setDebugMode();
        EventUtil.register(this);
        return view;
    }

    private void findView(View view) {
        mNavigationBar = view.findViewById(R.id.view_nb);
        mWeexContainer = view.findViewById(R.id.weex_container);
        mPb = view.findViewById(R.id.pb);
    }

    public NavigationBar getNavigationBar() {
        return mNavigationBar;
    }

    public LongCallbackHandler getNavigationBarEventHandler() {
        return mNbEventHandler;
    }

    private void initView() {
        mNavigationBar.setOnNavigationBarListener(new NavigationBar.INbOnClick() {
            @Override
            public void onNbBack() {
                backPress(LongCallbackHandler.OnClickNbBack);
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
        if (!mRoute.isShowBackBtn()) {
            mNavigationBar.hideNbBack();
        }
        if (mRoute.getScreenOrientation() >= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && mRoute.getScreenOrientation() <= ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
            getActivity().setRequestedOrientation(mRoute.getScreenOrientation());
        }
        if (!TextUtils.isEmpty(mRoute.getTitle())) {
            mNavigationBar.setNbTitle(mRoute.getTitle());
        }
        if (!mRoute.isShowNavigationBar()) {
            mNavigationBar.hide();
        }
    }

    public void onBackPressed() {
        backPress(LongCallbackHandler.OnClickSysBack);
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
                if (mCounter % 3 == 0) {
                    if (System.currentTimeMillis() - mStartTime < 3000) {
                        FitWeDebugActivity.startActivity(getActivity());
                    }
                    mCounter = 0;
                }
            }
        });
    }

    private void createWeexInstance() {
        mWXSDKInstance = new WXSDKInstance(getActivity());
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
        mPb.setVisibility(View.VISIBLE);
    }

    private void render() {
        String uri = UriHandler.handlePageUri(getActivity(), mRoute.getPageUri());
        mRoute.setPageUri(uri);
        HashMap<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, uri);
        options.put(FitConstants.KEY_ROUTE, mRoute);
        options.put(FitConstants.KEY_NATIVE_PARAMS, FitWe.getInstance().getConfiguration().getNativeParams());
        if (uri.startsWith("http")) {
            mWXSDKInstance.renderByUrl(uri, uri, options, null, WXRenderStrategy.APPEND_ASYNC);
        } else {
            mWXSDKInstance.render(uri, WXFileUtils.loadFileOrAsset(uri, getActivity()), options, null, WXRenderStrategy.APPEND_ASYNC);
        }
        FitLog.d(FitConstants.LOG_TAG, "load page route=%s", JSON.toJSONString(mRoute));
    }

    public void refresh() {
        if (mWXSDKInstance != null && !mWXSDKInstance.isDestroy()) {
            mWXSDKInstance.destroy();
        }
        createWeexInstance();
        render();
    }

    private void getDataFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FitConstants.KEY_ROUTE)) {
            mRoute = (Route) bundle.getSerializable(FitConstants.KEY_ROUTE);
        }
        if (mRoute == null || TextUtils.isEmpty(mRoute.getPageUri()) || !mRoute.getPageUri().startsWith("fit://")) {
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
            fireLifeCycleEvent("onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
            fireLifeCycleEvent("onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
            fireLifeCycleEvent("onPause");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
            fireLifeCycleEvent("onStop");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResult(requestCode, resultCode, data);
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
        if (mWXSDKInstance != null && FitWe.getInstance().getConfiguration().isDebug() && SharePreferenceUtil.getLocalFileActive(getContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
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
