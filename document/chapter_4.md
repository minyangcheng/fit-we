# 打包的zip文件原生端怎样使用

## zip包解压校验过程

### 约定
首先解释下三个版本的含义：
1. localVersion：当本地已经成功解压zip包中的内容，该zip包中buildConfig中的version的值
2. assetsVersion：android中assets下bundle.zip中的buildConfig中的version的值
3. downloadVersion: 当去请求服务器自更新接口的时候，如果有新的weex包则将服务器返回的version作为downloadVersion值

### 步骤
1. 比较downloadVersion和assetsVersion，如果downloadVersion不为空且大于assetsVersion，则使用更新接口下载的zip包，否则使用assets中的bundle.zip
2. 将压缩包中的内容解压到FitApp文件夹中，进行签名检验，规则：获取page目录下所有js文件，获取文件的md5值，把文件的md5值相加，对相加后的结果取md5值，将此值和buildConfig.json中的signature做对比
3. 如果签名校验成功，则将localVersion的值更新buildConfig.json中的version
4. 如果签名校验失败，则将解压的文件全部删除

```
public class ResourceParse {

    public long prepareJsBundle(Context context) {
        long startTime = new Date().getTime();
        if (SharePreferenceUtil.getInterceptorActive(context)) {
            String downloadVersion = SharePreferenceUtil.getDownLoadVersion(context);
            String assetsVersion = AssetsUtil.getAssetsVersionInfo(context);
            if (!TextUtils.isEmpty(downloadVersion) && FitUtil.compareVersion(downloadVersion, assetsVersion) > 0) {
                File zip = FileUtil.getFileInDir(FileUtil.getTempBundleDir(context), 0);
                FileUtil.deleteFile(FileUtil.getBundleDir(context));
                FileUtil.unZip(zip, FileUtil.getBundleDir(context));
                updateVersion(context, downloadVersion);
                SharePreferenceUtil.setDownLoadVersion(context, null);
                FitLog.d(FitConstants.LOG_TAG, "prepare js bundle from download , version=%s", downloadVersion);
            } else {
                String localVersion = SharePreferenceUtil.getVersion(context);
                if (TextUtils.isEmpty(localVersion) || FitUtil.compareVersion(assetsVersion, localVersion) > 0) {
                    transferInsideBundle(context);
                    FitLog.d(FitConstants.LOG_TAG, "prepare js bundle from assert , version=%s",assetsVersion);
                }
            }
        }
        long time = new Date().getTime() - startTime;
        FitLog.d(FitConstants.LOG_TAG, "prepare js bundle waste time=%s", time);
        return time;
    }

    private void transferInsideBundle(Context context) {
        AssetsUtil.copyAssetsFile(context, FitConstants.Resource.BUNDLE_NAME, new File(FileUtil.getTempBundleDir(context), FitConstants.Resource.BUNDLE_NAME));
        FileUtil.deleteFile(FileUtil.getBundleDir(context));
        FileUtil.unZip(new File(FileUtil.getTempBundleDir(context), FitConstants.Resource.BUNDLE_NAME), FileUtil.getBundleDir(context));
        updateVersion(context, AssetsUtil.getAssetsVersionInfo(context));
    }

    private void updateVersion(Context context, String version) {
        if (version == null) {
            return;
        }
        if (validateSignature(context)) {
            FitLog.d(FitConstants.LOG_TAG, "signature 验证成功!!!!");
            SharePreferenceUtil.setVersion(context, version);
        } else {
            FitLog.e(FitConstants.LOG_TAG, "signature 验证失败!!!!");
            FileUtil.deleteFile(FileUtil.getBundleDir(context));
        }
    }

    private boolean validateSignature(Context context) {
        String evaluateSignature = SignatureUtil.evaluateSignature(FileUtil.getBundleDir(context));
        String buildSignature = SignatureUtil.getSignatureFromBuildConfig(context);
        return !TextUtils.isEmpty(evaluateSignature) && !TextUtils.isEmpty(buildSignature)
                && evaluateSignature.equals(buildSignature);
    }

}
```

## 原生端加载页面和图片

### 约定
定义`fit：//`协议scheme，用来约定页面路径和图片路径：
1. `fit://page/UiPage`代表UiPage界面路径，对应于weex端源码中的`src/page/UiPage.vue`，使用`this.$page.open('fit://page/UiPage');`即可打开UiPage界面
2.  `fit://assets/img/logo.png`代码logo.png图片路径，对应于weex端源码中的`src/assets/img/logo.png`，使用`<image style="margin:100px 0px;width:100px;height:100px" src="fit://assets/img/logo.png"></image>`设置图片

### 原生端加载js页面文件

* 找到页面对应的js的文件

```
public static String handlePageUri(Context context, String pagePath) {
    if (TextUtils.isEmpty(pagePath)) {
        return null;
    }
    if (!pagePath.startsWith("fit://")) {
        return pagePath;
    }
    pagePath = pagePath.replace("fit://", "");
    if (!pagePath.endsWith(".js")) {
        pagePath += ".js";
    }
    String uri = FitWe.getInstance().getConfiguration().getHostServer() + "/" + pagePath;
    if (SharePreferenceUtil.getInterceptorActive(context)) {
        File bundleDir = FileUtil.getBundleDir(context);
        File pageFile = new File(bundleDir.getAbsolutePath() + "/" + pagePath);
        if (pageFile.exists()) {
            uri = pageFile.getAbsolutePath();
        }
    }
    return uri;
}
```

* 渲染页面，并向页面传入必要参数

```
private void render() {
    mWXSDKInstance = new WXSDKInstance(getActivity());
    mWXSDKInstance.registerRenderListener(this);
    String uri = UriHandler.handlePageUri(getActivity(), mRouteInfo.pagePath);
    mRouteInfo.uri = uri;
    HashMap<String, Object> options = new HashMap<>();
    options.put(WXSDKInstance.BUNDLE_URL, uri);
    options.put(FitConstants.KEY_ROUTE_INFO, mRouteInfo);
    options.put(FitConstants.KEY_NATIVE_PARAMS, FitWe.getInstance().getConfiguration().getNativeParams());
    if (uri.startsWith("http")) {
        mWXSDKInstance.renderByUrl(uri, uri, options, null, WXRenderStrategy.APPEND_ASYNC);
    } else {
        mWXSDKInstance.render(uri, WXFileUtils.loadFileOrAsset(uri, getActivity()), options, null, WXRenderStrategy.APPEND_ASYNC);
    }
    FitLog.d(FitConstants.LOG_TAG, "load page route=%s", JSON.toJSONString(mRouteInfo));
}
```

### 原生端加载图片文件

* 找到页面对应的图片文件

```
public static String handleImageUri(Context context, String imagePath) {
    if (TextUtils.isEmpty(imagePath)) {
        return null;
    }
    if (!imagePath.startsWith("fit://")) {
        return imagePath;
    }
    imagePath = imagePath.replace("fit://", "");
    String uri = FitWe.getInstance().getConfiguration().getHostServer() + "/" + imagePath;
    if (SharePreferenceUtil.getInterceptorActive(context)) {
        File bundleDir = FileUtil.getBundleDir(context);
        File pageFile = new File(bundleDir.getAbsolutePath() + "/" + imagePath);
        if (pageFile.exists()) {
            uri = pageFile.getAbsolutePath();
        }
    }
    return uri;
}
```

* 加载图片到对应的ImageView上

```
public class ImageAdapter implements IWXImgLoaderAdapter {

    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        UriHandler.displayImage(view, url);
    }

}
```

