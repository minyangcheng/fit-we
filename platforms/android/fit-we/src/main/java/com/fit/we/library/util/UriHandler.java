package com.fit.we.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.fit.we.library.FitWe;

import java.io.File;

/**
 * Created by minych on 18-4-7.
 */

public class UriHandler {

    public static String handlePageUri(Context context, String pagePath) {
        if (TextUtils.isEmpty(pagePath)) {
            return null;
        }
        if (!pagePath.startsWith("fit://")) {
            return pagePath;
        }
        pagePath = pagePath.replace("fit://", "");
        if (!pagePath.endsWith(".js")) {
            pagePath += ".js";
        }
        String uri = FitWe.getInstance().getConfiguration().getHostServer() + "/" + pagePath;
        if (SharePreferenceUtil.getInterceptorActive(context)) {
            File bundleDir = FileUtil.getBundleDir(context);
            File pageFile = new File(bundleDir.getAbsolutePath() + "/" + pagePath);
            if (pageFile.exists()) {
                uri = pageFile.getAbsolutePath();
            }
        }
        return uri;
    }

    public static String handleImageUri(Context context, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return null;
        }
        if (!imagePath.startsWith("fit://")) {
            return imagePath;
        }
        imagePath = imagePath.replace("fit://", "");
        String uri = FitWe.getInstance().getConfiguration().getHostServer() + "/" + imagePath;
        if (SharePreferenceUtil.getInterceptorActive(context)) {
            File bundleDir = FileUtil.getBundleDir(context);
            File pageFile = new File(bundleDir.getAbsolutePath() + "/" + imagePath);
            if (pageFile.exists()) {
                uri = pageFile.getAbsolutePath();
            }
        }
        return uri;
    }

    public static void displayImage(ImageView view, String url) {
        String uri = UriHandler.handleImageUri(view.getContext(), url);
        if (uri.startsWith("http")) {
            ImageLoaderWrap.displayHttpImage(uri, view);
        } else {
            ImageLoaderWrap.displayFileImage(new File(uri), view);
        }
    }

}
