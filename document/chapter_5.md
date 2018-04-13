# 如何接入

## android

* 引入fit-we库
```
compile project(':fit-we')
```

* 初始化(通过FitWe和FitConfiguration类)
```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFitWe();
    }
    
    private void initFitWe() {
        FitConfiguration configuration = new FitConfiguration(this)
            .setDebug(BuildConfig.DEBUG)
            .setHostServer(BuildConfig.fitWeHostServer)
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
            .url("http://10.10.12.151:8889/checkWeexUpdate?version=" + FitWe.getInstance().getVersion())
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
                    int code = jsonObject.getIntValue("code");
                    if (code == 1000) {
                        resourceCheck.setCheckApiSuccessResp(jsonObject.getString("version"),
                            jsonObject.getString("md5"),
                            jsonObject.getString("dist"));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resourceCheck.setCheckApiFailResp(new Exception("weex bundle check fail"));
            }
        });
    }
}

```

## ios

ios库正在开发中.......

## weex

直接下载weex端开发模板即可

## 打包（以android端为例）

运行`npm run build`，将生成的zip包放入assets文件夹，命名为bundle.zip

## 调试 （以android端为例）

1. 运行`npm run dev`
2. 调用`FitConfiguration.setHostServer`设置weex文件开发所在的服务器地址
3. 修改代码后，将会自动页面


