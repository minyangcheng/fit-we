package com.fit.we.library.container;

import com.taobao.weex.bridge.JSCallback;

/**
 * Created by minych on 18-11-9.
 */

public interface IWeexHandler {

    void addJSCallback(String key, JSCallback jsCallback);

    void removeJSCallback(String key);

    void refresh();

    void showHudDialog();

    void hideHudDialog();

    void setStatusBarVisibility(boolean visible);

    void setNBVisibility(boolean visible);

    void setNBBackBtnVisibility(boolean visible);

    void setNBTitle(String title);

    void setNBTitleClickable(boolean clickable, int arrow);

    void setNBRightBtn(int which, String imageUrl, String text);

    void hideRightBtn(int which);

}
