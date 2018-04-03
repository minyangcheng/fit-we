package com.min.hybrid.library.container;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.min.fit.weex.R;
import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.FitLog;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.HashMap;
import java.util.Map;

public class FitContainerFragment extends Fragment implements IWXRenderListener {

    private static final String TAG = FitContainerFragment.class.getSimpleName();

    private FrameLayout mContainer;
    private WXSDKInstance mWXSDKInstance;
    private String mUrl;
    private JSONObject mRouteInfo;

    public static FitContainerFragment newInstance(String bundleUrl) {
        FitContainerFragment fragment = new FitContainerFragment();
        Bundle args = new Bundle();
        args.putString(WXSDKInstance.BUNDLE_URL, bundleUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromArguments();

        View view = View.inflate(getActivity(), R.layout.fragment_container, null);
        mContainer = (FrameLayout) view.findViewById(R.id.fragment_container);
        mWXSDKInstance = new WXSDKInstance(getActivity());
        mWXSDKInstance.registerRenderListener(this);

        HashMap<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, mUrl);
        options.put(FitConstants.KEY_ROUTE_INFO, mRouteInfo);
        mWXSDKInstance.renderByUrl(mUrl, mUrl, options, null, WXRenderStrategy.APPEND_ASYNC);
    }

    private void getDataFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(WXSDKInstance.BUNDLE_URL)) {
            String bundleUrl = bundle.getString(WXSDKInstance.BUNDLE_URL);
            Uri uri = Uri.parse(bundleUrl);
            mUrl = bundleUrl.replace("?" + uri.getQuery(), "");
            String routeStr = uri.getQueryParameter("routeInfo");
            if (TextUtils.isEmpty(mUrl) || TextUtils.isEmpty(routeStr)) {
                getActivity().finish();
            } else {
                mRouteInfo = JSON.parseObject(routeStr);
            }
        } else {
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
        FitLog.d(TAG, "url=%s,routeInfo=%s onRenderSuccess", mUrl, mRouteInfo.toJSONString());
        mContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("key", "value");
                mWXSDKInstance.fireGlobalEventCallback("geolocation", params);
            }
        }, 2000);
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        FitLog.d(TAG, "url=%s,routeInfo=%s onRefreshSuccess", mUrl, mRouteInfo.toJSONString());
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        FitLog.e(TAG, "url=%s,routeInfo=%s onException msg=%s", mUrl, mRouteInfo.toJSONString(), msg);
    }

}
