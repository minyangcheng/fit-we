package com.min.hybrid.library.net;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class FileCallBack implements Callback {

    private static Handler mHandler=new Handler(Looper.getMainLooper());

    private File mDestFile;

    public FileCallBack(File destFile){
        this.mDestFile=destFile;
    }

    public FileCallBack(String destFilePath){
        this.mDestFile=new File(destFilePath);
    }

    public File saveFile(Response response, String url) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try{
            final long total = response.body().contentLength();
            is = response.body().byteStream();
            long sum = 0;
            createDirIfNoExit();
            fos = new FileOutputStream(mDestFile);
            while ((len = is.read(buf)) != -1){
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                onProgressOnUi(url,finalSum,total);
            }
            fos.flush();
            return mDestFile;
        } finally{
            try{
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            try{
                if (fos != null) fos.close();
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private void createDirIfNoExit(){
        File dir = mDestFile.getParentFile();
        if (!dir.exists()){
            dir.mkdirs();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFailOnUi(getUrlFromCall(call), e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String url=getUrlFromCall(call);
        onStartOnUI(url);

        if(call.isCanceled()){
            onFailOnUi(url,new Exception("request is cancel..."));
        }
        if (response.isSuccessful()) {
            try {
                File file=saveFile(response,url);
                onSuccessOnUI(url, file);
            } catch (IOException e) {
                onFailOnUi(url,e);
            }
        }else{
            onFailOnUi(url,new Exception("request is cancel..."));
        }
    }

    private String getUrlFromCall(Call call){
        return call.request().url().toString();
    }

    private void onStartOnUI(final String url){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onStart(url);
            }
        });
    }

    public void onProgressOnUi(final String url, final long progress, final long total){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onProgress(url,progress,total);
            }
        });
    }

    public void onSuccessOnUI(final String url, final File file){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(url, file);
            }
        });
    }

    public void onFailOnUi(final String url, final Throwable t){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFail(url,t);
            }
        });
    }

    public abstract void onStart(String url);

    public abstract void onProgress(String url, long progress, long total);

    public abstract void onSuccess(String url, File file);

    public abstract void onFail(String url, Throwable t);

}
