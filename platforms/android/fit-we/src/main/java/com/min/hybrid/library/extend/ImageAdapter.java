package com.min.hybrid.library.extend;

import android.text.TextUtils;
import android.widget.ImageView;

import com.min.hybrid.library.FitLog;
import com.min.hybrid.library.util.ImageLoaderWrap;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

public class ImageAdapter implements IWXImgLoaderAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        FitLog.d(TAG, "loadImage-->%s", url);
        if(TextUtils.equals("fit://assets/img/logo.png",url)){
            ImageLoaderWrap.displayHttpImage("http://10.10.12.151:8888/assets/logo.png", view);
        }
    }

}
