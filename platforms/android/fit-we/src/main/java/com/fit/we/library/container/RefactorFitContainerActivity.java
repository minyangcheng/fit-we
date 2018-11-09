package com.fit.we.library.container;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.fit.we.library.FitConstants;
import com.fit.we.library.R;
import com.fit.we.library.bean.Route;
import com.fit.we.library.util.NavigationBarEventHandler;
import com.fit.we.library.util.StatusBarUtil;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class RefactorFitContainerActivity extends AppCompatActivity {

    private WeexProxy mWeexProxy;
    private Route mRoute;

    public static void startActivity(Context context, Route routeInfo) {
        Intent intent = new Intent(context, RefactorFitContainerActivity.class);
        intent.putExtra(FitConstants.KEY_ROUTE, routeInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initStatusBar();
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        setContentView(R.layout.activity_container);
        FrameLayout containerView = findViewById(R.id.view_container);
        mWeexProxy = new WeexProxy(this, mRoute);
        mWeexProxy.onCreate(containerView);
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
    public void onBackPressed() {
        mWeexProxy.onBackPressed(NavigationBarEventHandler.OnClickSysBack);
    }
}
