package com.fit.we.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.fit.we.library.FitConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {

    private static final String[][] MIME_MapTable = {
        // {后缀名，MIME类型}
        {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"},
        {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"},
        {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"},
        {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"},
        {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        {".xls", "application/vnd.ms-excel"},
        {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"},
        {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"},
        {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"},
        {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"},
        {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"},
        {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"},
        {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"},
        {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"},
        {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"},
        {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"},
        {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"},
        {".ppt", "application/vnd.ms-powerpoint"},
        {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
        {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"},
        {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"},
        {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"},
        {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"},
        {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"},
        {"", "*/*"}};

    public static File getBasePath(Context context) {
        File appDir = new File(context.getFilesDir(), FitConstants.Resource.BASE_DIR);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    public static File getAppPath(String path, Context context) {
        File appFile = new File(getBasePath(context), path);
        if (!appFile.exists()) {
            appFile.mkdirs();
        }
        return appFile;
    }

    public static File getBundleDir(Context context) {
        return getAppPath(FitConstants.Resource.JS_BUNDLE, context);
    }

    public static File getTempBundleDir(Context context) {
        return getAppPath(FitConstants.Resource.JS_BUNDLE_ZIP, context);
    }

    public static File getCheckSignatureDir(Context context) {
        return getAppPath(FitConstants.Resource.JS_CHECK_SIGNATURE, context);
    }

    public static File getPathBundleDir(Context context, String path) {
        return new File(getBundleDir(context), path);
    }

    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String readFile(String path) {
        String str = null;
        try {
            FileInputStream inputStream = new FileInputStream(new File(path));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            str = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static byte[] loadLocalFile(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(path));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isEmptyDir(File file) {
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            return files.length == 0;
        }
        throw new RuntimeException("dir is null or not a directory");
    }


    public static File getFileInDir(File file, int index) {
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > index) {
                return files[index];
            }
            return null;

        }
        return null;
    }


    public static void unZip(File fromFile, File toFile) {
        if (toFile == null || fromFile == null) {
            return;
        }
        try {
            ZipUtil.unZip(fromFile.getAbsolutePath(), toFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] extractZip(File zipFile, String entryName) {
        ZipFile zf = null;
        ZipEntry ze = null;
        byte[] buffer = null;
        try {
            zf = new ZipFile(zipFile);
            ze = zf.getEntry(entryName);
            InputStream in = zf.getInputStream(ze);
            int size = in.available();
            buffer = new byte[size];
            in.read(buffer);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static void clearDir(File dir) {
        if (dir.isFile()) {
            dir.delete();
            return;
        }
        if (dir.isDirectory()) {
            File[] childFile = dir.listFiles();
            if (childFile == null || childFile.length == 0) {
                dir.delete();
                return;
            }
            for (File f : childFile) {
                clearDir(f);
            }
            dir.delete();
        }

    }

    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFile(file1);
                }
            }
            file.delete();
        }
    }

    public static void renameFile(File path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path, oldname);
            File newfile = new File(path, newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }

    public static void openFile(Context con, String path) {
        File file = new File(path);
        openFile(con, file);
    }

    /**
     * 弹出手机内所有可打开该文件类型的app选择
     *
     * @param con
     * @param file
     */
    public static void openFile(Context con, File file) {
        if (con == null) {
            return;
        }
        try {
            if (!file.exists() || file.isDirectory()) {
                UiUtil.toastShort(con, "文件未找到");
                return;
            }
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            // 跳转
            con.startActivity(intent);
        } catch (Exception e) {
            UiUtil.toastShort(con, "未知文件类型");
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (TextUtils.isEmpty(end))
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (String[] aMIME_MapTable : MIME_MapTable) {
            if (end.equals(aMIME_MapTable[0]))
                type = aMIME_MapTable[1];
        }
        return type;
    }

}
