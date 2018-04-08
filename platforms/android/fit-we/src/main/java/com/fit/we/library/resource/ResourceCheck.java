package com.fit.we.library.resource;

import android.content.Context;
import android.text.TextUtils;

import com.fit.we.library.FitConstants;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.net.DownloadManager;
import com.fit.we.library.net.FileCallBack;
import com.fit.we.library.util.FileUtil;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.Md5Util;
import com.fit.we.library.util.SharePreferenceUtil;

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
        if (mCurrentStatus == FitConstants.Version.UPDATING || TextUtils.isEmpty(SharePreferenceUtil.getVersion(mContext))) {
            return;
        }
        if (SharePreferenceUtil.getInterceptorActive(mContext)) {
            if (mCheckApiHandler != null) {
                startRequestCheckApi();
                mCheckApiHandler.checkRequest(this);
            }
        }
    }

    private void startRequestCheckApi() {
        mCurrentStatus = FitConstants.Version.UPDATING;
    }

    public void setCheckApiFailResp(Exception e) {
        FitLog.e(FitConstants.LOG_TAG, e);
        mCurrentStatus = FitConstants.Version.SLEEP;
    }

    public void setCheckApiSuccessResp(String remoteVersion, String md5, String dist) {
        try {
            String localVersion = SharePreferenceUtil.getVersion(mContext);
            FitLog.d(FitConstants.LOG_TAG, "localVersion=%s,remoteVersion=%s,md5=%s,dist=%s", localVersion, remoteVersion, md5, dist);
            if (FitUtil.compareVersion(remoteVersion, localVersion) > 0) {
                download(remoteVersion, md5, dist);
            } else {
                mCurrentStatus = FitConstants.Version.SLEEP;
            }
        } catch (Exception e) {
            mCurrentStatus = FitConstants.Version.SLEEP;
            e.printStackTrace();
        }
    }

    private void download(final String remoteVersion, final String md5, String dist) {
        try {
            if (hasDownloadVersion(remoteVersion)) {
                FitLog.d(FitConstants.LOG_TAG, "this version=%s has been download", remoteVersion);
                mCurrentStatus = FitConstants.Version.SLEEP;
                return;
            }
            File destination = new File(FileUtil.getTempBundleDir(mContext), FitConstants.Resource.TEMP_BUNDLE_NAME);
            DownloadManager.getInstance()
                .downloadFile(dist, new FileCallBack(destination) {
                    @Override
                    public void onStart(String url) {
                        FitLog.d(FitConstants.LOG_TAG, "startDownload url=%s", url);
                    }

                    @Override
                    public void onProgress(String url, long progress, long total) {
                    }

                    @Override
                    public void onSuccess(String url, File file) {
                        FitLog.d(FitConstants.LOG_TAG, "complete download url=%s", url);
                        if (validateZip(file, md5)) {
                            RenameDeleteFile();
                            SharePreferenceUtil.setDownLoadVersion(mContext, remoteVersion);
                            FitLog.d(FitConstants.LOG_TAG, "set download version=%s", remoteVersion);
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
        } catch (Exception e) {
            mCurrentStatus = FitConstants.Version.SLEEP;
            e.printStackTrace();
        }
    }

    private boolean validateZip(File file, String md5) {
        try {
            if (file.exists() && Md5Util.getFileMD5(file).equals(md5)) {
                return true;
            }
            //todo
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
