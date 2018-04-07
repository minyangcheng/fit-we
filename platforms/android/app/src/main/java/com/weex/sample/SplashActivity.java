package com.weex.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.min.hybrid.library.FitWe;
import com.min.hybrid.library.bean.RouteInfo;
import com.min.hybrid.library.container.FitContainerActivity;

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
                        toHome();
                    }
                }, 2500 - prepareTime);
            }
        }).start();
    }

    private void toHome() {
//        RouteInfo bean = new RouteInfo(FitWe.getInstance().getConfiguration().getHostServer());
        RouteInfo bean = new RouteInfo("fit://page/SamplePage");
        bean.showBackBtn = false;
        FitContainerActivity.startActivity(this, bean);
        finish();
    }

}
