package com.fit.we.sample;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConfiguration;
import com.fit.we.library.FitWe;
import com.fit.we.library.net.HttpManager;
import com.fit.we.library.resource.CheckApiHandler;
import com.fit.we.library.resource.ResourceCheck;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFitWe();
    }

    private void initFitWe() {
        FitConfiguration configuration = new FitConfiguration(this)
            .setHostServer(BuildConfig.fitWeHostServer)
            .setCheckApiHandler(new CheckApiHandler() {
                @Override
                public void checkRequest(ResourceCheck resourceCheck) {
                    checkApiRequest(resourceCheck);
                }
            }).addNativeParam("name", "minych");
        FitWe.getInstance().init(configuration);
    }

    private void checkApiRequest(final ResourceCheck resourceCheck) {
        Request request = new Request.Builder()
            .url("http://10.10.12.151:8888/static/updateJson?version=" + FitWe.getInstance().getVersion())
            .get()
            .build();
        Call call = HttpManager.getHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                resourceCheck.setCheckApiFailResp(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    if (jsonObject != null) {
                        resourceCheck.setCheckApiSuccessResp(jsonObject.getString("version"),
                            jsonObject.getString("md5"),
                            jsonObject.getString("dist"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
