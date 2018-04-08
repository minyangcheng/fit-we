package com.fit.we.library.util;

import android.text.TextUtils;

import com.fit.we.library.FitConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 解压
 */
public class ZipUtil {

    public static void unZip(String zipFilePath, String outPath) throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry;
        String entryName;
        mkdirs(outPath);
        while ((entry = zipInputStream.getNextEntry()) != null) {
            entryName = entry.getName();
            FitLog.d(FitConstants.LOG_TAG, "unZip file=%s", entryName);
            if (entry.isDirectory()) {
                File folder = new File(outPath + File.separator + entryName);
                mkdirs(folder.getAbsolutePath());
            } else {
                File file = new File(outPath + File.separator + entryName);
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        zipInputStream.close();
    }

    public static String getFileContentInZip(InputStream inputStream, String fileName) throws Exception {
        String result = "";
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry;
        String entryName;
        String s;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            entryName = entry.getName();
            if (!entry.isDirectory() && entryName.equals(fileName)) {
                InputStreamReader inputReader = new InputStreamReader(zipInputStream);
                BufferedReader bufReader = new BufferedReader(inputReader);
                while ((s = bufReader.readLine()) != null) {
                    result += s;
                }
            }
        }
        zipInputStream.close();
        return result;
    }

    private static void mkdirs(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

}
