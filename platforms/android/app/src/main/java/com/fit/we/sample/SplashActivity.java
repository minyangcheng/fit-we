package com.fit.we.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.fit.we.library.FitWe;
import com.fit.we.library.bean.RouteInfo;
import com.fit.we.library.container.FitContainerActivity;
import com.fit.we.library.service.HotRefreshService;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class SplashActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long prepareTime = FitWe.getInstance().prepareJsBundle(SplashActivity.this);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HotRefreshService.startService(SplashActivity.this);
                        toHome();
                    }
                }, 2500 - prepareTime);
            }
        }).start();
    }

    private void toHome() {
        RouteInfo bean = new RouteInfo("fit://page/SamplePage");
        bean.showBackBtn = false;
        FitContainerActivity.startActivity(this, bean);
        finish();
    }

}