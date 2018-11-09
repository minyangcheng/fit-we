package com.fit.we.library.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fit.we.library.FitConstants;
import com.fit.we.library.R;
import com.fit.we.library.bean.Route;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.extend.weex.LongCallbackHandler;
import com.fit.we.library.extend.weex.WeexProxy;
import com.fit.we.library.widget.HudDialog;
import com.fit.we.library.widget.NavigationBar;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class FitContainerFragment extends Fragment {

    private WeexProxy mWeexProxy;
    private Route mRoute;

    private FrameLayout mContainerView;
    private NavigationBar mNavigationBar;

    private HudDialog mHudDialog;

    public static FitContainerFragment newInstance(Route routeInfo) {
        FitContainerFragment fragment = new FitContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(FitConstants.KEY_ROUTE, routeInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_container, container, false);
        mContainerView = view.findViewById(R.id.view_container);
        mNavigationBar = view.findViewById(R.id.view_nb);
        initView();
        mWeexProxy = new WeexProxy(getActivity(), mRoute, mWeexHandler);
        mWeexProxy.onCreate(mContainerView);
        mWeexProxy.setDebugMode(mContainerView);
        return view;
    }

    private void initView() {
        mNavigationBar.setOnNavigationBarListener(new NavigationBar.INbOnClick() {
            @Override
            public void onNbBack() {
                mWeexProxy.onBackPressed(LongCallbackHandler.OnClickNbBack);
            }

            @Override
            public void onNbLeft(View view) {
                if (view.getTag() != null && "close".equals(view.getTag().toString())) {
                    onNbBack();
                } else {
                    mWeexProxy.getLongCallbackHandler().onClickNbLeft();
                }
            }

            @Override
            public void onNbRight(View view, int which) {
                mWeexProxy.getLongCallbackHandler().onClickNbRight(which);
            }

            @Override
            public void onNbTitle(View view) {
                mWeexProxy.getLongCallbackHandler().onClickNbTitle(0);
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
    public void onResume() {
        super.onResume();
        mWeexProxy.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mWeexProxy.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWeexProxy.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWeexProxy.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWeexProxy.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWeexProxy.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        mWeexProxy.onBackPressed(LongCallbackHandler.OnClickSysBack);
    }

    private IWeexHandler mWeexHandler = new IWeexHandler() {

        @Override
        public LongCallbackHandler getLongCallbackHandler() {
            return mWeexProxy.getLongCallbackHandler();
        }

        @Override
        public void refresh() {
            mWeexProxy.refresh();
        }

        @Override
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

        @Override
        public void hideHudDialog() {
            if (mHudDialog != null && mHudDialog.isShowing()) {
                mHudDialog.dismiss();
            }
        }

        @Override
        public void setNBVisibility(boolean visible) {
            if (visible) {
                mNavigationBar.show();
            } else {
                mNavigationBar.hide();
            }
        }

        @Override
        public void setNBBackBtnVisibility(boolean visible) {
            if (visible) {
                mNavigationBar.showNbBack();
            } else {
                mNavigationBar.hideNbBack();
            }
        }

        @Override
        public void setNBTitle(String title, String subTitle) {
            mNavigationBar.setNbTitle(title);
        }

        @Override
        public void setNBTitleClickable(boolean clickable, int arrow) {
            mNavigationBar.setTitleClickable(clickable, arrow);
        }

        @Override
        public void setNBLeftBtn(String imageUrl, String text) {
            mNavigationBar.setLeftBtn(imageUrl, text);
        }

        @Override
        public void hideNBLeftBtn() {
            mNavigationBar.hideLeftBtn();
        }

        @Override
        public void setNBRightBtn(int which, String imageUrl, String text) {
            mNavigationBar.setRightBtn(which, imageUrl, text);
        }

        @Override
        public void hideNBRightBtn(int which) {
            mNavigationBar.hideRightBtn(which);
        }

        @Override
        public View getNBRoot() {
            return mNavigationBar.getNavigationView();
        }
    };

}
