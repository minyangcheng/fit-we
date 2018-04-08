package com.fit.we.library.resource;

import android.content.Context;
import android.text.TextUtils;

import com.fit.we.library.FitConstants;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.AssetsUtil;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.SignatureUtil;

import java.io.File;
import java.util.Date;

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
