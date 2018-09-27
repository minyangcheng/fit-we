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

    public static String handlePageUri(Context context, String pageUri) {
        if (TextUtils.isEmpty(pageUri)) {
            return null;
        }
        if (!pageUri.startsWith("fit://")) {
            return pageUri;
        }
        pageUri = pageUri.replace("fit://", "");
        if (!pageUri.endsWith(".js")) {
            pageUri += ".js";
        }
        String uri = FitWe.getInstance().getConfiguration().getFitWeServer() + "/" + pageUri;
        if (SharePreferenceUtil.getLocalFileActive(context)) {
            File bundleDir = FileUtil.getBundleDir(context);
            File pageFile = new File(bundleDir.getAbsolutePath() + "/" + pageUri);
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
        String uri = FitWe.getInstance().getConfiguration().getFitWeServer() + "/" + imagePath;
        if (SharePreferenceUtil.getLocalFileActive(context)) {
            File bundleDir = FileUtil.getBundleDir(context);
            File pageFile = new File(bundleDir.getAbsolutePath() + "/" + imagePath);
            if (pageFile.exists()) {
                uri = pageFile.getAbsolutePath();
            }
        }
        return uri;
    }

    public static void displayImage(ImageView view, String url) {
        if(view==null||TextUtils.isEmpty(url)){
            return;
        }
        String uri = UriHandler.handleImageUri(view.getContext(), url);
        if (uri.startsWith("http")) {
            ImageLoaderWrap.displayHttpImage(uri, view);
        } else {
            ImageLoaderWrap.displayFileImageWithNoCache(new File(uri), view);
        }
    }

}
