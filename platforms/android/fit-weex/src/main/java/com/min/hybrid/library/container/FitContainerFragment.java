package com.min.hybrid.library.container;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.min.fit.weex.R;
import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.FitLog;
import com.min.hybrid.library.FitWe;
import com.min.hybrid.library.bean.RouteInfo;
import com.min.hybrid.library.util.FileUtil;
import com.min.hybrid.library.util.SharePreferenceUtil;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.io.File;
import java.util.HashMap;

public class FitContainerFragment extends Fragment implements IWXRenderListener {

    private static final String TAG = FitContainerFragment.class.getSimpleName();

    private FrameLayout mContainer;
    private WXSDKInstance mWXSDKInstance;
    private RouteInfo mRouteInfo;

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

        View view = View.inflate(getActivity(), R.layout.fragment_container, null);
        mContainer = (FrameLayout) view.findViewById(R.id.fragment_container);
        mWXSDKInstance = new WXSDKInstance(getActivity());
        mWXSDKInstance.registerRenderListener(this);
        render();
    }

    private void render() {
        String pagePath = mRouteInfo.pagePath.replace("fit://", "");
        String uri = FitWe.getInstance().getConfiguration().getHostServer() + "/" + pagePath + ".js";
        if (SharePreferenceUtil.getInterceptorActive(getActivity())) {
            File bundleDir = FileUtil.getBundleDir(getActivity());
            File pageFile = new File(bundleDir.getAbsolutePath() + "/" + pagePath + ".js");
            if (pageFile.exists()) {
                uri = pageFile.getAbsolutePath();
            }
        }
        mRouteInfo.uri = uri;
        HashMap<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, uri);
        options.put(FitConstants.KEY_ROUTE_INFO, mRouteInfo);
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
        if (mRouteInfo == null || TextUtils.isEmpty(mRouteInfo.pagePath)) {
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContainer.getParent() != null) {
            ((ViewGroup) mContainer.getParent()).removeView(mContainer);
        }
        return mContainer;
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        mContainer.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        FitLog.d(TAG, "routeInfo=%s onRenderSuccess", JSON.toJSONString(mRouteInfo));
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        FitLog.d(TAG, "routeInfo=%s onRefreshSuccess", JSON.toJSONString(mRouteInfo));
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        FitLog.e(TAG, "routeInfo=%s onException msg=%s", JSON.toJSONString(mRouteInfo), msg);
    }

}
