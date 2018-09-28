package com.fit.we.library.resource;

import android.content.Context;
import android.text.TextUtils;

import com.fit.we.library.FitConstants;
import com.fit.we.library.service.HotRefreshService;
import com.fit.we.library.util.AssetsUtil;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.SharePreferenceUtil;

import java.io.File;
import java.util.Date;

public class ResourceParse {

    public long prepareJsBundle(Context context) {
        long startTime = new Date().getTime();
        HotRefreshService.startService(context);

        if (SharePreferenceUtil.getLocalFileActive(context)) {
            String downloadVersion = SharePreferenceUtil.getDownLoadVersion(context);
            String assetsVersion = AssetsUtil.getAssetsVersionInfo(context);
            if (!TextUtils.isEmpty(downloadVersion) && FitUtil.compareVersion(downloadVersion, assetsVersion) > 0) {
                parseDownloadZip(context, downloadVersion);
                FitLog.d(FitConstants.LOG_TAG, "prepare js bundle from download , version=%s", downloadVersion);
            } else {
                String localVersion = SharePreferenceUtil.getVersion(context);
                if (TextUtils.isEmpty(localVersion) || FitUtil.compareVersion(assetsVersion, localVersion) > 0) {
                    parseAssetZip(context, assetsVersion);
                    FitLog.d(FitConstants.LOG_TAG, "prepare js bundle from assert , version=%s", assetsVersion);
                }
            }
        }
        long time = new Date().getTime() - startTime;
        FitLog.d(FitConstants.LOG_TAG, "prepare js bundle waste time=%s", time);
        return time;
    }

    private void parseAssetZip(Context context, String assetsVersion) {
        AssetsUtil.copyAssetsFile(context, FitConstants.Resource.BUNDLE_NAME, new File(FileUtil.getTempBundleDir(context), FitConstants.Resource.BUNDLE_NAME));
        FileUtil.deleteFile(FileUtil.getBundleDir(context));
        FileUtil.unZip(new File(FileUtil.getTempBundleDir(context), FitConstants.Resource.BUNDLE_NAME), FileUtil.getBundleDir(context));
        SharePreferenceUtil.setVersion(context, assetsVersion);
    }

    private void parseDownloadZip(Context context, String downloadVersion) {
        File zip = FileUtil.getFileInDir(FileUtil.getTempBundleDir(context), 0);
        FileUtil.deleteFile(FileUtil.getBundleDir(context));
        FileUtil.unZip(zip, FileUtil.getBundleDir(context));
        SharePreferenceUtil.setDownLoadVersion(context, null);
        SharePreferenceUtil.setVersion(context, downloadVersion);
    }

}
