package com.fit.we.library.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class FileCallBack implements Callback {

    private File mDestFile;

    public FileCallBack(File destFile) {
        this.mDestFile = destFile;
    }

    public FileCallBack(String destFilePath) {
        this.mDestFile = new File(destFilePath);
    }

    public File saveFile(Response response, String url) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            final long total = response.body().contentLength();
            is = response.body().byteStream();
            long sum = 0;
            createDirIfNoExit();
            fos = new FileOutputStream(mDestFile);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                onProgress(url, finalSum, total);
            }
            fos.flush();
            return mDestFile;
        } finally {
            try {
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void createDirIfNoExit() {
        File dir = mDestFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFail(getUrlFromCall(call), e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String url = getUrlFromCall(call);
        onStart(url);

        if (call.isCanceled()) {
            onFail(url, new Exception("request is cancel..."));
        }
        if (response.isSuccessful()) {
            try {
                File file = saveFile(response, url);
                onSuccess(url, file);
            } catch (IOException e) {
                onFail(url, e);
            }
        } else {
            onFail(url, new Exception("request is cancel..."));
        }
    }

    private String getUrlFromCall(Call call) {
        return call.request().url().toString();
    }

    public abstract void onStart(String url);

    public abstract void onProgress(String url, long progress, long total);

    public abstract void onSuccess(String url, File file);

    public abstract void onFail(String url, Throwable t);

}
