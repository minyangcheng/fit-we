package com.min.hybrid.library.resource;

import android.content.Context;
import android.text.TextUtils;

import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.util.AssetsUtil;
import com.min.hybrid.library.util.FileUtil;
import com.min.hybrid.library.util.FitUtil;
import com.min.hybrid.library.util.L;
import com.min.hybrid.library.util.SharePreferenceUtil;
import com.min.hybrid.library.util.SignatureUtil;

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
                L.d(FitConstants.LOG_TAG, "prepare js bundle from download , version=%s", downloadVersion);
            } else {
                String localVersion = SharePreferenceUtil.getVersion(context);
                if (TextUtils.isEmpty(localVersion) || FitUtil.compareVersion(assetsVersion, localVersion) > 0) {
                    transferInsideBundle(context);
                    L.d(FitConstants.LOG_TAG, "prepare js bundle from assert , version=%s",assetsVersion);
                }
            }
        }
        long time = new Date().getTime() - startTime;
        L.d(FitConstants.LOG_TAG, "prepare js bundle waste time=%s", time);
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
            L.d(FitConstants.LOG_TAG, "signature 验证成功!!!!");
            SharePreferenceUtil.setVersion(context, version);
        } else {
            L.e(FitConstants.LOG_TAG, "signature 验证失败!!!!");
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
