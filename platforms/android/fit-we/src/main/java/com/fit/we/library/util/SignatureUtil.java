package com.fit.we.library.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by minych on 18-3-3.
 */

public class SignatureUtil {

    public static String evaluateSignature(File dirFile) {
        ArrayList<File> dataList = getNeedSignatureFiles(dirFile);
        Collections.sort(dataList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
            }
        });
        String allMd5 = "";
        for (File file : dataList) {
            allMd5 += Md5Util.getFileMD5(file);
        }
        FitLog.d(FitConstants.LOG_TAG, "allMd5=%s", allMd5);
        return Md5Util.getMd5code(allMd5);
    }

    private static ArrayList<File> getNeedSignatureFiles(File dirFile) {
        ArrayList<File> resultList = new ArrayList<File>();
        String filePath;
        if (dirFile != null && dirFile.exists()) {
            File[] fileArr = dirFile.listFiles();
            for (File file : fileArr) {
                if (file.isDirectory()) {
                    resultList.addAll(getNeedSignatureFiles(file));
                } else {
                    filePath = file.getAbsolutePath();
                    if (filePath.endsWith(".js")) {
                        resultList.add(file);
                    }
                }
            }
        }
        return resultList;
    }

    public static String getSignatureFromBuildConfig(Context context) {
        try {
            File file = new File(FileUtil.getBundleDir(context), "buildConfig.json");
            String content = FileUtil.readFile(file.getAbsolutePath());
            JSONObject jsonObject = JSON.parseObject(content);
            return jsonObject.getString("signature");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
