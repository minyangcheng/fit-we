package com.fit.we.library.resource;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConstants;
import com.fit.we.library.bean.ReceiveNewVersionEvent;
import com.fit.we.library.net.DownloadManager;
import com.fit.we.library.net.FileCallBack;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.Md5Util;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.SignatureUtil;

import java.io.File;

public class ResourceCheck {

    private Context mContext;
    private int mCurrentStatus = FitConstants.Version.SLEEP;
    private CheckApiHandler mCheckApiHandler;

    public ResourceCheck(Context context, CheckApiHandler checkApiHandler) {
        this.mContext = context;
        this.mCheckApiHandler = checkApiHandler;
    }

    public void checkVersion() {
        if (mCheckApiHandler == null) {
            return;
        }
        if (mCurrentStatus == FitConstants.Version.UPDATING || TextUtils.isEmpty(SharePreferenceUtil.getVersion(mContext))) {
            return;
        }
        startRequestCheckApi();
        mCheckApiHandler.checkRequest(this);
    }

    private void startRequestCheckApi() {
        mCurrentStatus = FitConstants.Version.UPDATING;
    }

    public void setCheckApiFailResp() {
        mCurrentStatus = FitConstants.Version.SLEEP;
    }

    public void setCheckApiSuccessResp(String remoteVersion, String md5, String dist) {
        String localVersion = SharePreferenceUtil.getVersion(mContext);
        if (FitUtil.compareVersion(remoteVersion, localVersion) > 0) {
            download(remoteVersion, md5, dist);
        } else {
            mCurrentStatus = FitConstants.Version.SLEEP;
        }
    }

    private void download(final String remoteVersion, final String md5, String dist) {
        if (hasDownloadVersion(remoteVersion)) {
            FitLog.d(FitConstants.LOG_TAG, "this version=%s zip has been download", remoteVersion);
            EventUtil.post(new ReceiveNewVersionEvent());
            mCurrentStatus = FitConstants.Version.SLEEP;
            return;
        }
        File destination = new File(FileUtil.getTempBundleDir(mContext), FitConstants.Resource.TEMP_BUNDLE_NAME);
        DownloadManager.getInstance()
            .downloadFile(dist, new FileCallBack(destination) {
                @Override
                public void onStart(String url) {
                    FitLog.d(FitConstants.LOG_TAG, "startDownload zip url=%s", url);
                }

                @Override
                public void onProgress(String url, long progress, long total) {
                }

                @Override
                public void onSuccess(String url, File file) {
                    FitLog.d(FitConstants.LOG_TAG, "completeDownload zip url=%s", url);
                    if (validateZipMd5(file, md5) && validateUnzipVersionAndSignature(remoteVersion)) {
                        RenameDeleteFile();
                        SharePreferenceUtil.setDownLoadVersion(mContext, remoteVersion);
                        EventUtil.post(new ReceiveNewVersionEvent());
                        FitLog.d(FitConstants.LOG_TAG, "this zip is valid , set downloadVersion=%s", remoteVersion);
                    } else {
                        FileUtil.deleteFile(new File(FileUtil.getTempBundleDir(mContext), FitConstants.Resource.TEMP_BUNDLE_NAME));
                    }
                    mCurrentStatus = FitConstants.Version.SLEEP;
                }

                @Override
                public void onFail(String url, Throwable t) {
                    FitLog.e(FitConstants.LOG_TAG, t);
                    mCurrentStatus = FitConstants.Version.SLEEP;
                }
            });
    }

    private boolean validateZipMd5(File file, String md5) {
        FitLog.d(FitConstants.LOG_TAG, "start validate zip md5");
        if (file.exists() && Md5Util.getFileMD5(file).equals(md5)) {
            return true;
        }
        //todo
        return true;
    }

    private boolean validateUnzipVersionAndSignature(String remoteVersion) {
        FitLog.d(FitConstants.LOG_TAG, "start validate zip version and signature");
        File zip = new File(FileUtil.getTempBundleDir(mContext), FitConstants.Resource.TEMP_BUNDLE_NAME);
        File checkSignatureDir = FileUtil.getCheckSignatureDir(mContext);
        FileUtil.deleteFile(checkSignatureDir);
        FileUtil.unZip(zip, checkSignatureDir);

        JSONObject buildConfigJsonObject = SignatureUtil.getBuildConfigJsonObject(checkSignatureDir);
        String buildVersion = buildConfigJsonObject.getString("version");
        String buildSignature = buildConfigJsonObject.getString("signature");
        String evaluateSignature = SignatureUtil.evaluateSignature(checkSignatureDir);

        boolean result = remoteVersion.equals(buildVersion) && evaluateSignature.equals(buildSignature);

        FileUtil.deleteFile(checkSignatureDir);
        return result;
    }

    private boolean hasDownloadVersion(String version) {
        String downLoadVersion = SharePreferenceUtil.getDownLoadVersion(mContext);
        if (!TextUtils.isEmpty(downLoadVersion)) {
            if (version.equals(downLoadVersion)) {
                return true;
            }
        }
        return false;
    }

    private void RenameDeleteFile() {
        FileUtil.deleteFile(new File(FileUtil.getTempBundleDir(mContext), FitConstants.Resource.BUNDLE_NAME));
        FileUtil.renameFile(FileUtil.getTempBundleDir(mContext), FitConstants.Resource.TEMP_BUNDLE_NAME, FitConstants.Resource.BUNDLE_NAME);
    }

}
