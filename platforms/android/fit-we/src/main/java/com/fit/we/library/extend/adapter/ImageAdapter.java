package com.fit.we.library.extend.adapter;

import android.widget.ImageView;

import com.fit.we.library.util.UriHandler;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

public class ImageAdapter implements IWXImgLoaderAdapter {

    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        UriHandler.displayImage(view, url);
    }

}
