package com.fit.we.library.extend;

import android.widget.ImageView;

import com.fit.we.library.util.UriHandler;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

public class ImageAdapter implements IWXImgLoaderAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        UriHandler.displayImage(view, url);
    }

}
