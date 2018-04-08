package com.fit.we.library.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.R;;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.UiUtil;
import com.fit.we.library.widget.NavigationBar;

import java.io.File;

public class HybridDebugActivity extends AppCompatActivity {

    private NavigationBar mNavigationBar;
    private TextView mInfoTv;
    private CheckBox mInterceptorCb;
    private EditText mUrlEt;
    private Button mSureBtn;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HybridDebugActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid_debug);
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
        mInfoTv.setText(getLocalBuildConfigContent());
        mInterceptorCb.setChecked(SharePreferenceUtil.getInterceptorActive(this));
        mUrlEt.setText(SharePreferenceUtil.getPageDevHostUrl(this));
        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure();
            }
        });
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
        mInfoTv = (TextView) findViewById(R.id.tv_info);
        mInterceptorCb = (CheckBox) findViewById(R.id.cb_interceptor);
        mUrlEt = (EditText) findViewById(R.id.et_url);
        mSureBtn = (Button) findViewById(R.id.btn_sure);
    }

    private void sure() {
        String url = mUrlEt.getText().toString();
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            UiUtil.toastShort(this, "拦截页面地址不是有效的url地址，请重新输入");
            return;
        }
        SharePreferenceUtil.setPageDevHostUrl(this, url);
        SharePreferenceUtil.setInterceptorActive(this, mInterceptorCb.isChecked());
        UiUtil.toastShort(this, "设置成功，重启后生效");
        exit();
    }

    private void exit() {
        FitUtil.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    finish();
                }
            }
        }, 1000);
    }

}
