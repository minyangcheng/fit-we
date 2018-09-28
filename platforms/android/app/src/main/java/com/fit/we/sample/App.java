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

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFitWe();
    }

    private void initFitWe() {
        FitConfiguration configuration = new FitConfiguration(this)
            .setDebug(BuildConfig.DEBUG)
            .setFitWeServer(BuildConfig.fitWeServer)
            .setCheckApiHandler(new CheckApiHandler() {
                @Override
                public void checkRequest(ResourceCheck resourceCheck) {
                    checkApiRequest(resourceCheck);
                }
            }).addNativeParam("apiServer", "http://www.fitwe.com");
        FitWe.getInstance().init(configuration);
    }

    private void checkApiRequest(final ResourceCheck resourceCheck) {
        Request request = new Request.Builder()
            .url("http://10.10.12.170:8080/update.json")
            .get()
            .build();
        Call call = HttpManager.getHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                resourceCheck.setCheckApiFailResp();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    int code = jsonObject.getIntValue("code");
                    if (code == 10000) {
                        resourceCheck.setCheckApiSuccessResp(jsonObject.getString("version"),
                            jsonObject.getString("md5"),
                            jsonObject.getString("dist"));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resourceCheck.setCheckApiFailResp();
            }
        });
    }

}
