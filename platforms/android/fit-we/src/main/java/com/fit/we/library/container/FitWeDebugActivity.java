package com.fit.we.library.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitWe;
import com.fit.we.library.R;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.UiUtil;
import com.fit.we.library.widget.NavigationBar;

import java.io.File;

public class FitWeDebugActivity extends AppCompatActivity {

    private NavigationBar mNavigationBar;
    private CheckBox mInterceptorCb;
    private TextView mUrlTv;
    private TextView mBuildConfigTv;
    private TextView mNativeParamsTv;

    public static void startActivity(Context context) {
        if (FitWe.getInstance().getConfiguration().isDebug()) {
            Intent intent = new Intent(context, FitWeDebugActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_we_debug);
        findViews();
        initData();
    }

    private void initData() {
        mNavigationBar.setNbTitle("开发调试");
        mNavigationBar.setOnNavigationBarListener(new NavigationBar.INbOnClick() {
            @Override
            public void onNbBack() {
                finish();
            }

            @Override
            public void onNbLeft(View view) {

            }

            @Override
            public void onNbRight(View view, int which) {

            }

            @Override
            public void onNbTitle(View view) {

            }
        });
        mUrlTv.setText(FitWe.getInstance().getConfiguration().getHostServer());
        mInterceptorCb.setChecked(SharePreferenceUtil.getInterceptorActive(this));
        mInterceptorCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FitLog.d("mytest",b+"");
                SharePreferenceUtil.setInterceptorActive(FitWeDebugActivity.this, b);
                UiUtil.toastShort(FitWeDebugActivity.this, "设置成功，请重启后生效");
            }
        });
        mBuildConfigTv.setText(getLocalBuildConfigContent());
        mNativeParamsTv.setText(JSON.toJSONString(FitWe.getInstance().getConfiguration().getNativeParams()));
    }

    private String getLocalBuildConfigContent() {
        File file = new File(FileUtil.getBundleDir(this), "buildConfig.json");
        if (file.exists()) {
            String s = FileUtil.readFile(file.getAbsolutePath());
            JSONObject jsonObject = JSON.parseObject(s);
            return JSON.toJSONString(jsonObject, true);
        } else {
            return "bundle.zip可能未被解压或解压失败";
        }
    }

    private void findViews() {
        mNavigationBar = (NavigationBar) findViewById(R.id.view_nb);
        mUrlTv = (TextView) findViewById(R.id.tv_url);
        mInterceptorCb = (CheckBox) findViewById(R.id.cb_interceptor);
        mBuildConfigTv = (TextView) findViewById(R.id.tv_build_config);
        mNativeParamsTv = (TextView) findViewById(R.id.tv_native_params);
    }

}
