package com.fit.we.library.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.fit.we.library.FitConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Assets 文件工具类
 */
public class AssetsUtil {

    /**
     * 读取assets 文件
     */
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String s;
            while ((s = bufReader.readLine()) != null) {
                result += s;
            }
            bufReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getAssetsVersionInfo(Context context) {
        try {
            InputStream is = context.getAssets().open(FitConstants.Resource.BUNDLE_NAME);
            String content = ZipUtil.getFileContentInZip(is, "buildConfig.json");
            return JSON.parseObject(content).getString("version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制assets 文件到指定目录
     *
     * @param fileName        文件名
     * @param destinationPath 指定目录的地址
     */
    public static boolean copyAssetsFile(Context context, String fileName, File destinationPath) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            outputStream = new FileOutputStream(destinationPath);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
