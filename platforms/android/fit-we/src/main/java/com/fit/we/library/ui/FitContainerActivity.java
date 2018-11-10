package com.fit.we.library.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.fit.we.library.FitConstants;
import com.fit.we.library.R;
import com.fit.we.library.bean.Route;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.extend.weex.LongCallbackHandler;
import com.fit.we.library.extend.weex.WeexProxy;
import com.fit.we.library.util.StatusBarUtil;
import com.fit.we.library.widget.HudDialog;
import com.fit.we.library.widget.NavigationBar;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class FitContainerActivity extends AppCompatActivity {

    private WeexProxy mWeexProxy;
    private Route mRoute;

    private FrameLayout mContainerView;
    private NavigationBar mNavigationBar;

    private HudDialog mHudDialog;

    public static void startActivity(Context context, Route routeInfo) {
        Intent intent = new Intent(context, FitContainerActivity.class);
        intent.putExtra(FitConstants.KEY_ROUTE, routeInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initStatusBar();
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        setContentView(R.layout.layout_container);
        mContainerView = findViewById(R.id.view_container);
        mNavigationBar = findViewById(R.id.view_nb);
        initView();
        mWeexProxy = new WeexProxy(this, mRoute, mWeexHandler);
        mWeexProxy.setDebugMode(mNavigationBar);
        mWeexProxy.onCreate(mContainerView);
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
            setRequestedOrientation(mRoute.getScreenOrientation());
        }
        if (!TextUtils.isEmpty(mRoute.getTitle())) {
            mNavigationBar.setNbTitle(mRoute.getTitle());
        }
        if (!mRoute.isShowNavigationBar()) {
            mNavigationBar.hide();
        }
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra(FitConstants.KEY_ROUTE)) {
            mRoute = (Route) getIntent().getSerializableExtra(FitConstants.KEY_ROUTE);
        } else {
            finish();
        }
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.statusBarLightMode(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeexProxy.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWeexProxy.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWeexProxy.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWeexProxy.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeexProxy.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWeexProxy.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWeexProxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
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
                mHudDialog = HudDialog.createProgressHud(FitContainerActivity.this);
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
