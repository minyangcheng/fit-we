package com.fit.we.library.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fit.we.library.FitWe;
import com.fit.we.library.R;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.SignatureUtil;
import com.fit.we.library.widget.NavigationBar;

public class FitWeDebugActivity extends AppCompatActivity {

    private NavigationBar mNavigationBar;
    private EditText mServerEt;
    private Switch mUseLocalFileSwitch;
    private TextView mConfigTv;
    private TextView mParamsTv;
    private TextView mRestartTv;

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
        initViews();
    }

    private void initViews() {
        mNavigationBar.setNbTitle("开发设置");
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
        mServerEt.setText(FitWe.getInstance().getConfiguration().getFitWeServer());
        mServerEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharePreferenceUtil.setFitWeServer(FitWeDebugActivity.this, s.toString());
            }
        });
        mUseLocalFileSwitch.setChecked(SharePreferenceUtil.getLocalFileActive(this));
        mUseLocalFileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferenceUtil.setLocalFileActive(FitWeDebugActivity.this, isChecked);
            }
        });
        mConfigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("buildConfig", JSON.toJSONString(
                    SignatureUtil.getBuildConfigJsonObject(FileUtil.getBundleDir(FitWeDebugActivity.this)), true));
            }
        });
        mParamsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("nativeParams", JSON.toJSONString(FitWe.getInstance().getConfiguration().getNativeParams()));
            }
        });

        mRestartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
    }

    private void findViews() {
        mNavigationBar = (NavigationBar) findViewById(R.id.view_nb);
        mServerEt = (EditText) findViewById(R.id.et_server);
        mUseLocalFileSwitch = (Switch) findViewById(R.id.sw_use_local_file);
        mConfigTv = (TextView) findViewById(R.id.tv_look_config);
        mParamsTv = (TextView) findViewById(R.id.tv_look_params);
        mRestartTv = (TextView) findViewById(R.id.tv_restart);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        builder.show();
    }

}
